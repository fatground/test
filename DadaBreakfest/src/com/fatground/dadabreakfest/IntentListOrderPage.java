package com.fatground.dadabreakfest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IntentListOrderPage extends ListActivity{
	public Context context = this;
	private IntentListOrderPage intentListOrderPage = this;
	public Map<String,String> map;
	private List<Map<String,Object>> listItems;
	private SharedPreferences sharedPreferences;
	private DisplayImageOptions options;
	private LinearLayout bottombar;
	private Button delete;
	private ListAdapter itemAdapter;
	private static final String TAG ="IntentListOrderPage";
	private String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=";
	
	private int checkState = 0;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intent_order_page);
		listItems = new LinkedList<Map<String,Object>>();
		sharedPreferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		delete = (Button) findViewById(R.id.delete);
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				//获取需要删除的订单数据
				JSONObject jsonObj = new JSONObject();
				for(int i=0;i < listItems.size();i++){
					if(listItems.get(i).get("ischecked").equals("checked")){
						try {
							jsonObj.put(String.valueOf(i), listItems.get(i).get("id"));
						} catch (JSONException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
				String content = String.valueOf(jsonObj);
				url += "deleteOrders&&orders=" + content +
	            		"&&token=" + sharedPreferences.getString("token", "") + 
	            		"&&userid=" + sharedPreferences.getString("userid", "");
				String test = url;
				System.out.println(test);
				RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				LinearLayout title = (LinearLayout) findViewById(R.id.title);
				LinearLayout bottombar = (LinearLayout) findViewById(R.id.bottom_bar);
				rp.setMargins(0, title.getHeight(), 0, bottombar.getHeight());
				getListView().setLayoutParams(rp);
				bottombar.setVisibility(0);
//				int invisible = findViewById(R.id.checkBox1).INVISIBLE;
				if(checkState == 0){
					itemAdapter.notifyDataSetChanged();
				}
			}});
		new ListCommentAsyncTask().execute(url);
	}
	
	/**
	 * 后台进程
	 */
	public class ListCommentAsyncTask extends AsyncTask<String, Void,Map<String,Object>>
    {
        @Override
        protected void onPreExecute()
        {
        }
        @Override
        protected Map<String,Object> doInBackground(String... params)
        {
            String jsonStr = HttpUtils.sendPostMessage(params[0] + 
            		"getOrders&&token=" + sharedPreferences.getString("token", "") + 
            		"&&userid=" + sharedPreferences.getString("userid", ""), "utf-8");
            //    解析服务器端的json数据
            Map<String,Object> checkupdate = JsonUtils.parseCheckOrders(jsonStr);

//        	listItems = JsonUtils.parseDishes(jsonStr);
            return checkupdate;
        }
        @Override
        protected void onPostExecute(Map<String,Object> result)
        {
        	super.onPostExecute(result);
        	String hasupdate =  result.get("state").toString();
        	if(hasupdate.equals("SUCCESS")){
            	options = new DisplayImageOptions.Builder()
            		.showStubImage(R.drawable.weibo_listab_mylist_off)
            		.showImageForEmptyUri(R.drawable.weibo_listab_mylist_off)
            		.showImageOnFail(R.drawable.weibo_listab_mylist_off)
            		.cacheInMemory(true)
            		.cacheOnDisc(true)
            		.displayer(new RoundedBitmapDisplayer(20))
            		.build();
            	listItems = JsonUtils.parseOrders(result.get("orders").toString());
//            	List<Map<String,Object>> list = listItems;
            	itemAdapter = new ListAdapter(context,listItems);
            	setListAdapter(itemAdapter); 
//            	fragmentComment.setListAdapter(itemAdapter);
//            	itemAdapter.notifyDataSetChanged();
        	}else{
        		Toast.makeText(context, "没有数据！", Toast.LENGTH_SHORT).show();
        		super.cancel(true);
        	}
        }
    }
	
	/**
	 * 自定义适配器
	 */
	class ListAdapter extends BaseAdapter{
		
		private Context context;
		private List<Map<String, Object>> listItems1;
		private LayoutInflater listContainer;
		private boolean[] hasChecked;
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		
//		private ImageLoadingListenter animateFirstListener = new AnimateFirstDisplayListener();
		public ListAdapter(Context context,List<Map<String,Object>> listItems){
			super();
			this.context = context;
			Log.e(TAG, "ListAdapter context " + context.toString());
//			listContainer = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.listItems1 = listItems;
			listContainer = LayoutInflater.from(context);
//			hasChecked = new boolean[getCount()];
		}
		
		public class ViewHolder{
			public TextView title;
			public TextView createtime;
			public TextView unit_price;
			public TextView total_price;
			public TextView amount;
			public ImageView photograph;
			public CheckBox checkBox1;
		}
		@Override
		public int getCount() {
			Log.e(TAG, "getCount: " + Integer.toString(listItems.size()));
			return listItems1.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			return position;
		}

		public void showCheckBox(){
			listItems.size();
			for(int i=0;i<listItems.size();i++){
				
			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			final int index = position;
			View view = convertView;
			final ViewHolder holder;
			if(convertView == null){
				view = getLayoutInflater().inflate(R.layout.list_order_item,parent,false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.unit_price = (TextView) view.findViewById(R.id.unit_price);
				holder.total_price = (TextView) view.findViewById(R.id.total_price);
				holder.amount = (TextView) view.findViewById(R.id.amount);
				holder.createtime = (TextView) view.findViewById(R.id.createtime);
				holder.photograph = (ImageView) view.findViewById(R.id.photograph);
				holder.checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
			holder.title.setText((String) listItems.get(position).get("dishname"));
			holder.unit_price.setText((String) "￥" + listItems.get(position).get("unit_price"));
			holder.total_price.setText((String) "￥" + listItems.get(position).get("total_price"));
			holder.amount.setText((String) listItems.get(position).get("amount"));
			holder.createtime.setText((String) listItems.get(position).get("createtime"));
			if(checkState == 0){
				holder.checkBox1.setVisibility(1);
			}
			holder.checkBox1.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					// TODO 自动生成的方法存根
					if(isChecked){
						listItems.get(index).put("ischecked", "checked");
					}else{
						listItems.get(index).put("ischecked", "unchecked");
					}
				}
				
			});
			if(listItems.get(index).get("ischecked").equals("checked")){
				holder.checkBox1.setChecked(true);
			}else{
				holder.checkBox1.setChecked(false);
			}
			/**
			 * 显示图片
			 * 参数1：图片URL
			 * 参数2：图片控件
			 * 参数3：图片位置
			 * 参数4：监听器
			 */
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage("", holder.photograph, options);
			return view;
		}
		
	}

}
