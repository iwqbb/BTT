package test.btt;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Bluetooth mBt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(this, "hoge...", Toast.LENGTH_LONG).show();
		mBt = new Bluetooth(mHandler);
		try {
			mBt.setBluetoothDevice(getString(R.string.BTDEVICE_NAME_BLUETOOTHMATE), getString(R.string.BTDEVICE_ADDRESS_BLUETOOTHMATE));
			mBt.connectSocket();
			mBt.receiveStart();
		} catch (Exception e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		
		SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar1);
		seekBar.setMax(255);
		seekBar.setProgress(0);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int val = seekBar.getProgress();
				if(val < 0)	val = 0;
				if(val > 255) val = 255;
		    	byte[] buffer = {(byte)val};
		    	try {
					mBt.send(buffer);
				} catch (IOException ex) {
					
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO 自動生成されたメソッド・スタブ
				int val = seekBar.getProgress();
				if(val < 0)	val = 0;
				if(val > 255) val = 255;
		    	byte[] buffer = {(byte)val};
		    	try {
					mBt.send(buffer);
				} catch (IOException ex) {
					
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onDestroy(){
		try {
			mBt.closeSocket();
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
		mBt.receiveStop();
		super.onDestroy();
	}
	
    public void onClickButton1(View view){
    	byte[] buffer = {0x01};
    	try {
			mBt.send(buffer);
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
		}
    }
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case Bluetooth.MSG_TEST_VALUE:
            	TextView tv = (TextView)findViewById(R.id.textView1);
            	tv.setText("val=" + String.valueOf(msg.arg1));
            	break;
            }
        }
    };
}
