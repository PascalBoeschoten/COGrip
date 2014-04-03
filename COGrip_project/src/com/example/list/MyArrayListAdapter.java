package com.example.list;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.cogrip_project.*;
import com.example.entity.CrowdPoint;


public class MyArrayListAdapter extends ArrayAdapter<CrowdPoint>
{
	private Activity activity;
	private final ArrayList<CrowdPoint> cpList;
	private View row;
	private ViewHolder view;
	
	public MyArrayListAdapter(Context context, int textViewResourceId, ArrayList<CrowdPoint> cpList) {
		super(context, textViewResourceId, cpList);
		this.activity = (Activity) context;
		this.cpList = cpList;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    row = convertView;
	    if(row == null)
        {
            // Gets the layout for the errand row
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate( R.layout.listview_rowlayout, null );

            // Holds all of our views in a single container, which improves performance
            view = new ViewHolder();
            view.zipView = (TextView) row.findViewById(R.id.listViewZip);
            view.dateView = (TextView) row.findViewById(R.id.listViewDate);
    	    view.streetView = (TextView) row.findViewById(R.id.listViewStreet);
            
            row.setTag(view);
            
        } else {
            view = (ViewHolder) row.getTag();
        }
	    
	    CrowdPoint cp = cpList.get(position);
	    view.dateView.setText(cp.getDate());
	    view.zipView.setText(cp.getZip());
	    view.streetView.setText(cp.getStreet());	    
	    
	    return row;
	}
	
	protected static class ViewHolder {
		// All views in the errand row item
		TextView zipView;
	    TextView dateView;
	    TextView streetView;
	}
	
}
