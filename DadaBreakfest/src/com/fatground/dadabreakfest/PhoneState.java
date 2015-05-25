package com.fatground.dadabreakfest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneState extends Activity{
	
	private static String Model;
	private static String Brand;
	private String SDKver;
	private static String OS;
	private String phonenumber;
	
	public PhoneState(){
		this.loadPhoneStatus(this);
	}

	@SuppressWarnings("deprecation")
	public void loadPhoneStatus(Context context){
		TelephonyManager phoneMgr=(TelephonyManager)this.getSystemService(context.TELEPHONY_SERVICE);
		this.setPhonenumber(phoneMgr.getLine1Number());
		this.setModel(Build.MODEL);
		this.setBrand(Build.BRAND);
		this.setSDKver(Build.VERSION.SDK);
		this.setOs(Build.VERSION.RELEASE);
	}

	// �õ�����ip��ַ
	    public static String getLocalHostIp()
	    {
	        String ipaddress = "";
	        try
	        {
	            Enumeration<NetworkInterface> en = NetworkInterface
	                    .getNetworkInterfaces();
	            // �������õ�����ӿ�
	            while (en.hasMoreElements())
	            {
	                NetworkInterface nif = en.nextElement();// �õ�ÿһ������ӿڰ󶨵�����ip
	                Enumeration<InetAddress> inet = nif.getInetAddresses();
	                // ����ÿһ���ӿڰ󶨵�����ip
	                while (inet.hasMoreElements())
	                {
	                    InetAddress ip = inet.nextElement();
	                    if (!ip.isLoopbackAddress()
	                            && InetAddressUtils.isIPv4Address(ip
	                                    .getHostAddress()))
	                    {
	                        return ipaddress = ip.getHostAddress();
	                    }
	                }

	            }
	        }
	        catch (SocketException e)
	        {
	            Log.e("feige", "��ȡ����ip��ַʧ��");
	            e.printStackTrace();
	        }
	        return ipaddress;

	    }

	    // �õ�����Mac��ַ
	    public String getLocalMac()
	    {
	        String mac = "";
	        // ��ȡwifi������
	        WifiManager wifiMng = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	        WifiInfo wifiInfor = wifiMng.getConnectionInfo();
	        mac = "������mac��ַ�ǣ�" + wifiInfor.getMacAddress();
	        return mac;
	    }

	public static String getModel() {
		setModel(Build.MODEL);
		return Model;
	}

	public static void setModel(String model) {
		Model = model;
	}

	public String getSDKver() {
		return SDKver;
	}

	public void setSDKver(String sDKver) {
		SDKver = sDKver;
	}

	public static String getOs() {
		setOs(Build.VERSION.RELEASE);
		return OS;
	}

	public static void setOs(String os) {
		OS = os;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public static String getBrand() {
		setBrand(Build.BRAND);
		return Brand;
	}

	public static void setBrand(String brand) {
		Brand = brand;
	}
}
