package com.shibacon.shibagogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.shibacon.utils.StreamUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private int plevel=20;
	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
		
			if(msg.what==1) {
				 String l=(String) msg.obj;
				 Integer i=new Integer(l);
				 plevel=i.intValue();
			}			
			
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		Intent intent=new Intent(this,ShibaService.class);
//		startService(intent);
		TextView tv_level=findViewById(R.id.level_score);
		tv_level.setText(String.valueOf(plevel));
	}
	

	public void goToFriendList(View view) {
		Intent i=new Intent(MainActivity.this,FriendList.class);
		startActivity(i);
	}
	
	public void goToMissionList(View view) {
		Intent i=new Intent(MainActivity.this,MissionList.class);
		startActivity(i);
	}
	
	public void goToSetting(View view) {
		Intent i=new Intent(MainActivity.this,Setting.class);
		startActivity(i);
	}
	
	
	public void getlevel() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection conn=null;
				try {
					URL url=new URL("");
					String mytoken=Shibaapp.token;
					conn=(HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setRequestMethod("POST");
					conn.addRequestProperty("Charset", "UTF-8");
					conn.setDoOutput(true);
					OutputStream os=conn.getOutputStream();
					os.write(mytoken.getBytes());
					os.close();
					int code=conn.getResponseCode();
					if(code==200) {
						InputStream is=conn.getInputStream();
						String result=StreamUtils.readStream(is);
						Message msg=Message.obtain();
						msg.what=1;
						msg.obj=result;
						handler.sendMessage(msg);
					}
					
				} catch (Exception e) {
			
					e.printStackTrace();
				}
				
			}
		}) {};
	}

}

