package com.shibacon.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.shibacon.shibachan.User;
import com.shibacon.shibagogo.Newuser;

import android.app.Notification.MessagingStyle.Message;
import android.os.Handler;
import android.widget.Toast;

public class HttpUrlConnectionUtils {
	public static void SendRequest() {}
	

	////////注册的向服务器发送请求
	public static void RegisterRequest(final User user) {
		new Thread() {public void run() {
			try {
				JSONObject userJSON=new JSONObject();
				userJSON.put("userMailAddress", user.getMailAddr());//String
				userJSON.put("userPassword", user.getPassword());//String
				userJSON.put("userInitialInfo", user.getIni());//int[]
				userJSON.put("userImage", user.getImage());//Bitmap
				
				String content=String.valueOf(userJSON);
				
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
					InputStream is=conn.getInputStream();
					
					String result=StreamUtils.readStream(is);
					
					
				}
				
				conn.disconnect();
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		};}.start();
	}
	
	public static void GetQRRequest() {}
	
	public static void GetImageRequest() {}
	
	public static void GetMissionRequest() {}
	
	public static void GetFriendListRequest() {}

}
