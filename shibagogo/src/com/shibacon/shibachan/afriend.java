package com.shibacon.shibachan;

import com.shibacon.shibagogo.Shibaapp;

public class afriend {
	private String friendname;
	private String mytoken;
	public String getFriendname() {
		return friendname;
	}
	public void setFriendname(String friendname) {
		this.friendname = friendname;
	}
	public String getMytoken() {
		return Shibaapp.token;
	}
	public void setMytoken() {
	//	this.mytoken = Shibaapp.token;
	}
	public afriend(String friendname, String mytoken) {
		super();
		this.friendname = friendname;
		this.mytoken = mytoken;
	}	
	

}
