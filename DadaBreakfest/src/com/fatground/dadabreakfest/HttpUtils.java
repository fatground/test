package com.fatground.dadabreakfest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpUtils
{
    /**
     * @param path    请求的服务器URL地址
     * @param encode    编码格式
     * @return    将服务器端返回的数据转换成String
     */
    public static String sendPostMessage(String path, String encode)
    {
        String result = "";
        HttpClient httpClient = new DefaultHttpClient();
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3*1000);
        HttpConnectionParams.setSoTimeout(httpParams, 3*1000);
        try
        {
            HttpPost httpPost = new HttpPost(path);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                HttpEntity httpEntity = httpResponse.getEntity();
                if(httpEntity != null)
                {
                    result = EntityUtils.toString(httpEntity, encode);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            httpClient.getConnectionManager().shutdown();
        }
        
        return result;
    }
    
    public static boolean isNetworkConnected(Context context) {  
        if (context != null) {  
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
                    .getSystemService(Context.CONNECTIVITY_SERVICE);  
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
            if (mNetworkInfo != null) {  
                return mNetworkInfo.isAvailable();  
            }  
        }  
        return false;  
    }
    
}
