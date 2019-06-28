package com.shibacon.shibachan;

public class Mission {  //mission列表显示
	private String mission;  
	private int state;  //任务状态（-1失效；0待完成；1已完成）
	//release time
	private String date;

	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
