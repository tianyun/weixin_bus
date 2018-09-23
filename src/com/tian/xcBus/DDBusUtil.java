package com.tian.xcBus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;


public class DDBusUtil {
	private static final Logger LOGGER = Logger.getLogger(DDBusUtil.class);
	private static String COOKIE = "CurAreaCode=xinchang; RequestType=1; Hm_lvt_12c914e7e29b4db18c3abf02f680cff5=1451807960,1451821970,1451912025,1451912031; Hm_lpvt_12c914e7e29b4db18c3abf02f680cff5=1451913010; busMarkHis=1; line_his_xinchang=311%2C3%E8%B7%AF%7C811%2C8%E8%B7%AF; CurAreaCode=xinchang; ASP.NET_SessionId=hxbte4hi23r53ypxygtjj3ai";

	public static String httpUtil(String url) {
		LOGGER.info("正常--####--进入DDBusUtil-》httpUtil--####,传入参数url：" + url);
		try {
			StringBuilder resStr = null;
			String s = url;

			URL urlLink = new URL(s);
			HttpURLConnection resumeConnection = (HttpURLConnection) urlLink.openConnection();
			if (COOKIE == null) {
				LOGGER.error("！！错误 --####--新昌公交，错误信息：没有cooke");
			}
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			resumeConnection.setRequestProperty("Cookie", COOKIE);
			// Headers.Set("x-requested-with", "XMLHttpRequest");
			resumeConnection.setRequestProperty("x-requested-with", "XMLHttpRequest");
			resumeConnection.setRequestProperty("Referer", "http://m.doudou360.com/bus/live.aspx?lid=311");

			resumeConnection.connect();

			InputStream urlStream = resumeConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
			String result = null;
			resStr = new StringBuilder("");
			while ((result = bufferedReader.readLine()) != null) {
				resStr.append(result + "\n");
			}
			result = null;
			bufferedReader.close();
			LOGGER.info("正常--####--出来DDBusUtil-》httpUtil--####");
			// LOGGER.info("正常--####--出来DDBusUtil-》httpUtil--####,返回：" +
			// resStr.toString());
			return resStr.toString();
		} catch (Exception e) {
			LOGGER.error("！！错误--####--新昌公交,busHttpUtil 有异常", e);
		}
		return "0001";
	}

	public static String decode(String s) {
		return null;
	}

	public static void main(String[] args) {
		String url = "http://m.doudou360.com/bus/i/line.ashx?query=飞龙";
		System.out.println(DDBusUtil.httpUtil(url));
	}
}
