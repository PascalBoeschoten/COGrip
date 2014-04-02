package com.example.cogrip_project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BluetoothService extends Service {

	// Standard SerialPortService ID
	public final static UUID SPS_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805f9b34fb");
	public final static String ARDUINODEVICENAME = "OPIOT_BT_16";

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mDevice;
	private BluetoothSocket mSocket;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private Handler mHandler;
	private Thread mWorkerThread;

	private Button mSendButton;
	private EditText mTextField;
	private String TAG = this.getClass().getSimpleName();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
//			Intent enableBluetooth = new Intent(
//					BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(enableBluetooth, 0);
			return START_STICKY; 
		}

		Log.d(TAG, "Checking paired devices...");
		for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
			if (device.getName().equals(ARDUINODEVICENAME)) {
				Log.d(TAG, "Found " + ARDUINODEVICENAME);
				mDevice = device;
				break;
			}
		}

		try {
			Log.d(TAG, "Opening socket...");
			mSocket = mDevice.createRfcommSocketToServiceRecord(SPS_UUID);
			mSocket.connect();
			mOutputStream = mSocket.getOutputStream();
			mInputStream = mSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		mHandler = new Handler();
		mWorkerThread = new Thread(new Runnable() {
			private boolean stopWorker;

			public void run() {
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						int bytesAvailable = mInputStream.available();
						if (bytesAvailable > 0) {
							int received = mInputStream.read();
							Log.d(TAG, "Received " + (char) received);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mWorkerThread.start();

		mSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		return Service.START_STICKY;
	}

	public void sendStuff(byte[] bytes) {
		try {
			Log.d(TAG, "Writing " + mTextField.getText());
			mOutputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}