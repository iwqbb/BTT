package test.btt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class Bluetooth {
	private BluetoothAdapter mBTAdapter = null;
	private BluetoothDevice mBTDevice = null;
	private BluetoothSocket mBTSocket = null;
	
	/**
	 * BluetoothAdapterを取得
	 * @return BluetoothAdapter
	 */
	public BluetoothAdapter getBluetoothAdapter(){
		return mBTAdapter;
	}
	
	/**
	 * BluetoothDeviceを取得
	 * @return BluetoothDevice
	 */
	public BluetoothDevice getBluetoothDevice(){
		return mBTDevice;
	}
	
	/**
	 * BluetoothSocketを取得
	 * @return BluetoothSocket
	 */
	public BluetoothSocket getBluetoothSocket(){
		return mBTSocket;
	}
}
