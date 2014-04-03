package com.example.list;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.cogrip_project.R;
import com.example.entity.CrowdPoint;
import com.example.webrequests.WebRequestHelper;
import com.google.android.gms.maps.model.LatLng;

public class ListWrapper extends Fragment implements OnItemClickListener{
	ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_list_wrapper, container, false);
		
		listView = (ListView) rootView.findViewById(R.id.listView);
		WebRequestHelper.getNearbyAreasForList(this, getActivity().getApplicationContext());
	    return rootView;
	}
	
	public void setupListAfterResponse(ArrayList<CrowdPoint> cpList)
	{
		if (listView != null)
		{
			MyArrayListAdapter listAdapter = new MyArrayListAdapter( (getActivity()), 0, cpList);
			listView.setAdapter( listAdapter );
			listView.setOnItemClickListener(this);
			listView.setFastScrollEnabled(true);
			listView.setSmoothScrollbarEnabled(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Log.d("Mada", "item clicked");
		CrowdPoint cp = new CrowdPoint(new LatLng(52.444,33.333), "what", "the", "fuck", 5);
		WebRequestHelper.sendSensorData(cp, getActivity().getApplicationContext());
	}
}
