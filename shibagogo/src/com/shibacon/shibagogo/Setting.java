package com.shibacon.shibagogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.shibacon.utils.StreamUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class Setting extends Activity {
	private String imagePath;
	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==1) {
				imagePath=(String) msg.obj;
			}
		};
	};

	private ImageView image=null;

	private Handler hand=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bitmap bitmap=(Bitmap)msg.obj;
			image.setImageBitmap(bitmap);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		//findview这个函数
//		getimagepath();
//		uploadimageview();
	}
	
//	public void getimagepath() {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				HttpURLConnection conn=null;
//				try {
//					URL url=new URL("");
//					String mytoken=Shibaapp.token;
//					conn=(HttpURLConnection) url.openConnection();
//					conn.setReadTimeout(5000);
//					conn.setRequestMethod("POST");
//					conn.addRequestProperty("Charset", "UTF-8");
//					conn.setDoOutput(true);
//					OutputStream os=conn.getOutputStream();
//					os.write(mytoken.getBytes());
//					os.close();
//					int code=conn.getResponseCode();
//					if(code==200) {
//						InputStream is=conn.getInputStream();
//						String result=StreamUtils.readStream(is);
//						Message msg=Message.obtain();
//						msg.what=1;
//						msg.obj=result;
//						handler.sendMessage(msg);
//						is.close();
//					}
//					
//				} catch (Exception e) {
//			
//					e.printStackTrace();
//				}
//				
//			}
//		}) {};
//	}
//
//	private void uploadimageview() {
//		new Thread() {public void run() {
//			try {
//				URL url=new URL(imagePath);//QRCode
//				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//				conn.setRequestMethod("GET");
//				conn.setConnectTimeout(5000);
//				int code = conn.getResponseCode();
//				if(code == 200) {
//					InputStream in = conn.getInputStream();
//					Bitmap bitmap=BitmapFactory.decodeStream(in);
//					Message msg=Message.obtain();
//					msg.obj=bitmap;
//					hand.sendMessage(msg);
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		};}.start();
//		
//	}
//
//	public void logout() {
//		SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
//		Editor editor=sp.edit();
//		editor.putInt("login", 0);
//		editor.commit();
//	}
//}
//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//	// Inflate the menu; this adds items to the action bar if it is present.
//	getMenuInflater().inflate(R.menu.setting, menu);
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
}