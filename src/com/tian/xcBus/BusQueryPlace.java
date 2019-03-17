package com.tian.xcbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 查询地点
 * 
 * @author Administrator 2018.4.24
 */
public class BusQueryPlace {
	protected static final Logger LOGGER = Logger.getLogger(BusQueryPlace.class);

	/**
	 * 传入地点，返回一个或两个值
	 * 
	 * @param param
	 * @return
	 */
	public Map<String, String> queryPlace(String param) {
		LOGGER.info("正常--####--进入BusQueryPlace-》queryPlace--####,传入参数param：" + param);
		Map<String, String> resMap = new HashMap<String, String>();
		String url = "http://m.doudou360.com/bus/i/station.ashx?query=" + param;
		String resultStr = DDBusUtil.httpUtil(url);
		System.out.println(resultStr);

		JSONObject jsonData;
		try {
			jsonData = JSON.parseObject(resultStr);
			JSONArray resultArr = (JSONArray) jsonData.get("result");
			if("{}".equals(resultArr.toString())) {
				resMap.put("code", "0001");
				resMap.put("msg", "没找到该站点");
				return resMap;
			}
			
			if (resultArr.size() == 1) {
				resMap.put("code", "0000");
				resMap.put("id", ((JSONObject) resultArr.get(0)).getString("id"));
				resMap.put("name", ((JSONObject) resultArr.get(0)).getString("name"));

			} else {
				resMap.put("code", "00001");
				String resMsg = "";
				for (int i = 0; i < resultArr.size(); i++) {
					resMsg = resMsg + ((JSONObject) resultArr.get(0)).getString("name") + " ";
				}
				resMap.put("station", resMsg);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.info("正常--####--出来BusQueryPlace-》queryPlace--####,传入参数resMap：" + resMap.toString());
		return resMap;
	}

	/**
	 * 传入车站的id，反馈到站情况 url=http://m.doudou360.com/bus/station.aspx?id=3772
	 * 
	 * @param stationId
	 * @return
	 */
	public Map<String, String> queryStationInfo(String stationId) {
		LOGGER.info("正常--####--进入BusQueryPlace-》queryStationInfo--####,传入参数stationId：" + stationId);
		Map<String, String> resMap = new HashMap<String, String>();
		// 获取查询结果，并解析
		String url = "http://m.doudou360.com/bus/station.aspx?id=" + stationId;
		String resultStr = DDBusUtil.httpUtil(url);
		Document doc = Jsoup.parse(resultStr);
		Elements elementStation = doc.getElementsByClass("station");
		Elements elementInfo = doc.getElementsByClass("info");
		Elements elementTime = doc.getElementsByClass("time");
		Elements elementStationName = doc.getElementsByTag("h1");
		String stationName = elementStationName.get(0).text();
		
		StringBuilder tempSB = new StringBuilder();
		for (int i = 0; i < elementStation.size(); i++) {
			tempSB.append((elementStation.get(i).text()).replace(elementTime.get(i).text(), ""));
		}
		
		resMap.put("stationName", stationName);
		resMap.put("lineInfo", tempSB.toString());
		tempSB = null;
		System.out.println("element:" + elementStation.size());
		LOGGER.info("正常--####--出来BusQueryPlace-》queryStationInfo--####,传入参数resMap：" + resMap.toString());
		return resMap;
	}

	/**
	 * 传入要查询 的地点，返回靠近的站点或者"0001"
	 * 
	 * @param stationParam
	 * @param lineParam
	 * @return
	 */
	public Map<String, String> judgeLive(String liveParam, String lineParam, String stationName) {
		LOGGER.info("正常--####--进入BusQueryPlace-》judgeLive--####,传入参数liveParam：" + liveParam + "stationName:"
				+ stationName + "\nlineParam:" + lineParam);
		Map<String, String> resMap = new HashMap<String, String>();
		// 处理相应的特殊符
		lineParam = lineParam.replace("[", "");
		lineParam = lineParam.replace("]", "");
		// 切割
		String[] stasArr = lineParam.split(",");
		String desc = "开往 " + stasArr[stasArr.length - 1];
		resMap.put("desc", desc);
		String reStr = "";
		if (!"[]".equals(liveParam)) {

			liveParam = liveParam.replace("],[", "#");
			liveParam = liveParam.replace("]", "");
			liveParam = liveParam.replace("[", "");
			// 切割
			String[] liveArr = liveParam.split("#");
			System.out.println("liveArr length:" + liveArr.length);
			System.out.println("stasArr length:" + stasArr.length);
			for (int i = 0; i < liveArr.length; i++) {
				String[] liveinfo = liveArr[i].split(",");
				int n = Integer.parseInt(liveinfo[0]);
				int m = Integer.parseInt(liveinfo[1]);

				if (stationName.equals(stasArr[n])) {
					reStr = reStr + stasArr[n] + " ";
				} else if (stationName.equals(stasArr[n + 1])) {
					reStr = reStr + stasArr[n] + " ";

				}

			}

		}
		resMap.put("info", reStr);
		LOGGER.info("正常--####--出来BusQueryPlace-》judgeLive--####,输出resMap：" + resMap.toString());

		return resMap;
	}

	//@Test
	public void testQueryStationInfo() {
		Map<String, String> resMap = queryStationInfo("5830");
		System.out.println("queryStationInfo:" + resMap.toString());
	}

	 @Test
	public void testQueryPlace() {
		Map<String, String> resMap = queryPlace("人民医院");
		System.out.println("testQueryPlace:" + resMap.toString());
	}
}
