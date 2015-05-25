package com.fatground.dadabreakfest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.fatground.dadabreakfest.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IntentLoginPage extends Activity {

	//��������ר��
	private Context context = this;
	private SharedPreferences sp;
	private Editor editor;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		sp = this.getSharedPreferences("userinfo", MODE_PRIVATE);
		editor = sp.edit();
		setContentView(R.layout.intent_login_page);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText userpassword = (EditText) findViewById(R.id.password);
		final TextView toregister = (TextView) findViewById(R.id.txt_toregister);
		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				String name = username.getText().toString();
				String password = userpassword.getText().toString();
				String brand = PhoneState.getBrand();
				//�ж��˺������Ƿ�Ϊ��
				if(name.trim().equals("")||password.trim().equals("")){
					Toast.makeText(IntentLoginPage.this, "�û������벻��Ϊ��", Toast.LENGTH_LONG).show();
					return;
				}
				String post = "http://121.40.81.110:8080/tastebook/servlet/LoginServlet?action_flag=Login&&"
				+"name="+ name
				+"&&password=" + password
				+"&&type=android"
				+"&&brand=" + brand
				+"&&model=" + PhoneState.getModel()
				+"&&os=" + PhoneState.getOs();
				post = post.replace(" ", "%20");
				 //sharedpreferences�Ļ������� �Լ�ֵ�Ե���ʽ���浽data�ļ���
				Editor editor = sp.edit();
				editor.clear();
				editor.putString("name", name);
				editor.putBoolean("first", false);
				//�ύ���浽���ɵ�XML�ļ���
				editor.commit();
				new LoginAsyncTask().execute(post);
			}
		});
		toregister.setOnClickListener(new  OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(context,IntentRegistPage.class);
				startActivity(intent);
			}
			
		});
	}
	public class LoginAsyncTask extends AsyncTask<String, Void, LoginState>{
        @Override
        protected void onPreExecute()
        {
        }

		@Override
		protected LoginState doInBackground(String... params) {
			// TODO �Զ����ɵķ������
        	LoginState loginState = new LoginState();
            String jsonStr = HttpUtils.sendPostMessage(params[0], "utf-8");
            //    �����������˵�json����
            loginState = JsonUtils.parseLogin(jsonStr);
            return loginState;
		}

        @Override
        protected void onPostExecute(LoginState result)
        {
        	String loginstate = result.getLoginState();
        	if(loginstate.equals("LOGIN_SUCCESSFULLY")){
				Toast.makeText(IntentLoginPage.this, "��½�ɹ���", 3000).show();
				Map<String,String> userinfo = new HashMap<String,String>();
				userinfo = JsonUtils.parseUserInfo(result.getUserinfo());
				editor.putString("loginstate", loginstate).commit();
				editor.putString("token", result.getToken()).commit();
				editor.putString("username", userinfo.get("username")).commit();
				editor.putString("userid", userinfo.get("userid")).commit();
				editor.putString("ordercount", userinfo.get("ordercount")).commit();
				Bundle bundle = new Bundle();
				bundle.putString("login", "true");
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(RESULT_OK,intent);
				finish();
        	}else{
				Toast.makeText(IntentLoginPage.this, "�û����������", 3000).show();
        	}
        }
	}


}