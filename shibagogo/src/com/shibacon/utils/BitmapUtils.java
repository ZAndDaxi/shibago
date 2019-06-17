package com.shibacon.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Base64;

public class BitmapUtils {

	public static String convertIconToString(Bitmap bitmap) {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		int options=100;
		while(baos.toByteArray().length/1024 >1000) {
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options-=10;
		}
		byte[] bytes=baos.toByteArray();
		return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
}
