package com.fatground.dadabreakfest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class UserInfo extends Activity{
	
	private String username;
	private String userid;
	private int gender;
	private String token;
	private String address;
	private int phone;
	private int grade;
	private String mailadd;
	private int type;
	
	public UserInfo(){
		Context otherAppsContext = null;
		try {
			otherAppsContext = createPackageContext("com.fatground.breakfast", Context.CONTEXT_IGNORE_SECURITY);
		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		SharedPreferences sharedPreferences = otherAppsContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		this.setToken(sharedPreferences.getString("token", ""));
		this.setUserid(sharedPreferences.getString("userid", ""));
		this.setUsername(sharedPreferences.getString("username", ""));
	}
	
	public void getSP(){
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getMailadd() {
		return mailadd;
	}

	public void setMailadd(String mailadd) {
		this.mailadd = mailadd;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}
}
