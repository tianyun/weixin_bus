package com.tian.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class BusTest {

	public static void main(String[] args) {
		
		String busline ="";
		String buscookie =""; 
		try {
			Date date = new Date();
//			String s = "http://m.doudou360.com/bus/i/live.ashx?lid=311&_=1451827667970";
			String  s = "http://chedaona.cn/route/wap/index";
			// 重新打开一个连接
			URL url = new URL(s);
			HttpURLConnection resumeConnection = (HttpURLConnection) url.openConnection();
			// String cookieVal =
			// "RequestType=1; CurAreaCode=xinchang; Hm_lvt_12c914e7e29b4db18c3abf02f680cff5=1438993053; Hm_lpvt_12c914e7e29b4db18c3abf02f680cff5=1438993103; ASP.NET_SessionId=orm4telvieyeum4a0wd4pgsb";
			if (buscookie == null) {
				System.out.println("！！错误 --####--新昌公交，错误信息：没有cooke");
			}
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
//			resumeConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//			Headers.Set("x-requested-with", "XMLHttpRequest");
			resumeConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
//			resumeConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			resumeConnection.setRequestProperty("Cache-Control", "no-cache");
			resumeConnection.setRequestProperty("Cookie", "ca_cityCode=1301; CNZZDATA1258194733=175695929-1460035907-http%253A%252F%252Fwww.chedaona.cn%252F%7C1460035907");
			resumeConnection.setRequestProperty("Host", "chedaona.cn");
			resumeConnection.setRequestProperty("Pragma", "no-cache");
			resumeConnection.setRequestProperty("Proxy-Connection", "keep-alive");
			resumeConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			resumeConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 8_4_1 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12H321 MicroMessenger/6.3.8 wxdebugger/0.1.0 Language/zh_CN webviewId/0");
			resumeConnection.setRequestProperty("X-DevTools-Emulate-Network-Conditions-Client-Id", "25C536ED-ED0B-4EE9-8496-501626F99C77");
			resumeConnection.setRequestProperty("Referer", "http://chedaona.cn/route/wap/index");
//			resumeConnection.setRequestProperty("x-requested-with", "XMLHttpRequest");

			resumeConnection.connect();
			InputStream urlStream = resumeConnection.getInputStream();
            String encoding = resumeConnection.getContentEncoding();
            InputStream is2 = null;
            //解压字段
			if (encoding != null && encoding.contains("gzip")) {
                is2 = new GZIPInputStream(urlStream);
            } else if (encoding != null && encoding.contains("deflate")) {
                is2 = new InflaterInputStream(urlStream);
            } else {
                is2 = urlStream;
            }
			//获取流
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(is2, "utf-8"));
			String result = null;
			String resString = "";
			while ((result = bufferedReader.readLine()) != null) {
				System.out.println(result);
				resString = resString + result+"\n";
			}

			bufferedReader.close();
//			 System.out.println("正常--####--新昌公交，返回信息："+resString);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
