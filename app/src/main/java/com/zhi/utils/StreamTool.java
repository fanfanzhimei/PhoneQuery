package com.zhi.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamTool {
	
	public static byte[] readStream(InputStream in) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int len = 0;
		while((len = in.read(bytes))!=-1){
			bos.write(bytes, 0, len);
		}
		bos.flush();
		bos.close();
		in.close();
		return bos.toByteArray();
	}
}