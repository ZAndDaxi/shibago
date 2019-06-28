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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendList extends Activity {
//	private TreeMap<Integer, flist> fset;
//	private Handler handler=new Handler() {
//		public void handleMessage(Message msg) {
//			if(msg.what==1) {
//			fset=JSONParseUtils.getfriendlist((String)msg.obj);}
//		};
//	};

	private String[] usernames = new String[]{"user1", "user2", "user3"};
    private String[] level = new String[]{"23", "18", "9"};
    private int[] imageIds = new int[]{R.drawable.fiends_list, R.drawable.fiends_list, R.drawable.fiends_list};

    private ListView friend_list_ListView;
    private SimpleAdapter friend_list_SimpleAdapter;
    private List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
        friend_list_ListView = (ListView)findViewById(R.id.friend_list_ListView);
		
        for (int i = 0; i < usernames.length; i++) {
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("image", imageIds[i]);
            showitem.put("username", usernames[i]);
            showitem.put("level", level[i]);
            listitem.add(showitem);
        }

        friend_list_SimpleAdapter = new SimpleAdapter(this, listitem,
	        		R.layout.layout_friend_list, new String[]{"image", "username", "level"}, 
	        		new int[]{R.id.image, R.id.username, R.id.level});

        friend_list_ListView.setAdapter(friend_list_SimpleAdapter);
	}
//	public void friendlistfromserver() {
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

	//jump to addfriend
	public void addfriend(View view) {
		startActivity(new Intent(FriendList.this,Addfriend.class));
	}

}

