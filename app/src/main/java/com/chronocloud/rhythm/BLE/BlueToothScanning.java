package com.chronocloud.rhythm.BLE;

import org.apache.http.impl.auth.UnsupportedDigestAlgorithmException;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * 蓝牙4.0 搜索
 * @author qs
 *
 */
@SuppressLint("NewApi")
public class BlueToothScanning {
	private Context context;
	private BluetoothAdapter mBluetoothAdapter;
	private LeScanCallback mLeScanCallback;
	private OnAutoDismiss mOnAutoDismiss;
	
	public boolean mScanning;
	private static BlueToothScanning blueToothScanning;
	/**
	 * 获取Scan实例
	 * @param context 上下文	
	 * @return 实例
	 */
	public static BlueToothScanning getInstance(Context context) {
		if (blueToothScanning==null) {
			blueToothScanning = new BlueToothScanning(context);
		}
		return blueToothScanning;
	}
	/**
	 * Used this clazz means your phone must be support BlueThooth 4.0 
	 * 
	 * @param context
	 */
	private BlueToothScanning(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		SupportBlueTooth4();
	}
	/**
	 * 开始搜索附近蓝牙
	 */
	public void startScan(){
		if (mLeScanCallback!=null&&mBluetoothAdapter.isEnabled()) {
			scanLeDevice(true);
		}
	}
	/**
	 * 停止搜索附近蓝牙
	 */
	public void stopScan(){
		if (mLeScanCallback!=null&&mScanning) {
			scanLeDevice(false);
		}
	}
	/**
	 * 检查是否支持 4.0 &&蓝牙是否开启
	 */
	private void SupportBlueTooth4(){
		// Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
		if (!mBluetoothAdapter.isEnabled()) {
			mBluetoothAdapter.enable();
		}
		// Use this check to determine whether BLE is supported on the device.  Then you can
		// selectively disable BLE-related features.
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
        	
        	throw new UnsupportedDigestAlgorithmException("Your phone is nonsupport BlueThooth 4.0");
        	
        }
	}
	/**
     * 搜索设备     true 开始  FALSE停止
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
        	if (mScanning) {
        		mScanning=false;
        		mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
//             Stops scanning after a pre-defined scan period.								这个是自动关闭 
//															            new Handler().postDelayed(new Runnable() {
//															                @Override
//															                public void run() {
//															                	try {
//															//                		Looper.prepare();
//																					
//																				} catch (Exception e) {
//																					// TODO: handle exception
//																				}
//															                	if(mScanning)
//															                	{
//															                		mBluetoothAdapter.stopLeScan(mLeScanCallback);
//															                		
//															                		if(mOnAutoDismiss!=null)
//															                			mOnAutoDismiss.dismiss();
//															                		mScanning = false;
//															                	}
//															                }
//															            }, 5000);//搜索时间5000;
            mScanning = true;
            //F000E0FF-0451-4000-B000-000000000000
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    
	public LeScanCallback getLeScanCallback() {
		if (mLeScanCallback==null) {
			throw new NullPointerException("没存过你好意思取吗");
		}
		return mLeScanCallback;
	}
	public void setLeScanCallback(LeScanCallback mLeScanCallback) {
		this.mLeScanCallback = mLeScanCallback;
	}
	//搜索完成后的停止监听
	public void setOnAutoDismiss(OnAutoDismiss OnAutoDismiss) {
		this.mOnAutoDismiss = OnAutoDismiss;
	}
	/**
	 * 
	 * @author qs
	 *
	 */
	//搜索完成后的停止监听
	public interface OnAutoDismiss{
		abstract void dismiss();
	}
    
}
