package com.example.cogrip_project;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BluetoothActivity extends Activity {
	private String TAG = getClass().getSimpleName();

	private Button mRequestButton;
	private Button mStopButton;
	private Button mStartButton;
	private TextView mConnectionTextView;
	
	public static void enableBluetoothDialog(Context context) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    context.startActivity(enableBtIntent);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		enableBluetoothDialog(this);

		mStartButton = (Button) findViewById(R.id.StartButton);
		mStopButton = (Button) findViewById(R.id.StopButton);
		mRequestButton = (Button) findViewById(R.id.RequestStateButton);
		mConnectionTextView = (TextView) findViewById(R.id.ConnectedTextView);
		
		BluetoothService.launchService(this);

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
				boolean isRunning = BluetoothService.isRunning(context);
				mConnectionTextView.setText("Connected: "+ (isRunning ? "yes" : "no"));
			}
		});
	}
}
