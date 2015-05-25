package com.fatground.dadabreakfest;

import com.fatground.dadabreakfest.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start);
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
				StartActivity.this.finish();
			}
			
		},700);
	}

}
