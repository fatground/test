package com.fatground.dadabreakfest;


import com.fatground.dadabreakfest.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentSetting extends Fragment {
	private TextView setting_title;
	private TextView name;
	private Button change;
	private LinearLayout my_orders;
	private TextView my_orders_count;
	private String Screen_name;
	private FragmentSetting fragmentSetting = this;
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setting_title = (TextView) getView().findViewById(R.id.titleText);
		setting_title.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
//				Toast.makeText(context, "You have selected" + listItems.get(position-1).get("title"), Toast.LENGTH_SHORT).show();
				Intent intent =new Intent(getActivity(),IntentLoginPage.class);

				startActivityForResult(intent, 0);
//				startActivity(intent);// 启动新的 Activity 
			}
		});
		my_orders = (LinearLayout) getView().findViewById(R.id.my_orders);
		my_orders.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent =new Intent(getActivity(),IntentListOrderPage.class);

				startActivityForResult(intent, 0);
			}
			
		});
		
	}
	private Callbacks mCallbacks = sDummyCallbacks;
	public interface Callbacks{
		void getUserState(String id);
	}
	private static Callbacks sDummyCallbacks = new Callbacks(){
		public void getUserState(String id){
			
		}
	};
	public void onAttach(Activity activity){
		super.onAttach(activity);
		if(!(activity instanceof Callbacks)){
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}
		mCallbacks = (Callbacks) activity;
		Bundle arguments = fragmentSetting.getArguments();
		//感觉条件多余，注意日后修改！
		if(arguments != null) {
			Context otherAppsContext = null;
			try {
				otherAppsContext = getActivity().createPackageContext("com.fatground.test", Context.CONTEXT_IGNORE_SECURITY);
			} catch (NameNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			fragmentSetting.onDestroy();
			SharedPreferences sharedPreferences = otherAppsContext.getSharedPreferences("logindata", Context.MODE_PRIVATE);
			String username = sharedPreferences.getString("username", "");
			String ordercount = sharedPreferences.getString("ordercount", "");
			setting_title = (TextView) getView().findViewById(R.id.titleText);
			setting_title.setText("注销");
			name = (TextView) getView().findViewById(R.id.name);
			name.setText(username);
			my_orders_count = (TextView) getView().findViewById(R.id.my_orders_count);
			my_orders_count.setText(ordercount);
//			Screen_name = (String) getResources().getString(R.string.screen_name);
//			Screen_name.
		} 
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		mCallbacks = sDummyCallbacks;
	}
}
