package com.tian.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.tian.xcbus.Bus;

public class BusHttpNew {
	private static final Logger LOGGER = Logger.getLogger(Bus.class);

	public static String BusHttp(String url) {
		LOGGER.info("正常--####--进入BusHttpUtil-》BusHttp--####,传入参数url：" + url);
		try {
			StringBuilder resStr = null;
			String s = url;

			URL urlLink = new URL(s);
			HttpURLConnection resumeConnection = (HttpURLConnection) urlLink.openConnection();
			
			resumeConnection.setRequestProperty("User-Agent", "HZBus/1.1.3 (iPhone; iOS 11.2.2; Scale/2.00)");
			resumeConnection.setRequestProperty("Host", "m.ibuscloud.com");
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
			LOGGER.info("正常--####--出来BusHttpUtil-》BusHttp--####,返回参数：" + resStr.toString());
			return resStr.toString();
		} catch (Exception e) {
			LOGGER.error("！！错误--####--新昌公交,busHttpUtil 有异常", e);
		}
		return "0001";
	}

	public static void main(String[] args) {
		String s = "https://m.ibuscloud.com/v2/bus/getNextBusByRouteStopId?uuid=C2BA79C4-0B49-4947-BDED-CA2B5ADEB584&routeId=1004000161&timestamp=1516792591000&userLng=120.903866&deviceId=75f9537ee1ff0238e69b504dfe923c84e8f6e583d5f87e7753ef6573ba19778d&stopId=1004002830&access_id=ptapp&appSource=com.ibuscloud.publictransit&signature=k4zJiGWaFh5ssifbZDuNUtb%2BG6qFye8ApA1d6kIDB7k%3D&userLat=29.499831&city=330624&token=";
		String businfoString = BusHttp(s);
		System.out.println(businfoString);
		System.out.println(System.currentTimeMillis()+"kkk");
		
	}
}
