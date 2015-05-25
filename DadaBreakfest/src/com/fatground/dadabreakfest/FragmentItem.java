package com.fatground.dadabreakfest;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentItem extends ListFragment {
	
	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;

	private PullToRefreshListView mPullRefreshListView;
	private static final String TAG ="FragmentMain";
	
	private FragmentItem fragmentItem = this;
	private List<Map<String,Object>> listItems;
	private SimpleAdapter adapter;
	private ItemAdapter itemAdapter;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String url = "http://121.40.81.110:8080/tastebook/servlet/JsonDishesServlet?action_flag=";
	
	private DisplayImageOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_item, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listItems = new LinkedList<Map<String,Object>>();
//		listView = (ListView)getActivity().findViewById(R.id.res_list);
		mPullRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.dish_list);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				new RefreshDataTask().execute(url);
			}
		});

		// Add an end-of-list listener
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});

		new ListDishAsyncTask().execute(url);
	}
	
    public class ListDishAsyncTask extends AsyncTask<String, Void,Map<String,Object>>
    {
        @Override
        protected void onPreExecute()
        {
        }
        @Override
        protected Map<String,Object> doInBackground(String... params)
        {
            String jsonStr = HttpUtils.sendPostMessage(params[0] + "dish_from&&host=100013", "utf-8");
            boolean netState = HttpUtils.isNetworkConnected(getActivity());
            //    解析服务器端的json数据
            Map<String,Object> checkupdate = JsonUtils.parseCheckUpdate(jsonStr);
            if(!netState){
            	checkupdate.put("network", "false");
            }else{
            	checkupdate.put("network", "true");
            }

//        	listItems = JsonUtils.parseDishes(jsonStr);
            return checkupdate;
        }
        @Override
        protected void onPostExecute(Map<String,Object> result)
        {
        	super.onPostExecute(result);
        	if(result.get("network").equals("false")){
        		Toast.makeText(getActivity(), "网络连接错误", Toast.LENGTH_SHORT).show();
        	}else{
        		String hasUpdate = result.get("hasUpdate").toString();
        		if(hasUpdate.equals("true")){
            	options = new DisplayImageOptions.Builder()
            		.showStubImage(R.drawable.image_null)
            		.showImageForEmptyUri(R.drawable.image_null)
            		.showImageOnFail(R.drawable.image_null)
            		.cacheInMemory(true)
            		.cacheOnDisc(true)
            		.displayer(new RoundedBitmapDisplayer(20))
            		.build();
            	listItems = JsonUtils.parseDishes(result.get("jsonStr").toString());
//            	final String[] from = new String[]{"photograph","title","region","restaurant","price","time"};
//            	final int[] to = new int[]{R.id.photograph,R.id.title,R.id.region,R.id.restaurant,R.id.price,R.id.time};
//            	adapter = new SimpleAdapter(getActivity(),listItems,R.layout.list_dish_item,from,to);
//            	fragmentItem.setListAdapter(adapter);
            	itemAdapter = new ItemAdapter(getActivity(),listItems);
            	fragmentItem.setListAdapter(itemAdapter);
        	}else{
        		Toast.makeText(getActivity(), "没有数据！", Toast.LENGTH_SHORT).show();
        		super.cancel(true);
        	}

        	}
        }
    }
    
	private class RefreshDataTask extends AsyncTask<String, Void, List<Map<String,Object>>> {

		@Override
		protected List<Map<String,Object>> doInBackground(String... params) {
			//获取当前时间
			Date currentDate = new Date(System.currentTimeMillis());
			String dateStr = formatter.format(currentDate);
			Map<String,Object> mMap = listItems.get(listItems.size()-1);
			dateStr = (String) mMap.get("time");
			dateStr = dateStr.replace(" ", "%20");
			//按是否有更新转换jsonStr
			List<Map<String,Object>> refreshList = null;
			String jsonStr = HttpUtils.sendPostMessage(params[0]+"dishes_refresh&&host=100013&&date="+dateStr, "utf-8");
			Map<String,Object> getUpdate = JsonUtils.parseCheckUpdate(jsonStr);
			boolean hasUpdate = (Boolean) getUpdate.get("hasUpdate");
			if(hasUpdate){
				jsonStr = (String) getUpdate.get("jsonStr");
				try {
					Thread.sleep(4000);
					refreshList = JsonUtils.parseDishes(jsonStr);
				} catch (InterruptedException e) {
				}
				return refreshList;
			}else{
				return refreshList;
			}
		}

		@Override
		protected void onPostExecute(List<Map<String,Object>> result) {
			super.onPostExecute(result);
			if(result != null){
				for(int i=0;i<result.size();i++){
					listItems.add(result.get(i));
				}
//				mListItems.addFirst("Added after refresh...");
//				mAdapter.notifyDataSetChanged();
				itemAdapter.notifyDataSetChanged();

				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();

				Toast.makeText(getActivity(), "更新了"+result.size()+"条数据", Toast.LENGTH_SHORT).show();

				
			}else{
				Toast.makeText(getActivity(), "没有更新！", Toast.LENGTH_SHORT).show();				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();
				super.onCancelled();
			}
		}
	}
	public void onListItemClick(ListView parent, View v, int position, long id){
		Log.d(TAG, "onListItemClick");
//		Toast.makeText(getActivity(), "You have selected" + listItems.get(position-1).get("title"), Toast.LENGTH_SHORT).show();
		Intent intent =new Intent(getActivity(),IntentDishPage.class);
		  // 创建一个带“收件人地址”的 email 
		 Bundle bundle =new Bundle();// 创建 email 内容
		 bundle.putBoolean("boolean_key", true);// 编写内容
		 bundle.putString("fromFragmentItem","true");
		 bundle.putString("id", (String) listItems.get(position-1).get("id"));
		 bundle.putString("host", (String) listItems.get(position-1).get("host"));
		 bundle.putString("title", (String) listItems.get(position-1).get("title")); 
		 bundle.putString("region", (String) listItems.get(position-1).get("region"));
//		 bundle.putString("restaurant", (String) listItems.get(position-1).get("restaurant"));
		 bundle.putString("price", (String) listItems.get(position-1).get("price"));
		 bundle.putString("time", (String) listItems.get(position-1).get("time"));
		 intent.putExtra("key", bundle);// 封装 email 
		 startActivity(intent);// 启动新的 Activity 
	}
	
	/**
	 * 自定义适配器
	 */
	class ItemAdapter extends BaseAdapter{
		
		private Context context;
		private List<Map<String, Object>> listItems1;
		protected ImageLoader imageLoader = ImageLoader.getInstance();
		
//		private ImageLoadingListenter animateFirstListener = new AnimateFirstDisplayListener();
		public ItemAdapter(Context context,List<Map<String,Object>> listItems){
			super();
			this.context = context;
//			listContainer = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.listItems1 = listItems;
//			hasChecked = new boolean[getCount()];
		}
		
		public class ViewHolder{
			public TextView title;
			public TextView restaurant;
			public TextView region;
			public TextView price;
			public TextView summary;
			public TextView time;
			public ImageView photograph;
		}
		@Override
		public int getCount() {
			return listItems.size();
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

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			View view = convertView;
			final ViewHolder holder;
			if(convertView == null){
				view = getActivity().getLayoutInflater().inflate(R.layout.list_res_item,parent,false);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.restaurant = (TextView) view.findViewById(R.id.restaurant);
				holder.region = (TextView) view.findViewById(R.id.region);
				holder.price = (TextView) view.findViewById(R.id.price);
				holder.summary = (TextView) view.findViewById(R.id.summary);
				holder.time = (TextView) view.findViewById(R.id.time);
				holder.photograph = (ImageView) view.findViewById(R.id.photograph);
				view.setTag(holder);
			}else{
				holder = (ViewHolder) view.getTag();
			}
			holder.title.setText((String) listItems.get(position).get("title"));
			holder.region.setText((String) listItems.get(position).get("region"));
			holder.restaurant.setText((String) listItems.get(position).get("restaurant"));
			holder.price.setText((String) "￥"+listItems.get(position).get("price"));
			holder.time.setText((String) listItems.get(position).get("time"));
			holder.summary.setText((String) listItems.get(position).get("summary"));
			
			/**
			 * 显示图片
			 * 参数1：图片URL
			 * 参数2：图片控件
			 * 参数3：图片位置
			 * 参数4：监听器
			 */
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			imageLoader.displayImage((String)listItems.get(position).get("image"), holder.photograph, options);
			return view;
		}
		
	}

}
