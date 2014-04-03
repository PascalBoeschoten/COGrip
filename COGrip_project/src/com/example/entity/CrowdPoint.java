package com.example.entity;

import com.google.android.gms.maps.model.LatLng;

public class CrowdPoint 
{
	LatLng latlng;
	String street;
	String zip;
	String date;
	int crowdness;
	
	public CrowdPoint(LatLng latlng, String street, String zip, String date, int crowdness)
	{
		this.latlng 	= latlng;
		this.street		= street;
		this.zip		= zip;
		this.date		= date;
		this.crowdness 	= crowdness;
	}
	
	public LatLng getLatLng()
	{
		return latlng;
	}
	
	public String getStreet()
	{
		return street;
	}
	
	public String getZip()
	{
		return zip;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public int getCrowdness()
	{
		return crowdness;
	}
}
