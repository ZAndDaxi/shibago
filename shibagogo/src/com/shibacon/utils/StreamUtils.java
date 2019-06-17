package com.shibacon.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	
	public static String readStream(InputStream in) throws Exception {
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		int len=-1;
		byte[] buffer=new byte[1024];
		while((len=in.read(buffer))!=-1) {
			baos.write(buffer, 0, len);
		}
		in.close();
		String content=new String(baos.toByteArray());
		
		return content;
	}
	

}
