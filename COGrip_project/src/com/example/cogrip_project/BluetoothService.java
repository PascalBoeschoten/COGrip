package com.example.cogrip_project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
	private Thread mWorkerThread;

	private String TAG = this.getClass().getSimpleName();
	private AudioManager mAudioManager;
	
	public void abortStartup() {
		Toast.makeText(this, "BluetoothService: couldn't connect", Toast.LENGTH_SHORT).show();
		stopSelf();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Received start command");

		mAudioManager = (AudioManager) getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			abortStartup();
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

		if (mDevice == null) {
			Log.d(TAG, "Couldn't find paired device " + ARDUINODEVICENAME);
			abortStartup();
			return START_STICKY;
		}

		try {
			Log.d(TAG, "Opening socket...");
			mSocket = mDevice.createRfcommSocketToServiceRecord(SPS_UUID);
			mSocket.connect();
			mOutputStream = mSocket.getOutputStream();
			mInputStream = mSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			abortStartup();
			return START_STICKY;
		}

		mWorkerThread = new Thread(new Runnable() {
			private boolean stopWorker;

			public void run() {
				Log.d(TAG, "Starting to receive...");
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						int bytesAvailable = mInputStream.available();
						if (bytesAvailable > 0) {
							char received = (char) mInputStream.read();
							Log.d(TAG, "Received " + (char) received);
							handleVolumeChange(received);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					mSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mWorkerThread.start();
		Toast.makeText(this, "BluetoothService: connected", Toast.LENGTH_SHORT).show();
		
		return Service.START_STICKY;
	}

	protected void handleVolumeChange(char volumeChar) {
		int volume;
		try {
			volume = Integer.parseInt(String.valueOf(volumeChar));
		} catch (NumberFormatException e) {
			Log.d(TAG, "Received unknown volume level char '" + volumeChar
					+ "'");
			return;
		}

		int musicMaxVol = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int index = musicMaxVol / 4 * volume;

		Log.d(TAG, "Setting music to volume " + index + "/" + musicMaxVol);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
	}

	public void sendStuff(byte[] bytes) {
		try {
			String s = "";
			for (byte b : bytes) {
				s += b;
			}
			Log.d(TAG, "Writing " + s);
			mOutputStream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "I am being destroyed");
		if (mWorkerThread != null) {
			mWorkerThread.interrupt();
		}
	}

	public static void launchService(Context context) {
		Log.d("", "Launching BluetoothService");
		Intent i = new Intent(context, BluetoothService.class);
		context.startService(i);
	}

	public static void stopService(Context context) {
		Log.d("", "Stopping BluetoothService");
		Intent i = new Intent(context, BluetoothService.class);
		context.stopService(i);
	}

	public static boolean isRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (BluetoothService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}