package com.shibacon.shibagogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shibacon.shibachan.locationlog;
import com.shibacon.utils.StreamUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;

public class ShibaService extends Service {
	private ArrayList<locationlog> alll;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
	
		updateloc();
		uptoserver();

	}

	public void uptoserver() {
		while(true) {
			SystemClock.sleep(600000);//这里是10分钟
			conuploadtoserver();
		//	System.out.println(alll);
			if(!alll.isEmpty()) {
				alll.clear();}
		}	
	}
	public void updateloc() {

		new Thread() {public void run() {

			while(true) {
				
				SystemClock.sleep(10000);
				locationlog log=new locationlog();
				Location lo=getLocation();
				if(lo!=null) {
				Date dt=new Date(System.currentTimeMillis());
				String ti=dt.toString();
				log.setTime(ti);
				log.setLat(lo.getLatitude());
				log.setLng(lo.getLongitude());
				System.out.println(ti+"    ***   "+log.getLat());
				alll.add(log);}
			}
		};}.start();
	}
	
	public Location getLocation() {

		System.out.println("来找位置了");
		String serciveString=Context.LOCATION_SERVICE;
		LocationManager lm=(LocationManager)getSystemService(serciveString);
		List<String> providerlist=lm.getProviders(true);
		
		String provider;
		Location location;
		
		if(providerlist.contains(LocationManager.NETWORK_PROVIDER)) {
			provider=LocationManager.NETWORK_PROVIDER;
	//		System.out.println("网络接收");
		}else if(providerlist.contains(LocationManager.GPS_PROVIDER)) {
			provider=LocationManager.GPS_PROVIDER;
	//		System.out.println("鸡皮阿斯接收");
		}else {
			provider=null;
		}
		
		if(provider!=null) {
			try {
				location=lm.getLastKnownLocation(provider);			
				return location;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else {
		//	System.out.println("不存在提供");
		}
		return null;
		
	}
	public void conuploadtoserver() {
		new Thread() {
			public void run() {
				try {
					JSONArray ja=new JSONArray();
					JSONObject jsonobj=new JSONObject();
					JSONObject joo=null;
					int count=alll.size();
					for(int i=0;i<count;i++) {
						joo=new JSONObject();
						joo.put("date", alll.get(i).getTime());
						joo.put("lat", alll.get(i).getLat());
						joo.put("lng", alll.get(i).getLng());
						ja.put(joo);
						joo=null;
					}
					String info=ja.toString();
					jsonobj.put("timeLocationinfo", info);
					String content=String.valueOf(jsonobj);
					
					URL url=new URL("");
					
					HttpURLConnection conn=(HttpURLConnection)url.openConnection();
					conn.setReadTimeout(5000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setRequestProperty("User-Agent", "Fiddler");
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Charset", "UTF-8");
					OutputStream os=conn.getOutputStream();
					os.write(content.getBytes());
					os.close();
					int code=conn.getResponseCode();
					if(code==200) {						
					}
					
					conn.disconnect();	
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	

}
