package com.shibacon.shibagogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.shibacon.shibachan.flist;
import com.shibacon.utils.JSONParseUtils;
import com.shibacon.utils.StreamUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendList extends Activity {
	private TreeMap<Integer, flist> fset;
    private ListView friend_list_ListView;
    private SimpleAdapter friend_list_SimpleAdapter;
    private List<Bitmap> imalist=new ArrayList<Bitmap>();
    
    private Map<String, Object> showitem = new HashMap<String, Object>();
    private List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==1) {
			fset=JSONParseUtils.getfriendlist((String)msg.obj);
			//showfriendlist();
			for(int i=0;i<fset.size();i++) {
				final String m=fset.get(i+1).getImagepath();
				getImageView(m);
			}
			}
		};
	};//接收朋友列表

	
	private Bitmap ima;
	private Handler handlerforimage=new Handler() {
		public void handleMessage(Message msg) {
			ima=(Bitmap) msg.obj;
			imalist.add(ima);
			if(imalist.size()>=fset.size()) {showfriendlist();}
		};
	};//一一接收图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);

        friend_list_ListView = (ListView)findViewById(R.id.friend_list_ListView);
		friendlistfromserver();
		
       
	}
	public void showfriendlist() {
		 for (int i = 0; i < fset.size(); i++) {
	        	showitem.put("image", imalist.get(i));         
	            showitem.put("username", fset.get(i+1).getName());
	            showitem.put("level", fset.get(i+1).getLevel());
	            listitem.add(showitem);
	        }
		 
	        friend_list_SimpleAdapter = new SimpleAdapter(this, listitem,
		        		R.layout.layout_friend_list, new String[]{"image", "username", "level"}, 
		        		new int[]{R.id.image, R.id.username, R.id.level});

	        friend_list_ListView.setAdapter(friend_list_SimpleAdapter);
	        fset.clear();
	        imalist.clear();
	}
	public void friendlistfromserver() {
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
				}
				
			} catch (Exception e) {
		
				e.printStackTrace();
			}
				
		};}.start();
	}

	public void getImageView(final String imagePath) {
		new Thread() {public void run() {
			try {
				URL url=new URL(imagePath);//QRCode
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
	
	//jump to addfriend
	public void addafriend(View view) {
		startActivity(new Intent(FriendList.this,Addfriend.class));
	}

}

