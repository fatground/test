package com.fatground.dadabreakfest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fatground.dadabreakfest.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class IntentDishFragmentLike extends ListFragment {
	
	private PullToRefreshListView mPullRefreshListView;
	private static final String TAG ="IntentDishFragmentComment";
	private IntentDishFragmentLike fragmentLike = this;
	private View view;
	private List<Map<String,Object>> listItems;
	private SharedPreferences sharedPreferences;
	private ListView listView;
	private Bundle bundle;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=";
	
	private DisplayImageOptions options;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.intent_dish_fragment_like, container, false);
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bundle = getActivity().getIntent().getBundleExtra("key");
		listItems = new LinkedList<Map<String,Object>>();
//		listView = (ListView)getActivity().findViewById(R.id.res_list);
		sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		
		new ListCommentAsyncTask().execute(url);

	}
	
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
            		"getlikes&&token=" + sharedPreferences.getString("token", "") + 
            		"&&host=" + bundle.getString("id") +
            		"&&userid=" + sharedPreferences.getString("userid", ""), "utf-8");
            //    解析服务器端的json数据
            Map<String,Object> checkupdate = JsonUtils.parseCheckLikes(jsonStr);

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
            	listItems = JsonUtils.parseLikes(result.get("likes").toString());
            	ListAdapter itemAdapter = new ListAdapter(getActivity(),listItems);
            	List<Map<String,Object>> list = listItems;
//            	SimpleAdapter  adapter = new SimpleAdapter(getActivity(), list,
//            			R.layout.list_dish_comment, 
//            			new String[]{"username","comments","createtime"}, 
//            			new int[]{R.id.username,R.id.comment,R.id.time}); 
            	fragmentLike.setListAdapter(itemAdapter); 
//            	fragmentComment.setListAdapter(itemAdapter);
//            	itemAdapter.notifyDataSetChanged();
        	}else{
        		Toast.makeText(getActivity(), "没有数据！", Toast.LENGTH_SHORT).show();
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
			public TextView username;
			public TextView time;
			public ImageView photograph;
		}
		@Override
		public int getCount() {
			Log.e(TAG, "getCount: " + Integer.toString(listItems.size()));
			return listItems1.size();
		}

		@Override
		public Object getItem(int position) {
			Log.e(TAG, "getItem: " + listItems1.get(position));
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO 自动生成的方法存根
			Log.e(TAG, "getItemId: " + Integer.toString(position));
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			View view = convertView;
			final ViewHolder holder;
			if(convertView == null){
				view = getActivity().getLayoutInflater().inflate(R.layout.list_dish_like,parent,false);
				holder = new ViewHolder();
				holder.username = (TextView) view.findViewById(R.id.username);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.photograph = (ImageView) view.findViewById(R.id.photograph);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
			holder.username.setText((String) listItems.get(position).get("username"));
			holder.time.setText((String) listItems.get(position).get("createtime"));
			
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
