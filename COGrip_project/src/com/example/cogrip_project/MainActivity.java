package com.example.cogrip_project;

import com.example.cogrip_project.BluetoothFragment.BluetoothState;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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

public class MainActivity extends ActionBarActivity implements BluetoothFragment.BluetoothListener {
	
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
        	String deviceName = "Nexus 10";
        	BluetoothDevice arduinoDevice = null;
        	for (BluetoothDevice bd : BluetoothAdapter.getDefaultAdapter().getBondedDevices()) {
        		if (bd.getName().equals(deviceName)) {
        			arduinoDevice = bd;
        		}
        	}
        	if (arduinoDevice != null) {
        		Log.d(TAG, "Found paired device " + deviceName + ", connecting...");
        		mBtFragment.connect(arduinoDevice, false);
        	} else {
        		Log.d(TAG, deviceName + " is not paired");
        		Toast.makeText(this, deviceName + " is not paired", Toast.LENGTH_LONG).show();
        	}
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

	@Override
	public void stateChange(BluetoothState newState) {
	     if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + newState);
	     switch (newState) {
         case STATE_CONNECTED:
         	Log.d(TAG, "Connected");
             break;
         case STATE_CONNECTING:
         	Log.d(TAG, "Connecting");
             break;
         case STATE_LISTEN:
         	Log.d(TAG, "Listening");
         case STATE_NONE:
         	Log.d(TAG, "None");
             break;
         }
	}

	@Override
	public void read(int bytes, byte[] buffer) {
        String readMessage = new String(buffer, 0, bytes);
        Log.d(TAG, "Got read message: " + readMessage);
	}

	@Override
	public void write(byte[] buffer) {
        String writeMessage = new String(buffer);
        Log.d(TAG, "Got write message: " + writeMessage);
	}

	@Override
	public void deviceName(String deviceName) {
		String message = "Connected to " + deviceName;
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        Log.d(TAG, message);
        
        byte[] msg = new byte[1];
        msg[0] = 'L';
        mBtFragment.write(msg);
	}

	@Override
	public void failure(String text) {
		String message = "Failure: " + text;
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		Log.d(TAG, "Connected to " + message);
	}
}
