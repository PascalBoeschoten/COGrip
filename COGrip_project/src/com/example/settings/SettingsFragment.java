package com.example.settings;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.example.cogrip_project.BluetoothService;
import com.example.cogrip_project.R;

public class SettingsFragment extends Fragment {
	protected static final String TAG = "SettingsFragment";
	ToggleButton mBluetoothToggleButton;

	public SettingsFragment() {

	}
	
	public static void enableBluetoothDialog(Context context) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (!adapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    context.startActivity(enableBtIntent);
		}
	}

	@Override	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.settings, container, false);
		
		mBluetoothToggleButton = (ToggleButton) rootView.findViewById(
				R.id.bluetoothToggleButton);
		
		mBluetoothToggleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBluetoothToggleButton.isChecked()) {
					Log.d(TAG, "Starting Bluetooth");
					BluetoothService.launchService(getActivity());
				} else {
					Log.d(TAG, "Stopping Bluetooth");
					BluetoothService.stopService(getActivity());
				}
			}
		});
		
		enableBluetoothDialog(getActivity());
		
		return rootView;
	}

}
