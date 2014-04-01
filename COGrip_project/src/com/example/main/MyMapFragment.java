package com.example.main;

import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.entity.CrowdPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.miun.entity.Task;

public class MyMapFragment extends MapFragment
{
	private View rootView;
	private Inflater inflater;
	private LatLngBounds.Builder builder;
	private List crowdedList;
	
	public static MyMapFragment newInstance(List<CrowdPoint> crowdedList) {
		MyMapFragment fragment = new MyMapFragment();
		fragment.setCrowdedList(crowdedList);
		return fragment;
	}
	
	public void setCrowdedList(List<CrowdPoint> crowdedList)
	{
		this.crowdedList = crowdedList;
	}
	
	//TODO: fix
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		//View rootView = inflater.inflate(R.layout.fragment_navigate_map, container, false);
		rootView = super.onCreateView(inflater, container, savedInstanceState);
		
		inflater = inflater;
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initMap();
	}

	private void initMap()
	{
	    UiSettings settings = getMap().getUiSettings();
	    settings.setAllGesturesEnabled(true);
	    settings.setMyLocationButtonEnabled(true);
	    settings.setZoomControlsEnabled(true);
	    getMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	    
	    getMap().setOnCameraChangeListener(new OnCameraChangeListener() 
	    {
			@Override
			public void onCameraChange(CameraPosition arg0)
			{
				builder = new LatLngBounds.Builder();
			    builder.include(new LatLng(52.379992, 4.870763));
			    builder.include(new LatLng(52.357458, 4.925265));
				// Move camera.
	            getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 10));
	            // Remove listener to prevent position reset on camera move.
	            getMap().setOnCameraChangeListener(null);
			}
	    });
	}
}
