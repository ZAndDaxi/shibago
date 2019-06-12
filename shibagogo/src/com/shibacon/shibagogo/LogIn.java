package com.shibacon.shibagogo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				if(!mailaddr.contains("@")) {
					Toast.makeText(LogIn.this, "有効なメールアドレスを入力ください", Toast.LENGTH_SHORT).show();
					return;
				}

				if(canfind(mailaddr, pwd)) {
					Intent i=new Intent(LogIn.this,MainActivity.class);
					startActivity(i);
					return;
					
				}else {
					Toast.makeText(LogIn.this, "入力間違いました", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			private boolean Canfind(String miladdr,String pwd) {
				
				return false;
			}

			private boolean canfind(String mailaddr, String pwd) {
				if(mailaddr.equals("xing@a")&&pwd.equals("test")) {
					return true;
				}else {
					return false;
				}
			}
		});
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
