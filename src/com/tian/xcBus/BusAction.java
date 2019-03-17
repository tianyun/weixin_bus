package com.tian.xcbus;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.tian.api.Wether;
import com.tian.util.DBUtilMysql;


public class BusAction {
	protected static final Logger LOGGER = Logger.getLogger(BusAction.class);
	
	/**
	 * 传入路线信息，获取实时情况
	 * @param Content
	 * @return
	 */
	public static Map<String, String> getBusWx(String Content) {
		LOGGER.info("正常--####--进入BusAction-》getBusWx--####,传入参数Content：" + Content);

		Map<String, String> resMap = new HashMap<String, String>();
		String resString = "";

		Map<String, String> lineMap = Bus.queryLine(Content);
		if ("0001".equals(lineMap.get("code"))) {
			resString = "路线：" + lineMap.get("line") + "\n实时公交暂不支持此路线";
			resMap.put("code", "0005");
			resMap.put("info", resString);
		} else if ("0000".equals(lineMap.get("code"))) {
			String busInfo = Bus.getBusInfo(lineMap.get("id"), lineMap.get("desc"));
			System.out.println("testAll:" + busInfo);
			Map<String, String> dealInfoMap = Bus.dealInfo(busInfo);
			System.out.println(dealInfoMap.toString());
	
			if (dealInfoMap != null) {
				// 天气
				String wether = Wether.getWetherInfo("新昌").get("wether_info");
				// 路线
				String line = "路线：" + dealInfoMap.get("name");
				// 判断是否运营
				if ("no".equals(dealInfoMap.get("isBusness"))) {
					line = line + "--已下班";
				}
				String[] busTime = Content.split(" ");
				resString = wether + line + "\n方向：" + dealInfoMap.get("desc") + "\n首末班车：" + getBusTime(busTime[0])
						+ "\n“>”表示快到，“=”表示停靠\n\n" + dealInfoMap.get("stas")
						+ "\n\n发送数字+空格+2 查询反向，如3 2\n\n-->查询\n拔茅专线发送：14\n西郊环线发送：16";
				resMap.put("code", "0000");

				resMap.put("info", resString);

			} else {
				resString = "没有这个公交路线\n\n发送数字+空格+2 查询反向，如3 2";
				resMap.put("code", "0005");

				resMap.put("info", resString);
			}
		}
		LOGGER.info("正常--####--出来BusAction-》getBusWx--####,返回resMap：" + resMap.toString());
		return resMap;
	}

	/**
	 * 公交车首末班车时间
	 * @param Num
	 * @return
	 */
	public static String getBusTime(String Num) {
		LOGGER.info("正常--####--进入BusAction-》getBusTime--####,传入参数Num：" + Num);

		try {
			InputStream inStream = DBUtilMysql.class.getResourceAsStream("/bus_time.properties");
			Properties prop = new Properties();
			// 装载并获取数据
			prop.load(inStream);
			return prop.getProperty("bus." + Num + "路");
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("getBusTime error！！", e);
		}

		return null;
	}

	public static void main(String[] args) {
		Map<String, String> resmap = BusAction.getBusWx("8 2");
		System.out.println("main:"+resmap.toString());
		
	}

	@Test
	public void testAll() {
		Map<String, String> resMap = Bus.queryLine("8");
		if ("0001".equals(resMap.get("code"))) {
			System.out.println("nothing");
		} else if ("0000".equals(resMap.get("code"))) {
			String busInfo = Bus.getBusInfo(resMap.get("id"), resMap.get("desc"));
			System.out.println("testAll:" + busInfo);
			Map<String, String> dealInfoMap = Bus.dealInfo(busInfo);
			System.out.println(dealInfoMap.toString());
		}

	}
}
