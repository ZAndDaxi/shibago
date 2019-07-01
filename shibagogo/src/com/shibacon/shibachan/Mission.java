package com.shibacon.shibachan;

public class Mission {  //mission列表显示
	private String mission; //任务名字哈，，简介，，需要控制紫薯的那个？？？？？等，，我跟菁笛说说？？？
	
	private String state;  //任务状态（"Expired"失效; "Processing"待完成; "completed"已完成）
	//release time
	private String date;
	private double lat;
	private double lng;

	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public String getState() {
		return state;
	}
	public void setState(String i) {
		this.state = i;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
