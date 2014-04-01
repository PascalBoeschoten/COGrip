package com.example.cogrip_project;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.main.MainWrapper;
import com.example.walkthrough.FirstPageFragment;
import com.example.walkthrough.SecondPageFragment;
import com.example.walkthrough.ThirdPageFragment;

public class Walkthrough_activity extends ActionBarActivity implements OnCheckedChangeListener, OnClickListener 
{
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	RadioGroup 	walkthroughRadioGroup;
	RadioButton walkthroughRadio0;
	RadioButton walkthroughRadio1;
	RadioButton walkthroughRadio2;
	TextView 	skipTextBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_walkthrough_activity);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		setThingsUp();
	}

	private void setThingsUp()
	{
		walkthroughRadioGroup 	= (RadioGroup)	findViewById(R.id.walkthroughRadioGroup);
		walkthroughRadio0 		= (RadioButton) findViewById(R.id.walkthroughRadio0);
		walkthroughRadio1 		= (RadioButton) findViewById(R.id.walkthroughRadio1);
		walkthroughRadio2 		= (RadioButton) findViewById(R.id.walkthroughRadio2);
		walkthroughRadio0.setChecked(true);
		walkthroughRadioGroup.setOnCheckedChangeListener(this); 
		
		skipTextBtn 			= (TextView)	findViewById(R.id.skipTextBtn);
		skipTextBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.walkthrough_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter 
	{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) 
		{
			switch(position)
			{
				case 0:
					Toast.makeText(getApplicationContext(), "rb0", Toast.LENGTH_SHORT).show();
					walkthroughRadio0.setChecked(true);
					return new FirstPageFragment();
				case 1:
					Toast.makeText(getApplicationContext(), "rb1", Toast.LENGTH_SHORT).show();
					walkthroughRadio1.setChecked(true);
					return new SecondPageFragment();
				case 2:
					Toast.makeText(getApplicationContext(), "rb2", Toast.LENGTH_SHORT).show();
					walkthroughRadio2.setChecked(true);
					return new ThirdPageFragment();
			}
			
			return null;
		}

		@Override
		public int getCount() 
		{
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
        switch(checkedId) 
        {
              case R.id.walkthroughRadio0:
            	  mSectionsPagerAdapter.getItem(0);
                   break;
              case R.id.walkthroughRadio1:
            	  mSectionsPagerAdapter.getItem(1);
                  break;
              case R.id.walkthroughRadio2:
            	  mSectionsPagerAdapter.getItem(2);
                  break;
        }   
	}

	@Override
	public void onClick(View v) {
		if (((TextView)v) == skipTextBtn)
		{
			Intent i = new Intent(this, MainWrapper.class);
			startActivity(i);
		}
	}
}
