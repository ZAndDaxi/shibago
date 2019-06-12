package com.shibacon.shibachan;

public class Shiba { //狗信息
	private String name;
	private int level;
	public Shiba(String name) {
		// TODO Auto-generated constructor stub
		this.name=name;
		level=0;
	}
	public int levelup(int l) {
		level+=l;
		return level;
	}

}
