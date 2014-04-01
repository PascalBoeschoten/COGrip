package com.example.entity;

import com.google.android.gms.maps.model.LatLng;

public class CrowdPoint 
{
	LatLng latlng;
	int crowdness;
	
	public CrowdPoint(LatLng latlng, int crowdness)
	{
		this.latlng 	= latlng;
		this.crowdness 	= crowdness;
	}
}
