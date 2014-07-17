package com.chronocloud.rhythm.BLE;

public class BleUtil {
	
	//写出数据岛蓝牙
	
	public static void WriteOut(String string) {
		// TODO Auto-generated method stub
		if (BluetoothLeService.mmBluetoothLeService!=null) {
			BluetoothLeService.mmBluetoothLeService.WriteValue(string);
		}
		
	}
}
