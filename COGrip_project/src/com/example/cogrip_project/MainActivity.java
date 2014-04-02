package com.example.cogrip_project;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	// Debugging
    private static final String TAG = "MainActivity";
    private static final boolean D = true;
	
	private BluetoothFragment mBtFragment;
	private BluetoothAdapter mBluetoothAdapter;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
		
        // Setup Fragments
        
		FragmentManager fm = getSupportFragmentManager();

		if (savedInstanceState == null) {
			fm.beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// See if the BT fragment already exists
		mBtFragment = (BluetoothFragment) fm.findFragmentByTag("bluetooth");
		if (mBtFragment == null) {
			mBtFragment = new BluetoothFragment();
		}
	}
	
    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
        	mBtFragment.setHandler(mHandler);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothFragment.MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothFragment.STATE_CONNECTED:
                	Log.d(TAG, "Connected");
                    break;
                case BluetoothFragment.STATE_CONNECTING:
                	Log.d(TAG, "Connected");
                    break;
                case BluetoothFragment.STATE_LISTEN:
                	Log.d(TAG, "Connected");
                case BluetoothFragment.STATE_NONE:
                	Log.d(TAG, "Connected");
                    break;
                }
                break;
            case BluetoothFragment.MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Log.d(TAG, "Writing message: " + writeMessage);
                break;
            case BluetoothFragment.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                Log.d(TAG, "Read message: " + readMessage);
                break;
            case BluetoothFragment.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                String deviceName = msg.getData().getString(BluetoothFragment.DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + deviceName, Toast.LENGTH_SHORT).show();
                break;
            case BluetoothFragment.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothFragment.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
}
