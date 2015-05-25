package com.fatground.dadabreakfest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fatground.dadabreakfest.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IntentCommentPage extends Activity{
	public Context context = this;
	public Map<String,String> map;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intent_comment_page);
		final EditText editcomment = (EditText) findViewById(R.id.commentstext);
		Button confirm = (Button) findViewById(R.id.confirm_comment);
		Bundle bundle=this.getIntent().getBundleExtra("key");
		final String host = bundle.getString("id");
		confirm.setOnClickListener(new OnClickListener(){
			//提交评论
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				String comment = editcomment.getText().toString();
				Context otherAppsContext = null;
				try {
					otherAppsContext = createPackageContext("com.fatground.test", Context.CONTEXT_IGNORE_SECURITY);
				} catch (NameNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				SharedPreferences sharedPreferences = otherAppsContext.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
				if(sharedPreferences.getAll().size() == 0){
					Toast.makeText(context, "未登陆", Toast.LENGTH_LONG).show();
				}else{
					String userid = sharedPreferences.getString("userid", "");
					String token = sharedPreferences.getString("token", "");
					String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=putcomments&&comment="
									+ comment + "&&host=" + host + "&&userid=" + userid + "&&token=" + token;
					url = replaceBlank(url);
					new PostCommentTask().execute(url);
				}
			}
			
		});
		
	}
	public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
	//后台根据token跟uid上传评论
	private class PostCommentTask extends AsyncTask<String, Void, Map<String,String>> {
        @Override
        protected void onPreExecute()
        {
        }

		@Override
		protected Map<String, String> doInBackground(String... params) {
			// TODO 自动生成的方法存根
			Map<String,String> result = new HashMap<String,String>();
			String jsonStr = HttpUtils.sendPostMessage(params[0], "utf-8");
			result = JsonUtils.parseComment(jsonStr);
			return result;
		}
        @Override
        protected void onPostExecute(Map<String,String> result)
        {
        	if(result.get("state").equals("SUCCESS")){
        		Toast.makeText(context, "发表成功", Toast.LENGTH_SHORT).show();
        		finish();
        	}else{
        		Toast.makeText(context, "账号登陆无效，请重新登陆！", Toast.LENGTH_SHORT).show();
        		finish();
        	}
        }
		
	}

}
