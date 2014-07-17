package com.chronocloud.rhythm;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TabActivity;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.chronocloud.rhythm.RhythmMmBp.BasePush;
import com.chronocloud.rhythm.RhythmMmBp.BaseResponse;
import com.chronocloud.rhythm.RhythmMmBp.ManufacturerSvrSendDataPush;
import com.chronocloud.rhythm.RhythmMmBp.SendDataToManufacturerSvrRequest;
import com.chronocloud.rhythm.RhythmMmBp.SendDataToManufacturerSvrResponse;
import com.chronocloud.rhythm.BLE.BlueToothScanning;
import com.chronocloud.rhythm.BLE.BlueToothScanning.OnAutoDismiss;
import com.chronocloud.rhythm.BLE.BluetoothLeService;
import com.chronocloud.rhythm.BLE.rec;
import com.chronocloud.rhythm.BLE.rec.OnServiceData;
import com.chronocloud.rhythm.BLE.rec.OnServiceStateListener;
import com.chronocloud.rhythm.util.GlobalMethod;
import com.chronocloud.rhythm.view.listDialog;
import com.chronocloud.rhythm.view.pop;
import com.chronocloud.rhythm.view.pop.OnClickInMain;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

@SuppressLint({ "InlinedApi", "NewApi" })
public class MainTabActivity extends TabActivity implements OnClickListener,
		LeScanCallback, OnAutoDismiss, OnClickInMain, OnServiceStateListener,
		OnServiceData {
    	private final String TAG = this.getClass().getName();
    private List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

	public static boolean ISLIVE = false;
    public final String TAB_MEASURE = "tab_measure"; // 搜索
    public final String TAB_DATA = "tab_data"; // 我的

	public final String TAB_SET = "tab_set"; // 设置
	private TabHost mTabHost;
	private RadioGroup mRgTab;
	private Button mBtnSearch;
	private TextView mTvTitle;
	private TextView mTvConnection;
	private BlueToothScanning blueToothScanning;
	private pop mpop;

	/**
	 * 接收蓝牙数据的集合
	 */
	private List<byte[]> byteList = new ArrayList<byte[]>();
	/**
	 * 记录字节数组的总长度
	 */
	private int byteLength = 0;
	/**
	 * 记录当前字节数组的长度
	 */
	private int currByteLength = 0;

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			BluetoothLeService.mmBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();

			if (!BluetoothLeService.mmBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			Log.e(TAG, "mBluetoothLeService is okay");
			// Automatically connects to the device upon successful start-up
			// initialization.
			// mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			// mBluetoothLeService = null;
		}
	};
	private BluetoothDevice bluetoothDevice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_tab);

		// CRC32 CRC = new CRC32();
		// CRC.update(buf);

		initView();
		initTabSpec();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ISLIVE = false;
		// 解绑
		disConnectionBle();
		unbindService(mServiceConnection);
		super.onDestroy();
	}

	/**
	 * 
	 * @description 初始化 view
	 * @author ljw 2014-4-24 上午10:20:21
	 */
	private void initView() {
		ISLIVE = true;
		// 绑定service
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		Log.d(TAG,
				"Try to bindService="
						+ bindService(gattServiceIntent, mServiceConnection,
								BIND_AUTO_CREATE));
		// 设置连接状态监听

		rec.setOnServiceStateListener(this);

		rec.setOnServiceData(this);
		blueToothScanning = BlueToothScanning.getInstance(this);

		mTabHost = getTabHost();

		mBtnSearch = (Button) findViewById(R.id.btn_search);

		mTvConnection = (TextView) findViewById(R.id.tv_connection);

		mTvTitle = (TextView) findViewById(R.id.tv_title);

		mRgTab = (RadioGroup) findViewById(R.id.rg_tab);

		blueToothScanning.setLeScanCallback(this);
		blueToothScanning.setOnAutoDismiss(this);

		mBtnSearch.setOnClickListener(this);

		mRgTab.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_measure:

					mTabHost.setCurrentTabByTag(TAB_MEASURE);
					mBtnSearch.setVisibility(View.VISIBLE);
					mTvTitle.setText(R.string.measure);
					break;

				case R.id.rb_data:

					mTabHost.setCurrentTabByTag(TAB_DATA);
					mBtnSearch.setVisibility(View.GONE);
					mTvTitle.setText(R.string.measure_data);
					break;

				case R.id.rb_set:

					mTabHost.setCurrentTabByTag(TAB_SET);
					mBtnSearch.setVisibility(View.GONE);
					mTvTitle.setText(R.string.set_rhythm);
					break;

				}
			}
		});

	}

	/**
	 * 
	 * @description 初始化 TabSpec
	 * @author ljw 2014-4-24 上午10:20:51
	 */
	private void initTabSpec() {
		// TODO Auto-generated method stub
		TabSpec tsMeasure = mTabHost.newTabSpec(TAB_MEASURE).setIndicator(
				TAB_MEASURE);
		tsMeasure
				.setContent(new Intent(MainTabActivity.this,
						MeasureActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		mTabHost.addTab(tsMeasure);

		TabSpec tsData = mTabHost.newTabSpec(TAB_DATA).setIndicator(TAB_DATA);
		tsData.setContent(new Intent(MainTabActivity.this, DataActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		mTabHost.addTab(tsData);

		TabSpec tsSet = mTabHost.newTabSpec(TAB_SET).setIndicator(TAB_SET);
		tsSet.setContent(new Intent(MainTabActivity.this, SetActivity.class)
				.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		mTabHost.addTab(tsSet);

		mTabHost.setCurrentTabByTag(TAB_MEASURE);
	}

	/**
	 * 链接ble
	 */
	public void connectionBle() {
		Log.i(TAG, "start-conn");
		BluetoothLeService.mmBluetoothLeService
				.connect(BluetoothLeService.DeviceAddress);
		Log.i(TAG, "stop-conn");
	}

	/**
	 * 断开ble
	 */
	public void disConnectionBle() {
		BluetoothLeService.mmBluetoothLeService.disconnect();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btn_search:
			mpop = null;
			mpop = new com.chronocloud.rhythm.view.pop(view);
			mpop.setClickInMain(MainTabActivity.this);
			mpop.show();
			blueToothScanning.startScan();
			break;
		}
	}

	@Override
	public void onLeScan(final BluetoothDevice device, int rssi,
			byte[] scanRecord) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mDeviceList.indexOf(device) == -1) {

					mDeviceList.add(device);
					System.out.println("what");
				}

				mpop.setPopContent(mDeviceList);

			}
		});
	}

	// 自动断开扫描接口
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub

	}

	/**
	 * i==-2 : popwindow消失 stop scan i==-1 : 执行刷新操作, 暂时用不上 # else : 则是position
	 * 链接设备
	 */
	@Override
	public void click(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case -2:
			blueToothScanning.stopScan();
			mDeviceList.clear();
			break;
		case -1:

			break;

		default:
			bluetoothDevice = mDeviceList.get(i);
			BluetoothLeService.DeviceAddress = bluetoothDevice.getAddress();
			System.out.println(BluetoothLeService.DeviceAddress);
			connectionBle();
			break;
		}
	}

	// service 连接状态监听
	@Override
	public void Linked(Context co) {
		// TODO Auto-generated method stub
		mTvConnection.setText(bluetoothDevice.getName());
		// 发数据到设备
		// AuthResponse authResponse = AuthResponse.newBuilder()
		// .setBaseResponse(BaseResponse.newBuilder().setErrCode(0))
		// .setAesSessionKey(ByteString.copyFrom("20".getBytes())).build();
		//
		// byte[] arbyte = authResponse.toByteArray();
		// for (int i = 0; i < arbyte.length; i++) {
		// Log.e("TAG", "arbyte" + i + ":" + arbyte[i]);
		// }
		// // Log.e("TAG", "length:" + length);
		// int length = arbyte.length + 8;
		// byte[] head = new byte[] { (byte) 254, 1, 0, 16, 78, 33, 0, 0 };
		// // for (int i = 0; i < head.length; i++) {
		// // Log.e("TAG", "head" + i + ":" + head[i]);
		// // }
		//
		// byte[] byteValue = new byte[length];
		// for (int i = 0; i < byteValue.length; i++) {
		// if (i <= head.length - 1) {
		// byteValue[i] = head[i];
		// } else {
		// byteValue[i] = arbyte[i - head.length];
		//
		// }
		// }
		//
		// for (int i = 0; i < byteValue.length; i++) {
		// Log.e("TAG", "byteValue" + i + ":" + byteValue[i]);
		// }
		//
		// BluetoothLeService.mmBluetoothLeService.WriteValue(byteValue);

		Toast.makeText(getApplicationContext(),
				"connected :" + bluetoothDevice.getName(), 0).show();
	}

	@Override
	public void LoseLinked() {
		// TODO Auto-generated method stub
		mTvConnection.setText(R.string.connection_null);
	}

	@Override
	public void ReceiverData(byte[] data) {
		// TODO Auto-generated method stub
		Log.e("TAG", "activity接收到数据咯。。。。");

		if (byteLength == 0) {
			Dialog dialog = new listDialog(this);
			dialog.show();
			byteLength++;
		} else {

			byte[] content = GlobalMethod.splitArray(data);

			try {
				SendDataToManufacturerSvrRequest svrRequest = SendDataToManufacturerSvrRequest
						.parseFrom(content);

				Toast.makeText(this, svrRequest.toString(), 8000).show();
				Log.e("MainTabActivity", "" + svrRequest.toString());

				/**
				 * 接收数据成功发送ok到蓝牙
				 */

				SendDataToManufacturerSvrResponse response = SendDataToManufacturerSvrResponse
						.newBuilder()
						.setBaseResponse(
								BaseResponse.newBuilder().setErrCode(0))
						.setData(ByteString.copyFromUtf8("UTOK")).build();
				byte[] b = response.toByteArray();
				byte[] head = new byte[] { (byte) 254, 1, 0,
						(byte) (b.length + 8), 78, 34, 0, 0 };
				
				BluetoothLeService.mmBluetoothLeService.WriteValue(GlobalMethod
						.mergerArray(head, b));

			} catch (InvalidProtocolBufferException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
