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

public class IntentDishFragmentComment extends ListFragment {
	
	private PullToRefreshListView mPullRefreshListView;
	private static final String TAG ="IntentDishFragmentComment";
	private IntentDishFragmentComment fragmentComment = this;
	private View view;
	private List<Map<String,Object>> listItems;
	private SharedPreferences sharedPreferences;
	private ListView listView;
	private Bundle bundle;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=";
	
	private DisplayImageOptions options;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.intent_dish_fragment_comment, container, false);
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		bundle = getActivity().getIntent().getBundleExtra("key");
		listItems = new LinkedList<Map<String,Object>>();
//		listView = (ListView)getActivity().findViewById(R.id.res_list);
		sharedPreferences = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
//		Activity ac = getActivity();
//		mPullRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.comment_list);
//		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
//			@Override
//			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//				// Update the LastUpdatedLabel
//				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//
//				// Do work to refresh the list here.
////				new RefreshDataTask().execute(url);
//			}
//		});
//
//		// Add an end-of-list listener
//		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
//
//			@Override
//			public void onLastItemVisible() {
//				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
//			}
//		});
//		listView = (ListView) getView().findViewById(R.id.); 
		
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
            		"getcomments&&token=" + sharedPreferences.getString("token", "") + 
            		"&&host=" + bundle.getString("id") +
            		"&&userid=" + sharedPreferences.getString("userid", ""), "utf-8");
            //    �����������˵�json����
            Map<String,Object> checkupdate = JsonUtils.parseCheckComments(jsonStr);

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
            	listItems = JsonUtils.parseComments(result.get("comments").toString());
            	List<Map<String,Object>> list = listItems;
            	SimpleAdapter  adapter = new SimpleAdapter(getActivity(), list,
            			R.layout.list_dish_comment, 
            			new String[]{"username","comments","createtime"}, 
            			new int[]{R.id.username,R.id.comment,R.id.time}); 
            	fragmentComment.setListAdapter(adapter); 
//            	ListAdapter itemAdapter = new ListAdapter(getActivity(),listItems);
//            	fragmentComment.setListAdapter(itemAdapter);
//            	itemAdapter.notifyDataSetChanged();
        	}else{
        		Toast.makeText(getActivity(), "û�����ݣ�", Toast.LENGTH_SHORT).show();
        		super.cancel(true);
        	}
        }
    }
	
	/**
	 * �Զ���������
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
			public TextView comment;
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
			// TODO �Զ����ɵķ������
			Log.e(TAG, "getItemId: " + Integer.toString(position));
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO �Զ����ɵķ������
			View view = convertView;
			final ViewHolder holder;
			if(convertView == null){
				view = getActivity().getLayoutInflater().inflate(R.layout.list_dish_comment,parent,false);
				holder = new ViewHolder();
				holder.username = (TextView) view.findViewById(R.id.username);
				holder.comment = (TextView) view.findViewById(R.id.comment);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.photograph = (ImageView) view.findViewById(R.id.photograph);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
			holder.username.setText((String) listItems.get(position).get("title"));
			holder.comment.setText((String) listItems.get(position).get("restaurant"));
			holder.time.setText((String) listItems.get(position).get("time"));
			
			/**
			 * ��ʾͼƬ
			 * ����1��ͼƬURL
			 * ����2��ͼƬ�ؼ�
			 * ����3��ͼƬλ��
			 * ����4��������
			 */
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage("", holder.photograph, options);
			return view;
		}
		
	}
}
