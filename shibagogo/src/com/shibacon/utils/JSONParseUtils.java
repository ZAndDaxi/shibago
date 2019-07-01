package com.shibacon.utils;

import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shibacon.shibachan.Mission;
import com.shibacon.shibachan.flist;

public class JSONParseUtils {
	public static TreeMap<Integer, flist> getfriendlist(String jsonString){
		TreeMap<Integer, flist> tm=new TreeMap<Integer, flist>();
		try {
			JSONArray jsonarray=new JSONArray(jsonString);
			for(int i=0;i<jsonarray.length();i++) {
				JSONObject jo=jsonarray.getJSONObject(i);
				flist f=new flist();
				f.setRanking(jo.getInt("ranking"));
				f.setName(jo.getString("name"));
				f.setLevel(jo.getInt("level"));
				f.setImagepath(jo.getString("imagepath"));
				tm.put(f.getRanking(), f);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		   
		return tm;
		
	}
	public static ArrayList<Mission> getmissionlist(String jsonS){
		ArrayList<Mission> al=new ArrayList<Mission>();
		try {
			JSONArray jsona=new JSONArray(jsonS);
			for(int i=0;i<jsona.length();i++) {
				JSONObject jo=jsona.getJSONObject(i);
				Mission m=new Mission();
				m.setMission(jo.getString("mission"));
				m.setDate(jo.getString("date"));
				m.setState(jo.getString("state"));
				m.setContent(jo.getString("content"));
				m.setLat(jo.getDouble("latitude"));
				m.setLng(jo.getDouble("longtitude"));
				al.add(m);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return al;
		
	}

}
