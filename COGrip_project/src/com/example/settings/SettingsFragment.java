package com.example.settings;

import com.example.cogrip_project.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ToggleButton;

public class SettingsFragment extends Fragment implements OnClickListener
{
	ToggleButton bluetoothToggleButton;
	
	public SettingsFragment()
	{
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		//setThingsUp();
	    return inflater.inflate(R.layout.settings, container, false);
	}


	private void setThingsUp() 
	{
		bluetoothToggleButton = (ToggleButton) getView().findViewById(R.id.bluetoothToggleButton);
		bluetoothToggleButton.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if (v instanceof ToggleButton)
		{
			if ((ToggleButton)v == bluetoothToggleButton)
			{
				/*TODO
				 * Connect to arduino
				 */
			}
		}
	}

}
