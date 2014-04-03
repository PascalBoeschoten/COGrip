package com.example.cogrip_project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BluetoothActivity extends Activity {
	private String TAG = getClass().getSimpleName();

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					BluetoothService.STATE_ACTION)) {
				mConnectionTextView
						.setText("Connected: "
								+ intent.getStringExtra(BluetoothService.CONNECTED_EXTRA_KEY));
			} else {
				Log.d(TAG, "Received unknown intent " + intent);
			}
		}
	};

	private Button mRequestButton;
	private Button mStopButton;
	private Button mStartButton;
	private TextView mConnectionTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mStartButton = (Button) findViewById(R.id.StartButton);
		mStopButton = (Button) findViewById(R.id.StopButton);
		mRequestButton = (Button) findViewById(R.id.RequestStateButton);
		mConnectionTextView = (TextView) findViewById(R.id.ConnectedTextView);

		BluetoothService.launchService(this);

		LocalBroadcastManager
				.getInstance(this)
				.registerReceiver(
						mReceiver,
						new IntentFilter(
								BluetoothService.STATE_ACTION));
		
		final Context context = this;
		mStartButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BluetoothService.launchService(context);
			}
		});
		mStopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BluetoothService.stopService(context);
			}
		});
		mRequestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BluetoothService.requestState(context);
			}
		});
	}
}
