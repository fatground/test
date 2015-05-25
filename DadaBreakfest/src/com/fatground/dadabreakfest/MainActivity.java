package com.fatground.dadabreakfest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fatground.dadabreakfest.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements FragmentSetting.Callbacks{
	private static final String TAG = "MainActivity";
	private Context context = this;
	private Fragment[] mFragments;
	private RadioGroup bottomRg;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioButton rbOne, rbTwo, rbThree, rbFour;
	private Button rfBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mFragments = new Fragment[4];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_item);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_setting);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]);
		fragmentTransaction.show(mFragments[0]).commit();
		setFragmentIndicator();
		getLoginState();
		rfBtn = (Button) findViewById(R.id.refresh);
		rfBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				new refreshUserInfoAsyncTask().execute();
			}
			
		});
//		startActivityForResult(null, 0);
	}

	private void setFragmentIndicator() {

		bottomRg = (RadioGroup) findViewById(R.id.bottomRg);
		rbOne = (RadioButton) findViewById(R.id.rbOne);
		rbTwo = (RadioButton) findViewById(R.id.rbTwo);

		bottomRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1]);
				switch (checkedId) {
				case R.id.rbOne:
					fragmentTransaction.show(mFragments[0]).commit();
					break;

				case R.id.rbTwo:
					fragmentTransaction.show(mFragments[1]).commit();
					break;

				default:
					break;
				}
			}
		});
	}
	@Override
	public void getUserState(String jsonString) {
		// TODO 自动生成的方法存根
		Bundle bundle = new Bundle();
		bundle.putBoolean("login", true);
		FragmentSetting fs = new FragmentSetting();
		fs.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_setting, fs).commit();
	}
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		Log.d(TAG, "onActivityforResult");
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			String stateString = (String) data.getExtras().get("login");
			if(null != stateString){
				getLoginState();
			}
		}
	}
	protected void getLoginState(){
//		Context otherAppsContext = null;
//		try {
//			otherAppsContext = createPackageContext("com.fatground.breakfast", Context.CONTEXT_IGNORE_SECURITY);
//		} catch (NameNotFoundException e) {
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
		SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		if(sharedPreferences.getAll().size() == 0){
		}else{
		String username = sharedPreferences.getString("name", "");
		TextView setting_title = (TextView) findViewById(R.id.titleText);
		setting_title.setText("注销");
		TextView name = (TextView) findViewById(R.id.name);
		name.setText(username);
		TextView my_orders_count = (TextView) findViewById(R.id.my_orders_count);
		String ordercount = sharedPreferences.getString("ordercount", "");
		my_orders_count.setText(ordercount);
		}
	}
	protected class refreshUserInfoAsyncTask extends AsyncTask<String, Void, Map<String,String>>{
        @Override
        protected void onPreExecute()
        {
        }

		@Override
		protected Map<String,String> doInBackground(String... arg0) {
			// TODO 自动生成的方法存根
			Map<String,String> result = new HashMap<String,String>();
			SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
			String url = "http://121.40.81.110:8080/tastebook/servlet/LoginServlet?action_flag=checkUserInfo&&" +
					"userid=" + sp.getString("userid", "") +
					"&&token=" + sp.getString("token", "");
			String jsonStr = HttpUtils.sendPostMessage(url, "utf-8");
			result = JsonUtils.parseUserInfo(jsonStr);
			return result;
		}

    	protected void onPostExecute(Map<String,String> result)
    	{
			SharedPreferences sp = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
    		TextView name = (TextView) findViewById(R.id.name);
    		TextView my_orders_count = (TextView) findViewById(R.id.my_orders_count);
    		name.setText(result.get("username"));
    		my_orders_count.setText(result.get("ordercount"));
    		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
    		Editor editor = sp.edit();
    		editor.putString("username", result.get("username")).commit();
    		editor.putString("ordercount", result.get("ordercount")).commit();
    	}
	}
	
}
