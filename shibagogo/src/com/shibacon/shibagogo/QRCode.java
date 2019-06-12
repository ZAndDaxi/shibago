package com.shibacon.shibagogo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class QRCode extends Activity {

	private ImageView qr=null;
	private Handler handler=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bitmap bitmap=(Bitmap)msg.obj;
			qr.setImageBitmap(bitmap);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		
		qr=(ImageView)findViewById(R.id.qrimage);
		uploadImageview();
		
	}
	private void uploadImageview() {
		new Thread() {public void run() {
			try {
				URL url=new URL("");//QRCode
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();
				if(code == 200) {
					InputStream in = conn.getInputStream();
					Bitmap bitmap=BitmapFactory.decodeStream(in);
					Message msg=Message.obtain();
					msg.obj=bitmap;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};}.start();
		
	}


}
