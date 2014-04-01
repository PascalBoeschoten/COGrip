package com.example.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cogrip_project.*;

public class MyArrayAdapter extends ArrayAdapter<String>{

	private final Context context;
	private final String[] values;

	public MyArrayAdapter(Context context, String[] values)
	{
		super(context, R.layout.listview_rowlayout, values);
		this.context = context;
		this.values = values;
  }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.listview_rowlayout, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.listview_label);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.listview_icon);
	    textView.setText(values[position]);
	    
	    imageView.setImageResource(R.drawable.ic_launcher);
	    
	    //Sätt ikonen beroende på vilken text som visas
	    String s = values[position];
	    /*
	    if (s.equals("Webbshop"))
	    {
	    	imageView.setImageResource(R.drawable.kundvagn);
	    } 
	    else if(s.equals("Skapa ärende"))
	    {
	    	imageView.setImageResource(R.drawable.folderplus);
	    }
	    else if(s.equals("Visa ärenden"))
	    {
	    	imageView.setImageResource(R.drawable.folder);
	    }
	    else if(s.equals("Admin"))
	    {
	    	imageView.setImageResource(R.drawable.adminicon);
	    }
	    */
	    return rowView;
	}
	
	
}
