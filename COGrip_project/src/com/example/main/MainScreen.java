package com.example.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cogrip_project.R;
import com.example.entity.CrowdPoint;
import com.example.list.ListWrapper;
import com.example.map.MyMapFragment;
import com.example.settings.SettingsFragment;
import com.example.webrequests.WebRequestHelper;

public class MainScreen extends Activity{
	ImageView feedImageView;
	ImageView mapImageView;
	ImageView settingsImageView;
	
	TextView markerStreet;
	TextView markerZip;
	TextView markerDate;
	
	RelativeLayout mainDescriptionContainer;
	RelativeLayout mainMarkerInfoContainer;
	MyMapFragment myMapFragment;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.activity_main_screen);
		 
		 setThingsUp();
	}


	private void setThingsUp() 
	{
		//Menu, images
		mainDescriptionContainer = (RelativeLayout) findViewById(R.id.mainDescriptionContainer);
		mainMarkerInfoContainer = (RelativeLayout) findViewById(R.id.mainMarkerInfoContainer);
		feedImageView = (ImageView) findViewById(R.id.feedImageView);
		feedImageView.setId(0);
		mapImageView = (ImageView) findViewById(R.id.mapImageView);
		mapImageView.setId(0);
		settingsImageView = (ImageView) findViewById(R.id.settingsImageView);
		settingsImageView.setId(0);
		addMenuOnClickListeners();
		mapImageView.performClick();
		
		//MarkerInfo
		markerStreet 	= (TextView) findViewById(R.id.markerStreet);
		markerZip		= (TextView) findViewById(R.id.markerZip);
		markerDate		= (TextView) findViewById(R.id.markerDate);
	}


	private void addMenuOnClickListeners()
	{
		feedImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (feedImageView.getId() == 0)
				{
					//Change Images
					feedImageView.setImageResource(R.drawable.feed_activated);
					feedImageView.setId(1);
					mapImageView.setImageResource(R.drawable.map_nonactive);
					mapImageView.setId(0);
					settingsImageView.setImageResource(R.drawable.settings_nonactive);
					settingsImageView.setId(0);
					
					mainDescriptionContainer.setBackgroundResource(R.drawable.title_activity);
					mainMarkerInfoContainer.setVisibility(RelativeLayout.GONE);
					switchToListView();
				}
			}
		});
		
		mapImageView.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (mapImageView.getId() == 0)
				{
					//Change Images
					mapImageView.setImageResource(R.drawable.map_activated);
					mapImageView.setId(1);
					feedImageView.setImageResource(R.drawable.feed_nonactive);
					feedImageView.setId(0);
					settingsImageView.setImageResource(R.drawable.settings_nonactive);
					settingsImageView.setId(0);
					
					mainDescriptionContainer.setBackgroundResource(R.drawable.title_map);
					mainMarkerInfoContainer.setVisibility(RelativeLayout.VISIBLE);
					
					switchToMapView();
				}
			}
		});
		
		settingsImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (settingsImageView.getId() == 0)
				{
					//Change Images
					settingsImageView.setImageResource(R.drawable.settings_active);
					settingsImageView.setId(1);
					feedImageView.setImageResource(R.drawable.feed_nonactive);
					feedImageView.setId(0);
					mapImageView.setImageResource(R.drawable.map_nonactive);
					mapImageView.setId(0);
					
					mainDescriptionContainer.setBackgroundResource(R.drawable.title_settings);
					mainMarkerInfoContainer.setVisibility(RelativeLayout.GONE);
					switchToSettingsView();
				}
			}
		});
	}
	
	private void switchToMapView() {
		myMapFragment = MyMapFragment.newInstance(this);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainScreenFragment, myMapFragment).commit();
        
        WebRequestHelper.getNearbyAreasForMap(myMapFragment, getApplicationContext());
	}
	
	private void switchToSettingsView()
	{
		SettingsFragment settingsFragment = new SettingsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainScreenFragment, settingsFragment).commit();
	}
	
	public void switchToListView()
	{
		ListWrapper listWrapper = new ListWrapper();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainScreenFragment, listWrapper).commit();
	}
	
	public void setMarkerInfo(CrowdPoint cp)
	{
		markerStreet.setText("BlaBlaBla");
		markerZip.setText("BLABLA");
		markerDate.setText("BLA");
	}
}
