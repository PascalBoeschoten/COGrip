package com.example.webrequests;
import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebRequests
{
	private static final String BASE_URL 			=		"http://vasternorrlanuf.se/jakob/amsterdam/websrv.php?";
	private static final String uploadSensorDataURL = 		"uploadSensorData";
	private static final String getSensorDataURL 	= 		"getSensorData";
	private static final String WEBSHOP_ITEMS_URL 	= 		".product/getAll";
	private static final String UPLOAD_PRODUCT_URL	=		".product/create";
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	    client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	    client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
	    return BASE_URL + relativeUrl;
	}
	
	//------------METHODS-----------------------------------------
	public static void uploadSensorData(Context context, LatLng latlng, int crowdness, AsyncHttpResponseHandler handler)
	{
		RequestParams params = new RequestParams();
		params.put("Lat", String.valueOf(latlng.latitude));
		params.put("Lng", String.valueOf(latlng.longitude));
		params.put("Crowdness", String.valueOf(crowdness));
		get(uploadSensorDataURL, params, handler);
	}
	
	public static void getSensorData(Context context, LatLng latlng, JsonHttpResponseHandler handler)
	{
		RequestParams params = new RequestParams();
		params.put("Lat", latlng.latitude);
		params.put("Lng", latlng.longitude);
		get(getSensorDataURL, params, handler);
	}
	
}
