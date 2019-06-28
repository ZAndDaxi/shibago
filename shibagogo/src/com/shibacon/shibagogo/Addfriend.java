package com.shibacon.shibagogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import com.shibacon.shibachan.afriend;
import com.shibacon.utils.StreamUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Addfriend extends Activity {
	private String friendname;

	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==1) {
				String re=(String) msg.obj;
				Toast.makeText(getApplicationContext(), re, Toast.LENGTH_SHORT).show();
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriend);
	}
	
	public void addfriend(final afriend af) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection conn=null;
				try {
					URL url=new URL("");

					JSONObject job=new JSONObject();
					
					job.put("token", af.getMytoken());
					job.put("friendname", af.getFriendname());
					
					String content=String.valueOf(job);
					conn=(HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);
					conn.setRequestMethod("POST");
					conn.setDoOutput(true);
					conn.setRequestProperty("User-Agent", "Fiddler");
					conn.setRequestProperty("Charset", "UTF-8");
					OutputStream os=conn.getOutputStream();
					os.write(content.getBytes());
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

//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//	// Inflate the menu; this adds items to the action bar if it is present.
//	getMenuInflater().inflate(R.menu.addfriend, menu);
//	return true;
//}
//
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
//	// Handle action bar item clicks here. The action bar will
//	// automatically handle clicks on the Home/Up button, so long
//	// as you specify a parent activity in AndroidManifest.xml.
//	int id = item.getItemId();
//	if (id == R.id.action_settings) {
//		return true;
//	}
//	return super.onOptionsItemSelected(item);
//}