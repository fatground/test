package com.fatground.dadabreakfest;

import java.util.HashMap;
import java.util.Map;

import com.fatground.dadabreakfest.R;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IntentRegistPage extends Activity{
	
	private Button btn_regist;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intent_regist_page);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText userpassword = (EditText) findViewById(R.id.password);
		final EditText reuserpassword = (EditText) findViewById(R.id.repassword);
		btn_regist = (Button) findViewById(R.id.regist);
		btn_regist.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
				String name = username.getText().toString();
				String password = userpassword.getText().toString();
				String repassword = reuserpassword.getText().toString();
				if(password.length()<6||password.length()>12){
					Toast.makeText(IntentRegistPage.this, "���볤������", Toast.LENGTH_SHORT).show();
					btn_regist.setClickable(true);
				}else if(password.equals(repassword)){
					String url = "http://121.40.81.110:8080/tastebook/servlet/LoginServlet?action_flag=Regist&&"+
							"name=" + name +"&&password=" + password;
					btn_regist.setClickable(false);
					new PostRegistTask().execute(url);
				} else{
					Toast.makeText(IntentRegistPage.this, "��ȷ�������Ƿ�����", Toast.LENGTH_SHORT).show();
					Resources resource=(Resources)getBaseContext().getResources();
					ColorStateList csl=(ColorStateList)resource.getColorStateList(R.color.red); 
					reuserpassword.setTextColor(csl);
					btn_regist.setClickable(true);
				}
				
			}	
		});
	}
	
	private class PostRegistTask extends AsyncTask<String, Void, Map<String,String>> {

		@Override
		protected Map<String, String> doInBackground(String... params) {
			// TODO �Զ����ɵķ������
			Map<String,String> state = new HashMap<String,String>();
            String jsonStr = HttpUtils.sendPostMessage(params[0], "utf-8");
            //    �����������˵�json����
            state = JsonUtils.parseRegist(jsonStr);
			return state;
		}
		@Override
        protected void onPostExecute(Map<String, String> result)
        {
			String state = result.get("state");
			if(state.equals("SUCCESS")){
				Toast.makeText(IntentRegistPage.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
				finish();
			}else if(state.equals("ESISTED")){
				Toast.makeText(IntentRegistPage.this, "�˺��ѱ�ע��", Toast.LENGTH_SHORT).show();
				btn_regist.setClickable(true);
			}else if(state.equals("")){
				Toast.makeText(IntentRegistPage.this, "ע��ʧ�ܣ�����ϵ����Ա", Toast.LENGTH_SHORT).show();
				btn_regist.setClickable(true);
			}
        }
	}

}
