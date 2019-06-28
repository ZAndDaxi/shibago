package com.shibacon.shibagogo;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.shibacon.utils.StreamUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends Activity {
	private EditText ed_addr = null;
	private EditText ed_pwd =null;
	private String messtoken="";

	private Handler handler=new Handler() {
		public void handleMessage(android.os.Message msg) {
			messtoken=(String)msg.obj;
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(checkLogin()) {
			Intent intent=new Intent(LogIn.this,MainActivity.class);
			startActivity(intent);
		}
		setContentView(R.layout.activity_log_in);
		
		
		ed_addr=(EditText) findViewById(R.id.mailaddress);
		ed_pwd=(EditText)findViewById(R.id.password);
		Button btn_login=(Button)findViewById(R.id.login);
		TextView tv_new=(TextView)findViewById(R.id.noaccount);
		
		tv_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(LogIn.this,Newuser.class);
				startActivity(i);
				return;
				
			}
		});
		
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mailaddr=ed_addr.getText().toString().trim();
				String pwd=ed_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(mailaddr)) {
					Toast.makeText(LogIn.this, "メールアドレスを入力ください", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(pwd)) {
					Toast.makeText(LogIn.this, "パスワードを入力ください", Toast.LENGTH_SHORT).show();
					return;
				}
				LoginRequest(mailaddr, pwd);
//				if(mailaddr.equals("a")&&pwd.equals("b")) {
//					messtoken="test";
//				}

				if(canfind(messtoken)) {
					Shibaapp.token=messtoken;
					SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
					Editor editor=sp.edit();
					editor.putInt("login", 1);
					editor.putString("token", Shibaapp.token);
					editor.commit();
					Intent i=new Intent(LogIn.this,MainActivity.class);
					startActivity(i);
					return;
					
				}else {
					Toast.makeText(LogIn.this, "入力間違いました", Toast.LENGTH_SHORT).show();
					return;
				}
			}

		});	
	}
	private boolean canfind(String mess) {
		if(mess=="") {
			return false;
		}else if(mess.equals("false")) {
			return false;
		}else {
			return true;
		}
	}
	private void LoginRequest(final String maila,final String pwd) {
		new Thread() {public void run() {
			HttpURLConnection conn=null;
			try {
				String path="";
				JSONObject userJSON=new JSONObject();
				userJSON.put("userMailAddress",maila);//String
				userJSON.put("userPassword", pwd);
				String content=String.valueOf(userJSON);
				
				URL url=new URL(path);
				
				conn=(HttpURLConnection)url.openConnection();
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
				    android.os.Message msg=android.os.Message.obtain();
				    msg.obj=result;
				    handler.sendMessage(msg);
					
					
				}
				
				
				} catch (Exception e) {
				e.printStackTrace();
				}finally{
					if(conn!=null)
						conn.disconnect();
				}//String

		}}.start();
		return;
	}
	public boolean checkLogin() {
		SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
		int i=sp.getInt("login", 0);
		if(i==1) {
			Shibaapp.token=sp.getString("token", Shibaapp.token);
			return true;
		}
		return false;

	}

}


















//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//	// Inflate the menu; this adds items to the action bar if it is present.
//	getMenuInflater().inflate(R.menu.log_in, menu);
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
