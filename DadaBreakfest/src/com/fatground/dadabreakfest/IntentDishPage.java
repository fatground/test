package com.fatground.dadabreakfest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fatground.dadabreakfest.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class IntentDishPage extends FragmentActivity{
	private static final String TAG = "IntentDishPage";
	private SimpleAdapter adapter;
	private IntentDishPage intentResPage = this;
	private Context context = this;
	private static String host = "";
	private SharedPreferences sharedPreferences;
	private Bundle bundle;
	private DisplayImageOptions options;
	private Fragment[] mFragments;
	private RadioGroup commentsRg;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	private RadioButton rbOne, rbTwo, rbThree;
	private List<Map<String,Object>> listItems;

	
	Intent intent =getIntent();// 收取 email 
	protected void onCreate(Bundle savedInstanceState) {
		bundle=this.getIntent().getBundleExtra("key");// 打开 email 
		bundle.getBoolean("boolean_key");
		sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		String fromFragmentItem = bundle.getString("fromFragmentItem");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intent_dish_page_info);
		host = bundle.getString("host");
		String id =(String) bundle.get("id");
//		ImageView imageView = (ImageView) findViewById(R.id.imageview);
		TextView title = (TextView) findViewById(R.id.title);
		TextView region = (TextView) findViewById(R.id.region);
		TextView price = (TextView) findViewById(R.id.price);
//		TextView summary = (TextView) findViewById(R.id.summary);
		TextView time = (TextView) findViewById(R.id.time);
		if(fromFragmentItem.equals("true")){
			title.setText((CharSequence) bundle.get("title"));
			region.setText((CharSequence) bundle.get("region"));
			price.setText((CharSequence) ("￥"+bundle.get("price")));
//			summary.setText((CharSequence) bundle.get("summary"));
			time.setText((CharSequence) bundle.get("time"));
		}else if(fromFragmentItem.equals("false")){
			new getDishAsyncTask().execute(id);
		}
		final Builder builder = new AlertDialog.Builder(this);
		Button bbOne = (Button) findViewById(R.id.bbOne);
		bbOne.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				builder.setTitle("数量");
				LinearLayout checkOrder = (LinearLayout) getLayoutInflater().inflate(R.layout.order_dialog, null);
				builder.setView(checkOrder);
				final NumberPicker numPicker = (NumberPicker) checkOrder.findViewById(R.id.numberPicker1);
				numPicker.setMaxValue(99);
				numPicker.setMinValue(0);
//				final DatePicker datePicker = (DatePicker) checkOrder.findViewById(R.id.numberPicker1);
				//设置单价
				TextView uPrice = (TextView) checkOrder.findViewById(R.id.TextView02);
				final TextView totalPrice = (TextView) checkOrder.findViewById(R.id.TextView04);
				uPrice.setText((CharSequence)  ("￥"+bundle.get("price")));
				numPicker.setOnValueChangedListener(new OnValueChangeListener() {

				      @Override
				      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
							int num = numPicker.getValue();
							float total = Float.valueOf(bundle.get("price").toString()) * num;
							totalPrice.setText((CharSequence) ("￥"+Float.valueOf(total).toString()));
				      }
				    });
				builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO 自动生成的方法存根
						if(sharedPreferences.getString("token", "").equals("")){
							Toast.makeText(context, "未登录！", Toast.LENGTH_SHORT).show();
							return;
						}else if(numPicker.getValue() == 0){
							Toast.makeText(context, "预订数量为0", Toast.LENGTH_SHORT).show();
						}else if(!sharedPreferences.getString("token", "").equals("")){
							new putOrdersAsyncTask().execute(Integer.valueOf(numPicker.getValue()).toString());
						}
					}
					
				});
				builder.setNegativeButton(R.string.cancle, null);
				builder.create().show();
			}
		});
		Button bbTwo = (Button) findViewById(R.id.bbTwo);
		bbTwo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent =new Intent(context,IntentCommentPage.class);
				 intent.putExtra("key", bundle);// 封装 email 
				startActivity(intent);
			}
			
		});
		Button bbThree = (Button) findViewById(R.id.bbThree);
		bbThree.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				new putLikesAsyncTask().execute("");
			}
			
		});
		mFragments = new Fragment[2];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.dish_fragment_comment);
		mFragments[1] = fragmentManager.findFragmentById(R.id.dish_fragment_like);
		fragmentTransaction = fragmentManager.beginTransaction()
				.hide(mFragments[0]).hide(mFragments[1]);
		fragmentTransaction.show(mFragments[0]).commit();
		setFragmentIndicator();

	}
	private void setFragmentIndicator() {

		commentsRg = (RadioGroup) findViewById(R.id.commentsRg);
		rbOne = (RadioButton) findViewById(R.id.rbOne);
		rbTwo = (RadioButton) findViewById(R.id.rbTwo);

		commentsRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
	
    //若从详情页中转过来的，重新获取店铺数据
    public class getDishAsyncTask extends AsyncTask<String,Void,Map<String,Object>>
    {
        @Override
        protected void onPreExecute()
        {
        }
		@Override
		protected Map<String, Object> doInBackground(String... params) {
			// TODO 自动生成的方法存根
        	String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=dish&&id=" + params[0];
            String jsonStr = HttpUtils.sendPostMessage(url, "utf-8");
            //    解析服务器端的json数据
            Map<String,Object> dish = JsonUtils.parseSingleDish(jsonStr);
			return dish;
		}
        @Override
        protected void onPostExecute(Map<String,Object> result)
        {
    		TextView title = (TextView) findViewById(R.id.title);
    		TextView region = (TextView) findViewById(R.id.region);
    		TextView restaurant = (TextView) findViewById(R.id.restaurant);
    		TextView price = (TextView) findViewById(R.id.price);
    		TextView summary = (TextView) findViewById(R.id.summary);
//    		TextView time = (TextView) findViewById(R.id.time);
			title.setText((CharSequence) result.get("title"));
			region.setText((CharSequence) result.get("region"));
			restaurant.setText((CharSequence) result.get("restaurant"));
			price.setText((CharSequence) result.get("price"));
			summary.setText((CharSequence) result.get("summary"));
//			time.setText((CharSequence) result.get("regist_date"));
        }
    }
    public class putLikesAsyncTask extends AsyncTask<String,Void,Map<String,String>>
    {
    	@Override
        protected void onPreExecute()
        {
        }

		@Override
		protected Map<String, String> doInBackground(String... arg0) {
			// TODO 自动生成的方法存根
			Map<String,String> result = new HashMap<String,String>();
			String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=putlikes&&"+
						"host=" + bundle.getString("id") +
						"&&userid=" + sharedPreferences.getString("userid", "") +
						"&&token=" + sharedPreferences.getString("token", "");
			String jsonStr = HttpUtils.sendPostMessage(url, "utf-8");
			result = JsonUtils.parseComment(jsonStr);
			return result;
		}
        @Override
        protected void onPostExecute(Map<String,String> result)
        {
        	if(result.get("state").equals("SUCCESS")){
        		Toast.makeText(context, "赞！", Toast.LENGTH_SHORT).show();
        	}else if(result.get("state").equals("OPERATED")){
        		Toast.makeText(context, "已经赞过", Toast.LENGTH_SHORT).show();
        	}else{
        		Toast.makeText(context, "账号登陆无效，请重新登陆！", Toast.LENGTH_SHORT).show();
        	}
        }
    	
    }
    public class putOrdersAsyncTask extends AsyncTask<String,Void,Map<String,String>>
    {
    	@Override
        protected void onPreExecute()
        {
        }
		@Override
		protected Map<String, String> doInBackground(String... params) {
			// TODO 自动生成的方法存根
			Map<String,String> result = new HashMap<String,String>();
			String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=putOrders&&"+
						"host=" + bundle.getString("id") +
						"&&userid=" + sharedPreferences.getString("userid", "") +
						"&&token=" + sharedPreferences.getString("token", "") +
						"&&amount=" + params[0];
			String jsonStr = HttpUtils.sendPostMessage(url, "utf-8");
			result = JsonUtils.parseComment(jsonStr);
			return result;
		}
        @Override
        protected void onPostExecute(Map<String,String> result)
        {
        	if(result.get("state").equals("SUCCESS")){
        		Toast.makeText(context, "已提交订单", Toast.LENGTH_SHORT).show();
        	}else{
        		Toast.makeText(context, "账号登陆无效，请重新登陆！", Toast.LENGTH_SHORT).show();
        	}
        }
    	
    }
}
