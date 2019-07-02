package com.shibacon.shibagogo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MissionContent extends Activity {

	private double lat;
	private double lng;
	private WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission_content);
		
		
		Location lo=getLocation();
		
		Intent intent=getIntent();
		double dlat=intent.getDoubleExtra("LAT",0);
		double dlng=intent.getDoubleExtra("LNG",0);
		if(lo!=null) {
			lat=lo.getLatitude();
			lng=lo.getLongitude();
	        webview = (WebView) findViewById(R.id.webView);

	        webview.setWebViewClient(new WebViewClient());
	        webview.getSettings().setJavaScriptEnabled(true);
	        webview.loadUrl("http://maps.google.com/maps?" + "saddr="+lat+","+lng+"" + "&daddr="+dlat+","+dlng+"");
		}
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
