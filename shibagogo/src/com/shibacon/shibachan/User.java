package com.shibacon.shibachan;

import android.graphics.Bitmap;

public class User {  //用户信息
	private String mailAddr;
	private String password;
	private String image;
	private int []ini= {1,1,1,1};
	public User(String mailaddr,String pass,String imge,int []init) {
		// TODO Auto-generated constructor stub
		mailAddr=mailaddr;
		password=pass;
		image=imge;
		ini[0]=init[0];ini[1]=init[1];ini[2]=init[2];ini[3]=init[3];
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	
	public String getMailAddr() {
		return mailAddr;
	}
	public String getPassword() {
		return password;
	}
	
	public void setImage(String str) {
		this.image=str;
	}
	public String getImage() {
		return image;
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
