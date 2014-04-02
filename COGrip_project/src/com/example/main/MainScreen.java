package com.example.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.*;
import com.example.cogrip_project.R;

public class MainScreen extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 setContentView(R.layout.activity_main_screen);
	}

	
}
