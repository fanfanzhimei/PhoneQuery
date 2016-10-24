package com.zhi.service;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.zhi.utils.StreamTool;
import org.xmlpull.v1.XmlPullParser;

public class QueryService {

	public static String query(Context context, String phoneNumber) throws Exception{
		AssetManager assetManager = context.getAssets();
		InputStream in = assetManager.open("asop12.xml");
		byte[] data = StreamTool.readStream(in);
		String value = new String(data);
		value = value.replaceAll("\\$mobile", phoneNumber);  // 替换占位符
		data = value.getBytes();

		String path = "http://ws.webxml.com.cn//WebServices/MobileCodeWS.asmx";  // 要请求的webxml的地址（看API）
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setReadTimeout(5000);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		conn.setRequestProperty("Content-Length", data.length + "");
		OutputStream os = conn.getOutputStream();
		os.write(data);  // 把获取到的xml数据写给服务器
		int state = conn.getResponseCode();
		if(state == 200){  // 写给服务器成功，要得到返回的xml数据
			InputStream inputStream = conn.getInputStream();
			return xmlParse(inputStream);
		}
		return null;
	}

	private static String xmlParse(InputStream inputStream) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		int event = parser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT){
			switch(event){
			case XmlPullParser.START_TAG:
				if("getMobileCodeInfoResult".equals(parser.getName())){
					return parser.nextText();
				}
				break;
			}
			event = parser.next();
		}
		return null;
	}
}