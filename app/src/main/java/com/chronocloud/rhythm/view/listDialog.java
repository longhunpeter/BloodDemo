package com.chronocloud.rhythm.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chronocloud.rhythm.R;
import com.chronocloud.rhythm.RhythmMmBp.BasePush;
import com.chronocloud.rhythm.RhythmMmBp.ManufacturerSvrSendDataPush;
import com.chronocloud.rhythm.BLE.BluetoothLeService;
import com.chronocloud.rhythm.util.GlobalMethod;
import com.google.protobuf.ByteString;

public class listDialog extends AlertDialog implements OnItemClickListener {

	private ListView mList;

	public listDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public listDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public listDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_dialog);
		mList = (ListView) findViewById(R.id.listdialog);

		// EEC_system = -1; // 通用的错误
		// EEC_needAuth = -2; // 设备未登录
		// EEC_sessionTimeout = -3; // session超时，需要重新登录
		// EEC_decode = -4; // proto解码失败
		// EEC_deviceIsBlock = -5; // 设备出现异常，导致被微信临时性禁止登录
		// EEC_serviceUnAvalibleInBackground = -6; // ios处于后台模式，无法正常服务
		// EEC_deviceProtoVersionNeedUpdate = -7; // 设备的proto版本过老，需要更新
		// EEC_phoneProtoVersionNeedUpdate = -8; // 微信客户端的proto版本过老，需要更新
		// EEC_maxReqInQueue = -9; // 设备发送了多个请求，并且没有收到回包。微信客户端请求队列拥塞。
		// EEC_userExitWxAccount = -10; // 用户退出微信帐号。

		List<String> listA = new ArrayList<String>();
		// listA.add("通用的错误  1");
		// listA.add("设备未登录  2");
		// listA.add("session超时，需要重新登录  3");
		// listA.add("proto解码失败  4");
		// listA.add("设备出现异常，导致被微信临时性禁止登录  5 ");
		// listA.add("ios处于后台模式，无法正常服务   6");
		// listA.add("设备的proto版本过老，需要更新  7");
		// listA.add("微信客户端的proto版本过老，需要更新   8");
		// listA.add("设备发送了多个请求，并且没有收到回包。微信客户端请求队列拥塞   9");
		// listA.add("用户退出微信帐号。 10");
		// listA.add("发送 ST。 11");

		listA.add("更新系统时间指令");
		listA.add("更新系统电量指令");
		listA.add("刷新用户信息");
		listA.add("立即测量指令");
		listA.add("停止测量");
		listA.add("关闭设备");
		listA.add("设置用户信息指令");
		listA.add("获取设备信息");
		listA.add("获取用户数据");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				R.layout.text, listA);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// TODO Auto-generated method stub

		String command = "";

		switch (position) {
		case 0:
			command = "UT+" + GlobalMethod.getSystemTime();
			break;
		case 1:
			command = "UP";
			break;
		case 2:
			command = "UI+A";
			// command = "UI+B";
			break;
		case 3:
			command = "OI";
			break;
		case 4:
			command = "OS";
			break;
		case 5:
			command = "O";
			// command = "F";
			break;
		case 6:
			command = "SU ";
			break;
		case 7:
			command = "GD";
			break;
		case 8:
			command = "GN+A";
			// command = "GN+B";
			break;

		}

		if (!"".equals(command)) {

			ManufacturerSvrSendDataPush push = ManufacturerSvrSendDataPush
					.newBuilder().setBasePush(BasePush.newBuilder())
					.setData(ByteString.copyFromUtf8(command)).build();
			byte[] content = push.toByteArray();
			// ECI_resp_sendDataToManufacturerSvr = 20002,
			byte[] head = new byte[] { (byte) 254, 1, 0,
					(byte) (content.length + 8), 117, 49, 0, 0 };
			byte[] byteValue = GlobalMethod.mergerArray(head, content);

			if (byteValue.length <= 20) {

				for (int i = 0; i < byteValue.length; i++) {
					Log.e("listDiaog", "byteValue[" + i + "]:" + byteValue[i]);
				}
				BluetoothLeService.mmBluetoothLeService.WriteValue(byteValue);
				Log.e("list", "发送指令: " + command + " !成功！！");
			} else {

				Object[] objects = GlobalMethod.splitAry(byteValue, 20);

				for (Object obj : objects) {
					byte[] bb = (byte[]) obj;
					for (int i = 0; i < bb.length; i++) {

						Log.e("listDialog", "bb[" + i + "]" + bb[i]);

					}
					BluetoothLeService.mmBluetoothLeService.WriteValue(bb);
					Log.e("list", "发送指令n: " + command + " !成功！！");
				}
			}

		}

	}

	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // TODO Auto-generated method stub
	//
	// com.chronocloud.rhythm.RhythmMmBp.AuthResponse.Builder builder =
	// AuthResponse
	// .newBuilder();
	// byte[] b = new byte[0];
	// byte[] head = new byte[] { (byte) 254, 1, 0, (byte) (b.length + 8), 78,
	// 33, 0, 0 };
	// AuthResponse authResponse;
	// switch (position) {
	// case 0:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_system_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 1:
	// // EEC_needAuth
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_needAuth_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 2:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_sessionTimeout_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 3:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_decode_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 4:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_deviceIsBlock_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 5:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_serviceUnAvalibleInBackground_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 6:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_deviceProtoVersionNeedUpdate_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 7:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_phoneProtoVersionNeedUpdate_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	// case 8:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_maxReqInQueue_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	//
	// case 9:
	// builder.setBaseResponse(BaseResponse.newBuilder().setErrCode(
	// EmErrorCode.EEC_userExitWxAccount_VALUE));
	// authResponse = builder.setAesSessionKey(
	// ByteString.copyFrom("20".getBytes())).build();
	//
	// b = authResponse.toByteArray();
	// break;
	//
	// case 10:
	// ManufacturerSvrSendDataPush sendDataPush = ManufacturerSvrSendDataPush
	// .newBuilder()
	// .setData(ByteString.copyFromUtf8("UT+1405121700"))
	// .setBasePush(BasePush.newBuilder()).build();
	//
	// b = sendDataPush.toByteArray();
	// head = new byte[] { (byte) 254, 1, 0, (byte) (b.length + 8), 117,
	// 49, 0, 0 };
	// break;
	//
	// }
	//
	// // byte[] b = new byte[] {10,2,8,-1,18,2,50,48};
	//
	// int length = b.length + head.length;
	// Log.e("TAG", "length:" + length);
	//
	// byte[] byteValue = new byte[length];
	//
	// for (int i = 0; i < byteValue.length; i++) {
	// if (i <= head.length - 1) {
	// byteValue[i] = head[i];
	// } else {
	// byteValue[i] = b[i - head.length];
	//
	// }
	// }
	//
	// if (byteValue.length > 20) {
	//
	// byte[] aa = new byte[20];
	// byte[] bb = new byte[5];
	//
	// for (int i = 0; i < aa.length; i++) {
	// aa[i] = byteValue[i];
	// }
	//
	// for (int i = 0; i < bb.length; i++) {
	// bb[i] = byteValue[aa.length + i];
	// }
	//
	// BluetoothLeService.mmBluetoothLeService.WriteValue(aa);
	// BluetoothLeService.mmBluetoothLeService.WriteValue(bb);
	// Log.e("TAG", "----" + position
	// + "---------------发送成功 2次-----------------------");
	//
	// } else {
	// BluetoothLeService.mmBluetoothLeService.WriteValue(byteValue);
	// Log.e("TAG", "----" + position
	// + "---------------发送成功-----------------------");
	// }
	//
	// for (int i = 0; i < byteValue.length; i++) {
	// Log.e("TAG", "byteValue" + i + ":" + byteValue[i]);
	// }
	//
	// for (int i = 0; i < b.length; i++) {
	// Log.e("TAG", "b" + i + ":" + b[i]);
	// }
	// }
}
