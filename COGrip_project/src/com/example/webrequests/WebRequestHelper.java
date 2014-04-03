package com.example.webrequests;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.entity.CrowdPoint;
import com.example.list.ListWrapper;
import com.example.map.MyMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;

public class WebRequestHelper 
{
	public static MyMapFragment myMapFragment;
	public static ListWrapper listWrapper;
	public static Context ctx;
	public static void getNearbyAreasForMap(MyMapFragment myMapFragmentArg, Context ctxArg)
	{
		myMapFragment = myMapFragmentArg;
		ctx = ctxArg;
		WebRequests.getNearbyCrowdedAreas(ctxArg, new LatLng(123, 324), new JsonHttpResponseHandler()
    	{

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) 
			{
				Log.d("Mada", "success");
				JSONArray jArray = response;
				
				ArrayList<CrowdPoint> cpList = new ArrayList<CrowdPoint>();
				
				for (int n=0;n<jArray.length();n++)
				{
					try
					{
						JSONObject jObj = jArray.getJSONObject(n);
						Log.d("Mada", "Lat,Lng: " + jObj.get("Lat") + ", " + jObj.get("Lng"));
						
						double lat 		= Double.valueOf(jObj.get("Lat").toString());
						double lng 		= Double.valueOf(jObj.get("Lng").toString());
						LatLng latlng 	= new LatLng(lat, lng);
//						String street	= jObj.get("Street").toString();
//						String zip		= jObj.get("Zip").toString();
//						String date		= jObj.get("Date").toString();
//						int crowdness 	= Integer.valueOf(jObj.get("crowdness").toString());
						
						String street="a", zip="a", date = "a";
						int crowdness = 0;
						CrowdPoint cp = new CrowdPoint(latlng, street, zip, date, crowdness);
						cpList.add(cp);
					} 
					catch (JSONException e) 
					{
						e.printStackTrace();
					}
				}
				
				myMapFragment.addMarker(cpList);
				
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				// TODO Auto-generated method stub
				Log.d("Mada", "fail0");
				Toast.makeText(ctx, "Bad internet connection", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseBody, e);
			}
			
			
    		
    	});
	}
	
	//FOR LIST ------------------------------
	public static void getNearbyAreasForList(ListWrapper listWrapperArg, Context ctxArg)
	{
		listWrapper = listWrapperArg;
		ctx = ctxArg;
		WebRequests.getNearbyCrowdedAreas(ctxArg, new LatLng(123, 324), new JsonHttpResponseHandler()
    	{

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) 
			{
				Log.d("Mada", "success");
				JSONArray jArray = response;
				
				ArrayList<CrowdPoint> cpList = new ArrayList<CrowdPoint>();
				
				for (int n=0;n<jArray.length();n++)
				{
					try
					{
						JSONObject jObj = jArray.getJSONObject(n);
						Log.d("Mada", "Lat,Lng: " + jObj.get("Lat") + ", " + jObj.get("Lng"));
						
						double lat 		= Double.valueOf(jObj.get("Lat").toString());
						double lng 		= Double.valueOf(jObj.get("Lng").toString());
						LatLng latlng 	= new LatLng(lat, lng);
//						String street	= jObj.get("Street").toString();
//						String zip		= jObj.get("Zip").toString();
//						String date		= jObj.get("Date").toString();
//						int crowdness 	= Integer.valueOf(jObj.get("crowdness").toString());
						
						String street="a", zip="a", date = "a";
						int crowdness = 0;
						CrowdPoint cp = new CrowdPoint(latlng, street, zip, date, crowdness);
						cpList.add(cp);
					} 
					catch (JSONException e) 
					{
						e.printStackTrace();
					}
				}
				
				listWrapper.setupListAfterResponse(cpList);
				
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				// TODO Auto-generated method stub
				Log.d("Mada", "fail0");
				Toast.makeText(ctx, "Bad internet connection", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseBody, e);
			}
			
			
    		
    	});
	}
	
	public static void sendSensorData(CrowdPoint cp, Context ctxArg)
	{
		ctx = ctxArg;
		
		Log.d("Mada", "Sending sensor data");
		WebRequests.uploadSensorData(ctxArg, cp, new JsonHttpResponseHandler()
    	{

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseBody)
			{
				Log.d("Mada", "success");
				super.onSuccess(statusCode, headers, responseBody);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) 
			{
				Log.d("Mada", "success");
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				// TODO Auto-generated method stub
				Log.d("Mada", "fail0");
				Toast.makeText(ctx, "Bad internet connection", Toast.LENGTH_SHORT).show();
				super.onFailure(statusCode, headers, responseBody, e);
			}
			
			
    		
    	});
	}
}
