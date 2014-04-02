package com.example.map;

import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.cogrip_project.*;
import com.example.utils.constants;
import com.example.webrequests.WebRequests;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.example.entity.CrowdPoint;

public class MainWrapper extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    
    //Map
    MyMapFragment myMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wrapper);

        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = constants.MENU_TITLES;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new MyArrayAdapter(this, constants.MENU_TITLES));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
        {
        	if (position == 1)
        	{
    	    	//Loads a map into the fragment
        		myMapFragment = MyMapFragment.newInstance();
    	        FragmentManager fragmentManager = getFragmentManager();
    	        fragmentManager.beginTransaction().replace(R.id.content_frame, myMapFragment).commit();
    	        
    	        getNearbyCrowdedAreas();
        	}
        	
        	mDrawerList.setItemChecked(position, true);
            setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        	
        	
        }
    }
    
    public void getNearbyCrowdedAreas()
    {
    	Log.d("Mada", "Skickar nu");
    	WebRequests.getNearbyCrowdedAreas(getApplicationContext(), new LatLng(123, 324), new JsonHttpResponseHandler()
    	{

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) 
			{
				Log.d("Mada", "success");
				JSONArray jArray = response;
				
				for (int n=0;n<jArray.length();n++)
				{
					try
					{
						JSONObject jObj = jArray.getJSONObject(n);
						Log.d("Mada", "Lat,Lng: " + jObj.get("Lat") + ", " + jObj.get("Lng"));
						
						double lat 		= Double.valueOf(jObj.get("Lat").toString());
						double lng 		= Double.valueOf(jObj.get("Lng").toString());
						int crowdness 	= Integer.valueOf(jObj.get("crowdness").toString());
						LatLng latlng 	= new LatLng(lat, lng);
						
						myMapFragment.addMarker(latlng, 4);
					} 
					catch (JSONException e) 
					{
						e.printStackTrace();
					}
				}
				
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable e) {
				// TODO Auto-generated method stub
				Log.d("Mada", "fail0");
				super.onFailure(statusCode, headers, responseBody, e);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable e, JSONArray errorResponse) {
				Log.d("Mada", "fail1");
				super.onFailure(statusCode, headers, e, errorResponse);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable e, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				Log.d("Mada", "fail2");
				super.onFailure(statusCode, headers, e, errorResponse);
			}
			
			
    		
    	});
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        /*
    	Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
		*/
        
        // update selected item and title, then close the drawer
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	protected void onResume()
	{
		super.onResume();
	}    
}