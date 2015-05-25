package com.fatground.dadabreakfest;

import java.util.*;

import org.json.*;


public class JsonUtils
{
    /**
     * @param citiesString    从服务器端得到的JSON字符串数据
     * @return    解析JSON字符串数据，放入List当中
     */
    public static List<String> parseCities(String citiesString)
    {
        List<String> cities = new ArrayList<String>();
        
        try
        {
            JSONObject jsonObject = new JSONObject(citiesString);
            JSONArray jsonArray = jsonObject.getJSONArray("name");
            for(int i = 0; i < jsonArray.length(); i++)
            {
                cities.add(jsonArray.getString(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return cities;
    }
    public static List<String> parseName(String jsonStr){
    	List<String> names = new ArrayList<String>();
    	String name = "";
    	JSONArray myJsonArray;
    	try{
    		myJsonArray = new JSONArray(jsonStr);
    		for(int i = 0;i<= myJsonArray.length();i++){
    		    //获取每一个JsonObject对象
    		    JSONObject myjObject = myJsonArray.getJSONObject(i);
    		    names.add(myjObject.getString("name"));
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return names;
    }
    /**
     * 将JSON转换餐厅列表
     * @param jsonStr
     */
    public static List<Map<String,Object>> parseRestaurant(String jsonStr){
    	List<Map<String,Object>> restaurants = new ArrayList<Map<String,Object>>();
    	JSONArray myJsonArray;
    	try{
    		myJsonArray = new JSONArray(jsonStr);
    		for(int i=0;i< myJsonArray.length();i++){
    			//获取每一个JSONObject对象
    			JSONObject myjObject = myJsonArray.getJSONObject(i);
    			Map<String, Object> map = new HashMap<String,Object>();
    			map.put("id", myjObject.get("id"));
    			map.put("title", myjObject.get("name"));
    			map.put("region", "["+myjObject.get("region")+"]");
    			map.put("restaurant", myjObject.get("name"));
    			map.put("price", "3.0");
    			map.put("summary", myjObject.get("summary"));
    			map.put("regist_date", myjObject.get("regist_date"));
    			map.put("image", myjObject.get("url_1"));
    			restaurants.add(map);
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return restaurants;
    }
    public static Map<String,Object> parseCheckUpdate(String jsonStr){
		Map<String,Object> map = new HashMap<String,Object>();
    	JSONObject myJsonObject;
    	try{
    		myJsonObject = new JSONObject(jsonStr);
    		map.put("hasUpdate", myJsonObject.get("hasUpdate"));
    		map.put("jsonStr", myJsonObject.get("JSONstr"));
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return map;
    }
    public static LoginState parseLogin(String jsonStr){
    	LoginState loginState = new LoginState();
    	JSONObject myJsonObject;
    	try{
    		myJsonObject = new JSONObject(jsonStr);
    		loginState.setLoginState(myJsonObject.getString("loginState"));
    		loginState.setToken(myJsonObject.getString("token"));
    		loginState.setUserinfo(myJsonObject.getString("userInfo"));
    	}catch(JSONException e){
			e.printStackTrace();
		}
    	return loginState;
    }
	public static List<Map<String, Object>> parseDishes(String jsonStr) {
    	List<Map<String,Object>> Dishes = new ArrayList<Map<String,Object>>();
    	JSONArray myJsonArray;
    	try{
    		myJsonArray = new JSONArray(jsonStr);
    		for(int i=0;i< myJsonArray.length();i++){
    			//获取每一个JSONObject对象
    			JSONObject myjObject = myJsonArray.getJSONObject(i);
    			Map<String, Object> map = new HashMap<String,Object>();
    			map.put("id", myjObject.get("id"));
    			map.put("host", myjObject.get("host"));
    			map.put("title", myjObject.get("name"));
//    			map.put("restaurant", myjObject.get("resname"));
//    			map.put("region", "["+myjObject.get("region")+"]");
    			map.put("price", myjObject.getString("price"));
    			map.put("time", myjObject.get("createtime"));
    			map.put("image", "http://121.40.81.110:8080/tastebook/res_img/100001.jpg");
    			Dishes.add(map);
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return Dishes;
	}
	public static List<Map<String, Object>> parseDishesFromId(String jsonStr) {
    	List<Map<String,Object>> Dishes = new ArrayList<Map<String,Object>>();
    	JSONArray myJsonArray;
    	try{
    		myJsonArray = new JSONArray(jsonStr);
    		for(int i=0;i< myJsonArray.length();i++){
    			//获取每一个JSONObject对象
    			JSONObject myjObject = myJsonArray.getJSONObject(i);
    			Map<String, Object> map = new HashMap<String,Object>();
    			map.put("id", myjObject.get("id"));
    			map.put("title", myjObject.get("name"));
    			map.put("price", myjObject.get("price"));
    			map.put("time", myjObject.get("createtime"));
    			map.put("image", "http://121.40.81.110:8080/tastebook/res_img/100001.jpg");
    			Dishes.add(map);
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return Dishes;
	}
	public static Map<String, Object> parseSingleRestaurant(String jsonStr) {
		Map<String, Object> map = new HashMap<String,Object>();
    	try{
			//获取每一个JSONObject对象
			JSONObject myjObject = new JSONObject(jsonStr);
			map.put("id", myjObject.get("id"));
			map.put("title", myjObject.get("name"));
			map.put("region", "["+myjObject.get("region")+"]");
			map.put("restaurant", myjObject.get("name"));
			map.put("price", "3.0");
			map.put("summary", myjObject.get("summary"));
			map.put("regist_date", myjObject.get("regist_date"));
			map.put("image", "http://121.40.81.110:8080/tastebook/res_img/100001.jpg");
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return map;
	}
	public static Map<String, Object> parseSingleDish(String jsonStr) {
		Map<String, Object> map = new HashMap<String,Object>();
    	try{
			//获取每一个JSONObject对象
			JSONObject myjObject = new JSONObject(jsonStr);
			map.put("id", myjObject.get("id"));
			map.put("title", myjObject.get("name"));
			map.put("region", "["+myjObject.get("region")+"]");
			map.put("resname", myjObject.get("resname"));
			map.put("price", "3.0");
			map.put("summary", myjObject.get("summary"));
			map.put("regist_date", myjObject.get("regist_date"));
			map.put("image", "http://121.40.81.110:8080/tastebook/res_img/100001.jpg");
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return map;
	}
	public static Map<String,String> parseUserInfo(String jsonStr) {
		// TODO 自动生成的方法存根
		Map<String,String> map = new HashMap<String,String>(); 
		try {
			JSONObject myjObject = new JSONObject(jsonStr);
			map.put("check", myjObject.getString("check"));
		} catch (JSONException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		if(map.get("check").equals("SUCCESS")){
	    	try{
				//获取每一个JSONObject对象
	    		JSONObject myjObject = new JSONObject(jsonStr);
				myjObject = new JSONObject(jsonStr);
				map.put("username", myjObject.getString("name"));
				map.put("userid", myjObject.getString("id"));
				map.put("type", myjObject.getString("type"));
				map.put("grade", myjObject.getString("grade"));
				map.put("gender", myjObject.getString("gender"));
				map.put("ordercount", myjObject.getString("orders_count"));
				map.put("phone", myjObject.getString("phone"));
//				map.put("mailadd", myjObject.getString("mailadd"));
//				map.put("address", myjObject.getString("address"));
	    	}catch(JSONException e){
	    		e.printStackTrace();
	    	}
		}
		return map;
	}
	public static Map<String, String> parseComment(String jsonStr) {
		// TODO 自动生成的方法存根
		Map<String,String> map = new HashMap<String,String>();
		try{
			//获取每一个JSONObject对象
			JSONObject myjObject = new JSONObject(jsonStr);
			map.put("state", myjObject.getString("state"));
		}catch(JSONException e){
			e.printStackTrace();
		}
		return map;
	}
	public static Map<String, Object> parseCheckComments(String jsonStr) {
		Map<String,Object> map = new HashMap<String,Object>();
    	JSONObject myJsonObject;
    	try{
    		myJsonObject = new JSONObject(jsonStr);
    		map.put("state", myJsonObject.get("state"));
    		map.put("comments", myJsonObject.get("comments"));
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return map;
	}
	public static Map<String, Object> parseCheckLikes(String jsonStr) {
		Map<String,Object> map = new HashMap<String,Object>();
    	JSONObject myJsonObject;
    	try{
    		myJsonObject = new JSONObject(jsonStr);
    		map.put("state", myJsonObject.get("state"));
    		map.put("likes", myJsonObject.get("likes"));
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return map;
	}
	public static Map<String, Object> parseCheckOrders(String jsonStr) {
		Map<String,Object> map = new HashMap<String,Object>();
    	JSONObject myJsonObject;
    	try{
    		myJsonObject = new JSONObject(jsonStr);
    		map.put("state", myJsonObject.get("state"));
    		map.put("orders", myJsonObject.get("orders"));
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return map;
	}
	public static List<Map<String, Object>> parseComments(String jsonStr) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
    	try{
			//获取每一个JSONObject对象
    		JSONArray myJsonArray = new JSONArray(jsonStr);
    		for(int i=0;i< myJsonArray.length();i++){
    			JSONObject myjObject = myJsonArray.getJSONObject(i);
    			Map<String, Object> map = new HashMap<String,Object>();
				map.put("id", myjObject.get("id"));
				map.put("username", myjObject.get("username"));
				map.put("host", myjObject.get("host"));
				map.put("dishname", myjObject.get("dishname"));
				map.put("userid", myjObject.get("userid"));
				map.put("comments", myjObject.get("comments"));
				map.put("createtime", myjObject.get("createtime"));
			listmap.add(map);
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return listmap;
	}
	public static List<Map<String, Object>> parseLikes(String jsonStr) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
    	try{
			//获取每一个JSONObject对象
    		JSONArray myJsonArray = new JSONArray(jsonStr);
    		for(int i=0;i< myJsonArray.length();i++){
    			JSONObject myjObject = myJsonArray.getJSONObject(i);
    			Map<String, Object> map = new HashMap<String,Object>();
				map.put("id", myjObject.get("id"));
				map.put("username", myjObject.get("username"));
				map.put("host", myjObject.get("host"));
				map.put("dishname", myjObject.get("dishname"));
				map.put("userid", myjObject.get("userid"));
				map.put("createtime", myjObject.get("createtime"));
			listmap.add(map);
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
    	return listmap;
	}
	public static List<Map<String,Object>> parseOrders(String jsonStr) {
		// TODO 自动生成的方法存根
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
    	try{
			//获取每一个JSONObject对象
    		JSONArray myJsonArray = new JSONArray(jsonStr);
    		for(int i=0;i< myJsonArray.length();i++){
    			JSONObject myjObject = myJsonArray.getJSONObject(i);
    			Map<String, Object> map = new HashMap<String,Object>();
				map.put("id", myjObject.get("id"));
				map.put("username", myjObject.get("username"));
				map.put("host", myjObject.get("host"));
				map.put("dishname", myjObject.get("dishname"));
				map.put("userid", myjObject.get("userid"));
				map.put("amount", myjObject.get("amount"));
				map.put("unit_price", myjObject.get("unit_price"));
				map.put("total_price", myjObject.get("total_price"));
				map.put("createtime", myjObject.get("createtime"));
				map.put("ischecked", "unchecked");
			listmap.add(map);
    		}
    	}catch(JSONException e){
    		e.printStackTrace();
    	}
		return listmap;
	}
	public static Map<String, String> parseRegist(String jsonStr) {
		// TODO 自动生成的方法存根
		Map<String,String> map = new HashMap<String,String>();
		try{
			JSONObject myObject = new JSONObject(jsonStr);
			map.put("state", myObject.getString("state"));
		}catch(JSONException e){
    		e.printStackTrace();
    	}
		return map;
	}
}