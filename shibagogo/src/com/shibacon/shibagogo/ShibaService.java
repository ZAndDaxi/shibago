package com.shibacon.shibagogo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shibacon.shibachan.locationlog;

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
//		new Thread() {public void run() {
//			while(true) {
//				SystemClock.sleep(3000);
//				System.out.println("邢小茜");
//			}
//		};}.start();

	}

//	public void uptoserver() {
//		while(true) {
//			SystemClock.sleep(60000);//这里是1分钟
//			System.out.println(alll);
//			alll.clear();
//		}
//		
//		
//	}
	public void updateloc() {

		new Thread() {public void run() {

			while(true) {
				
				SystemClock.sleep(5000);
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
	
	

}
