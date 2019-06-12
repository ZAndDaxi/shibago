package com.shibacon.shibagogo;

import java.io.File;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Newuser extends Activity {
	private EditText et_mailaddress=null;
	private EditText et_password=null;
	private Button btn_next1=null;//to image uplode新规登陆至上传照片
	private ImageView image_upload=null;
	private Button btn_imageupload=null;//upload上传照片
	private Button btn_next2=null;
	private Bitmap bitmap;
	private static Uri picuri;
	private static final int CHOOSE_PICTURE=0;
	private static final int TAKE_PICTURE=1;
	private static final int CROP_SMALL_PICTURE=2;
	
	private int []initial= {1,1,1,1};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		et_mailaddress=(EditText)findViewById(R.id.mailaddress1);
		et_password=(EditText)findViewById(R.id.password1);
		setContentView(R.layout.activity_newuser);
		btn_next1=(Button)findViewById(R.id.nexttoimage);
		btn_next1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				setContentView(R.layout.image_initialize);
				image_upload=(ImageView)findViewById(R.id.uploadimage);
				btn_imageupload=(Button)findViewById(R.id.imageupload);
				btn_next2=(Button)findViewById(R.id.next2);
				btn_imageupload.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showUploadDia();
						
					}
				});//加载图片
				btn_next2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						setContentView(R.layout.user_initialization);
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
		// TODO Auto-generated method stub
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
		}
		picuri=uri;
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
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
		new Thread() {public void run() {
			JSONObject userJSON=new JSONObject();

		};}.start();
	}
}
//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//	// Inflate the menu; this adds items to the action bar if it is present.
//	getMenuInflater().inflate(R.menu.newuser, menu);
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
