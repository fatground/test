package com.fatground.dadabreakfest;

public class LoginState{
		private String loginstate;
		private String token;
		private String userinfo;
		public LoginState(){
		}
		public void setLoginState(String loginstate){
			this.loginstate = loginstate;
		}
		public String getLoginState(){
			return this.loginstate;
		}
		public void setToken(String token){
			this.token = token;
		}
		public String getToken(){
			return this.token;
		}
		public String getUserinfo() {
			return userinfo;
		}
		public void setUserinfo(String userinfo) {
			this.userinfo = userinfo;
		}
	}
