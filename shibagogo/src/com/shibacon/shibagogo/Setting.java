package com.shibacon.shibagogo;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.shibacon.utils.BitmapUtils;
import com.shibacon.utils.PathParseUtils;
import com.shibacon.utils.StreamUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Setting extends Activity {
	private Bitmap bitmap;
	private static Uri picuri;
	private static final int CHOOSE_PICTURE=0;
	private static final int TAKE_PICTURE=1;
	private static final int CROP_SMALL_PICTURE=2;
	private String imagePath;
	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==1) {
				imagePath=(String) msg.obj;
				uploadimageview();
			}
		};
	};

	private Button btn_logout;
	private ImageView image=null;

	private Handler hand=new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bitmap bitmap=(Bitmap)msg.obj;
			image.setImageBitmap(bitmap);
		}
	};
	
	private Handler handler2=new Handler() {
		public void handleMessage(Message msg) {
			String message=(String)msg.obj;
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		};};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		image=(ImageView)findViewById(R.id.image_now);
		getimagepath();
		
		btn_logout=(Button)findViewById(R.id.log_out);
		btn_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logout();
				startActivity(new Intent(Setting.this,LogIn.class));
				return;
			}
		});
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showUploadDia();
				
			}
		});
	}
	
	public void getimagepath() {
		new Thread() {public void run() {
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
					is.close();
				}
				
			} catch (Exception e) {
		
				e.printStackTrace();
			}
		};}.start();
	}

	private void uploadimageview() {
		new Thread() {public void run() {
			try {
				URL url=new URL(imagePath);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				int code = conn.getResponseCode();
				if(code == 200) {
					InputStream in = conn.getInputStream();
					Bitmap bitmap=BitmapFactory.decodeStream(in);
					Message msg=Message.obtain();
					msg.obj=bitmap;
					hand.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};}.start();
		
	}

	public void logout() {
		SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
		Editor editor=sp.edit();
		editor.putInt("login", 0);
		editor.commit();
	}
	protected void showUploadDia() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("Set your image");
		String[] items= {"Choose from album","Take a picture"};
		builder.setNegativeButton("Cancel", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				switch (which) {
				case CHOOSE_PICTURE:
					Intent openAlbumIntent=new Intent(Intent.ACTION_GET_CONTENT);
					openAlbumIntent.setType("image/*");
					startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
					break;
				case TAKE_PICTURE:
					Intent openCameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					picuri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,picuri);
					startActivityForResult(openCameraIntent, TAKE_PICTURE);
					break;
				}
			}
		});
		builder.create().show();
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK) {
			switch (requestCode) {
			case TAKE_PICTURE:
				startPhotoZoom(picuri);
				break;
			case CHOOSE_PICTURE:

				startPhotoZoom(data.getData());
				break;
			case CROP_SMALL_PICTURE:
				if(data!=null) {
					setImageToView(data);
				}
				break;
			}
		}
	}
	
	protected void startPhotoZoom(Uri uri) {
		if(uri==null) {
			Log.i("tag", "This uri is not exist");
			return;
		}
		picuri=PathParseUtils.getUri(this, uri);
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(picuri, "image/*");
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_SMALL_PICTURE);
		
	}
	protected void setImageToView(Intent data) {
		Bundle extras=data.getExtras();
		if(extras!=null) {
			bitmap=extras.getParcelable("data");
			image.setImageBitmap(bitmap);
			final String newimage=BitmapUtils.convertIconToString(bitmap);
			uploadnewimage(newimage);
		}
	}
	public void uploadnewimage(final String newimage) {
		new Thread() {public void run() {
			HttpURLConnection conn=null;
			try {
				URL url=new URL("");
				String mytoken=Shibaapp.token;
				JSONObject json=new JSONObject();
				json.put("token", mytoken);
				json.put("Newimage", newimage);
				
				String content=String.valueOf(json);
				
				conn=(HttpURLConnection) url.openConnection();
				conn.setReadTimeout(5000);
				conn.setRequestMethod("POST");
				conn.addRequestProperty("Charset", "UTF-8");
				conn.setDoOutput(true);
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
					handler2.sendMessage(msg);
					is.close();
				}
				
			} catch (Exception e) {
		
				e.printStackTrace();
			}
		};}.start();
	}
}