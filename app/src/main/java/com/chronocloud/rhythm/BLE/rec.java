package com.chronocloud.rhythm.BLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chronocloud.rhythm.MainTabActivity;

public class rec extends BroadcastReceiver {

	/**
	 * 接收蓝牙数据的集合
	 */
	private static byte[] data;
	/**
	 * 记录字节数组的总长度
	 */
	private static int byteLength = 0;
	/**
	 * 记录当前字节数组的长度
	 */
	private static int currByteLength = 0;

	private static OnServiceStateListener mOnSerViceStateListener;
	private static OnServiceData mOnServData;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!MainTabActivity.ISLIVE)
			return;
		// TODO Auto-generated method stub
		final String action = intent.getAction();
		if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) { // 链接成功
			System.out.println("链接成功");

		} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { // �断开
			System.out.println("断开");
			loselinked();
		} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
				.equals(action)) // 可以开始干活了
		{
			System.out.println("可以开始干活了了");
			linked(context);
		} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { // 收到数据
			// System.out.println(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
			// String stringExtra =
			// intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
			// System.err.println(stringExtra.toCharArray());

			byte[] b = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);

			if (byteLength == 0) {

				for (int i = 0; i < b.length; i++) {

					Log.e("rec", "rec---b[" + i + "]:" + b[i]);
				}
				byteLength = Integer.parseInt("" + b[2] + b[3]);
				data = new byte[byteLength];
				for (int i = 0; i < b.length; i++) {
					data[i] = b[i];
				}

			} else {

				for (int i = 0; i < b.length; i++) {
					data[currByteLength + i] = b[i];
				}
			}

			currByteLength += b.length;
			if (byteLength == currByteLength) {

				ReceiverData(data);

				byteLength = 0;
				currByteLength = 0;
				Log.e("rec", "接收所有数据完毕！");
			}

		}
	}

	private void linked(Context co) {
		if (mOnSerViceStateListener != null) {

			mOnSerViceStateListener.Linked(co);
		}
	}

	private void loselinked() {
		if (mOnSerViceStateListener != null) {

			mOnSerViceStateListener.LoseLinked();
		}

	}

	private void ReceiverData(byte[] b) {
		if (mOnServData != null) {
			mOnServData.ReceiverData(b);
		}
		// mOnSerViceStateListener.ReceiverData(intent);

	}

	public static void setOnServiceStateListener(OnServiceStateListener on) {
		mOnSerViceStateListener = null;
		mOnSerViceStateListener = on;
	}

	public static void setOnServiceData(OnServiceData on) {
		mOnServData = null;
		mOnServData = on;
	}

	public interface OnServiceStateListener {
		/**
		 * 链接成功
		 */
		abstract public void Linked(Context co);

		/**
		 * 失去链接
		 */
		abstract public void LoseLinked();
	}

	public interface OnServiceData {
		/**
		 * 接受数据
		 */
		abstract public void ReceiverData(byte[] b);
	}

}
