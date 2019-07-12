package com.shibacon.shibagogo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shibacon.shibachan.Mission;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MissionList extends Activity {
	private ArrayList<Mission> milist;
	   private ListView mission_list_ListView;
	    private SimpleAdapter mission_list_SimpleAdapter;
	    private List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
	private Handler handler=new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==1) {
				milist=JSONParseUtils.getmissionlist((String)msg.obj);
				 showmissionlist();
			}
		};
	};
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mission_list);
		mission_list_ListView = (ListView)findViewById(R.id.mission_list_ListView);
		getFromserver();
       
        mission_list_ListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				double lat=milist.get(position).getLat();
				double lng=milist.get(position).getLng();
				Intent intent=new Intent(MissionList.this,MissionContent.class);
				intent.putExtra("LAT", lat);
				intent.putExtra("LNG", lng);
				startActivity(intent);
			}
		});
      
	}
	public void showmissionlist() {
		for (int i = 0; i < milist.size(); i++) {
            Map<String, Object> showitem = new HashMap<String, Object>();
            showitem.put("mission_names",milist.get(i).getMission());
            showitem.put("mission_contents",milist.get(i).getContent());
            showitem.put("mission_states",milist.get(i).getState());
            showitem.put("mission_limit_times", milist.get(i).getDate());
            listitem.add(showitem);
        }

        mission_list_SimpleAdapter = new SimpleAdapter(this, listitem,
        		R.layout.layout_mission_list, new String[]{"mission_names", "mission_contents", "mission_states", "mission_limit_times"}, 
        		new int[]{R.id.mission_name, R.id.mission_content, R.id.mission_state, R.id.mission_limit_time});

        mission_list_ListView.setAdapter(mission_list_SimpleAdapter);
	}
	public void getFromserver() {
		new Thread(){public void run() {
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
	
}

