package com.example.map;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.entity.CrowdPoint;
import com.example.main.MainScreen;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends MapFragment
{
	private View rootView;
	private Inflater inflater;
	private LatLngBounds.Builder builder;
	private ArrayList<CrowdPoint> cpList;
	private MainScreen parent;
	
	public static MyMapFragment newInstance(MainScreen parent) {
		MyMapFragment fragment = new MyMapFragment();
		fragment.setParent(parent);
		return fragment;
	}
	
	public void setParent(MainScreen parent)
	{
		this.parent = parent;
	}
	
	public MyMapFragment() {

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
	
	public void addMarker(final ArrayList<CrowdPoint> cpList)
	{
		if (getMap() != null)
		{
			this.cpList = cpList;
			
			for (int n=0;n<this.cpList.size();n++)
			{
				CrowdPoint cp = cpList.get(n);
				Marker marker = getMap().addMarker(new MarkerOptions()
					.position(cp.getLatLng())
					.icon(BitmapDescriptorFactory.fromResource(com.example.cogrip_project.R.drawable.pin_map_normal)));
				
				marker.setTitle("0");
			}
			
			getMap().setOnMarkerClickListener(new OnMarkerClickListener()
			{
				@Override
				public boolean onMarkerClick(Marker arg0) {
					setMarkerInfo(cpList.get(Integer.valueOf(arg0.getTitle())));
					return true;
				}
			});
		}
	}
	
	private void setMarkerInfo(CrowdPoint cp)
	{
		parent.setMarkerInfo(cp);
	}
	
}
