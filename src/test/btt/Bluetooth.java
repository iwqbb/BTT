package test.btt;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Bluetooth {
	private BluetoothAdapter mBTAdapter = null;
	private BluetoothDevice mBTDevice = null;
	private BluetoothSocket mBTSocket = null;
	
	static public UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
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
	
	/**
	 * BluetoothSocketを生成して接続する
	 * @return 新たに接続した場合にはtrue，そうでなければfalse
	 * @throws IOException, NullPointerException
	 */
	public boolean connectSocket(){
		if (mBTSocket != null && mBTSocket.isConnected()){
			return false;
		}
		
		try {
			// Socket生成
			mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (NullPointerException e1) {
			Log.e("Bluetooth", "通信先のデバイスが指定されていません");
		} catch (IOException e2){
			Log.e("Bluetooth", "Socketを生成できません");
		}

		try {
			// Socket接続
			mBTSocket.connect();
		} catch (IOException e1) {
			Log.e("Bluetooth", "Socketが通信先のデバイスに接続できません");
			try {
				mBTSocket.close();
			} catch (IOException e2) {
				Log.e("Bluetooth", "Socketを閉じるのに失敗しました");
			}
		}
		
		return true;
	}
}
