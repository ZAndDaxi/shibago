package com.shibacon.shibagogo;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.shibacon.shibachan.User;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Newuser extends Activity {
	private EditText et_mailaddress=null;
	private EditText et_password=null;
	private String mailaddr;
	private String password;
	private Button btn_next1=null;//to image uplode新规登陆至上传照片
	private ImageView image_upload=null;
	private Button btn_imageupload=null;//upload上传照片
	private Button btn_next2=null;//上传照片至初始化
	private Bitmap bitmap;
	private static Uri picuri;
	private static final int CHOOSE_PICTURE=0;
	private static final int TAKE_PICTURE=1;
	private static final int CROP_SMALL_PICTURE=2;
	private String messtoken="";
	private int []initial= {1,1,1,1};
	
	private RadioGroup rg1=null;
	private RadioGroup rg2=null;
	private RadioGroup rg3=null;
	private RadioGroup rg4=null;
	
	private Button submit=null;//上传
	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			messtoken=(String) msg.obj;
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_newuser);
		//[1]get mailaddr & pwd
		et_mailaddress=(EditText)findViewById(R.id.mailaddress1);
		et_password=(EditText)findViewById(R.id.password1);
		
		mailaddr=et_mailaddress.getText().toString().trim();
		password=et_password.getText().toString().trim();
	    //[2]transit to imageupload;
		btn_next1=(Button)findViewById(R.id.nexttoimage);
		btn_next1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				setContentView(R.layout.image_initialize);
				//[3]upload an image
				image_upload=(ImageView)findViewById(R.id.uploadimage);
				btn_imageupload=(Button)findViewById(R.id.imageupload);
				btn_imageupload.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showUploadDia();
						
					}
				});//加载图片
				
				//[4]transit to use initialization
				btn_next2=(Button)findViewById(R.id.next2);
				btn_next2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// 用户喜好获取
						setContentView(R.layout.user_initialization);
						rg1=(RadioGroup)findViewById(R.id.radioGroup1);
						rg2=(RadioGroup)findViewById(R.id.radioGroup2);
						rg3=(RadioGroup)findViewById(R.id.radioGroup3);
						rg4=(RadioGroup)findViewById(R.id.radioGroup4);
						if(rg1.getCheckedRadioButtonId()==R.id.btnNo) {
							initial[0]=0;
						}
						if(rg2.getCheckedRadioButtonId()==R.id.btnNo2) {
							initial[1]=0;
						}
						if(rg3.getCheckedRadioButtonId()==R.id.btnNo3) {
							initial[2]=0;
						}
						if(rg4.getCheckedRadioButtonId()==R.id.btnNo4) {
							initial[3]=0;
						}
						
						//[5]upload to server
						submit=(Button)findViewById(R.id.button_submit);
						submit.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
						
								uploadAll(bitmap, mailaddr, password, initial);
								Intent i=new Intent(Newuser.this,MainActivity.class);
								startActivity(i);
								return;
							}
						});
					}
				});//下一个
				
			}
		});
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
			image_upload.setImageBitmap(bitmap);	
		}
	}
	
	
	private void uploadAll(Bitmap bitmap,String mailaddress,String pwd,int []ini) {
		final User user=new User(mailaddress,pwd);
		
		user.setIni(ini[0], ini[1], ini[2], ini[3]);
		user.setImage(BitmapUtils.convertIconToString(bitmap));
		RegisterRequest(user);
		if(!messtoken.equals("fail")) {
			Shibaapp.token=messtoken;
			SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
			Editor editor=sp.edit();
			editor.putInt("login", 1);
			editor.putString("token", Shibaapp.token);
			editor.commit();
		}else {
			Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
		}
	
	}
	public void RegisterRequest(final User user) {
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
				    android.os.Message msg=android.os.Message.obtain();
				    msg.obj=result;
				    handler.sendMessage(msg);
					
					
				}
				
				conn.disconnect();
				
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		};}.start();
	}
}

