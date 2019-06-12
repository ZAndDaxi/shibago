package com.shibacon.shibachan;

import android.graphics.Bitmap;

public class User {  //用户信息
	private String mailAddr;
	private String password;
	private Bitmap image;
	private int []ini= {1,1,1,1};
	public User(String mailaddr,String pass) {
		// TODO Auto-generated constructor stub
		mailAddr=mailaddr;
		password=pass;
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int[] getIni() {
		return ini;
	}
	public void setIni(int i1,int i2,int i3,int i4) {
		this.ini[0]=i1;
		this.ini[1]=i2;		this.ini[2]=i3;		this.ini[3]=i4;		
	}

}
