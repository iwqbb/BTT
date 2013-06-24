package test.btt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Bluetooth {
	private BluetoothAdapter mBTAdapter = null;
	private BluetoothDevice mBTDevice = null;
	private BluetoothSocket mBTSocket = null;
	private Handler mHandler = null;
	
	static public UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	static final public int MSG_TEST_VALUE = 100; 
	
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
	 * コンストラクタ
	 * デフォルトのBluetoothAdapter取得する
	 */
	public Bluetooth(Handler handler){
		mBTAdapter = BluetoothAdapter.getDefaultAdapter();
		mHandler = handler;
	}
	
	/**
	 * BluetoothSocketを生成して接続する
	 * @return 新たに接続した場合にはtrue，そうでなければfalse
	 * @throws Exception
	 */
	public boolean connectSocket() throws Exception{
		if (mBTSocket != null && mBTSocket.isConnected()){
			return false;
		}
		
		// Socket生成
		try {
			Method m = mBTDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
			mBTSocket = (BluetoothSocket) m.invoke(mBTDevice, 1);
		} catch (Exception e){
			Log.e("Bluetooth", e.toString());
			throw e;
		}
		
		
		// Socket接続
		try {
			mBTSocket.connect();
		} catch (IOException e1) {
			Log.e("Bluetooth", "Socketが通信先のデバイスに接続できません");
			try {
				mBTSocket.close();
			} catch (IOException e2) {
				Log.e("Bluetooth", "Socketを閉じるのに失敗しました");
			}
			throw e1;
		}
		
		return true;
	}
	
	/**
	 * BluetoothSocketを閉じる
	 * @return closeした場合はtrue, closeするSocketがなかった場合はfalse
	 * @exception IOException
	 */
	public boolean closeSocket() throws IOException{
		if(mBTSocket == null){
			return false;
		}
		
		// Socket閉じる
		try {
			mBTSocket.close();
		} catch (IOException e) {
			Log.e("Bluetooth", "Socketを閉じるのに失敗しました");
			throw e;
		}
		mBTSocket = null;
		
		return true;
	}
	
	/**
	 * BluetoothのStreamにバッファを書き込む
	 * @param buf バッファ
	 * @exception IOException
	 */
	public void send(final byte[] buffer) throws IOException{
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
				OutputStream ost = null;
				// BluetoothのStream取得
				try {
					ost = mBTSocket.getOutputStream();
				} catch (IOException e) {
					Log.e("Bluetooth", "Streamを取得できませんでした");
				}
				// Streamに書き込み
				try {
					ost.write(buffer);
					Log.d("Bluetooth", "wrote :\"" + buffer.toString() + "\"");
				} catch (IOException e) {
					Log.e("Bluetooth", "Streamに書き込むことができませんでした");
				}
			}
		});
		
		//スレッド実行
		thread.run();
	}
	
	/**
	 * 
	 */
	public void receiveStart(){
		new Thread(){
			
			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
				while(true){
					try {
						InputStream ist = mBTSocket.getInputStream();
						final int val = ist.read();
						
						
						Log.d("Bluetooth", String.valueOf(val));
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// Update GUI
								Message msg = mHandler.obtainMessage(MSG_TEST_VALUE, val, 0);
								mHandler.sendMessage(msg);
							}
						});
						
					} catch (IOException e) {

					}
					
				}
			}
		}.start();
		
//		//スレッド実行
//		thread.run();
	}
	
	/**
	 * ペアリング済みのデバイスの中から指定した名前，アドレスのデバイスをBluetoothDeviceとしてセットする
	 * @param name デバイス名
	 * @param address デバイスのMACアドレス
	 * @return デバイスがあればtrue,なければfalse
	 * @exception Exception
	 */
	public boolean setBluetoothDevice(String name, String address) throws Exception{
		//ペアリングしているデバイス一覧を取得
		Set<BluetoothDevice> pairedDevice = null;
		try {
			pairedDevice = mBTAdapter.getBondedDevices();

			//指定したデバイスがあれば返す
			for(BluetoothDevice device : pairedDevice){
				if(device.getName().equals(name) && device.getAddress().equals(address)){
					mBTDevice = device;
					return true;
				}
			}
		} catch (Exception e){
			Log.e("Bluetooth", e.toString());
			throw e;
		}
		mBTDevice = null;
		return false;
	}
}
