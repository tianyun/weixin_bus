package com.tian.csbus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.tian.util.HttpUtil;

public class GetCSBusInfo_Backup {

	/**
	 * 来源于微信上 获取线路的ID
	 * 
	 * @param busLine
	 * @param desc
	 * @return
	 */
	public String getLineInfo(String busLine, String desc) {
		String url;
		String resStr = "";
		try {
			url = "http://58.20.113.233:50448/bus_api/app/queryLine?lineName=" + URLEncoder.encode(busLine, "utf-8");

			String result = HttpUtil.httpGet(url);
			System.out.println(result);
			JSONObject tempJsonData = JSONObject.parseObject(result);
			JSONObject jsonData = JSONObject.parseObject(tempJsonData.getString("data"));
			if ("[]".equals(jsonData.getString("busLine"))) {
				resStr = "query none";
			}else {
				String busLineStr = jsonData.getString("busLine");
				busLineStr=busLineStr.replace("[", "").replace("]", "");
				JSONObject busData = JSONObject.parseObject(busLineStr);
				resStr = busData.getString("LINE_CODE");
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resStr;
	}

	
	
	
	/**
	 * 来源于app
	 * 
	 * @param busLine
	 * @param desc
	 * @return
	 */
	public String getBusInfo(String busLine, String desc) {
		String url;
		String resStr;
		try {
			// 站点名称信息
			url = "http://112.74.229.255/livebus/api/line/getstationsbylinename?uuid=test&name="
					+ URLEncoder.encode(busLine, "utf-8");
			String result = HttpUtil.httpGet(url);
			JSONArray resultArr = JSONArray.parseArray(result);

			JSONArray stationArr0 = new JSONArray();
			JSONArray stationArr1 = new JSONArray();

			// 获取实时信息
			url = "http://112.74.229.255/livebus/api/car/getcar?uuid=test&stationid=7070354&runlineid=1142&carnum=7";

			for (int i = 0; i < resultArr.size(); i++) {
				JSONObject tempJsonData = (JSONObject) resultArr.get(i);
				if (desc.equals(tempJsonData.getString("isup"))) {
					stationArr0.add(tempJsonData);
					resStr = tempJsonData.getString("name");
				} else {

					continue;
				}
				tempJsonData = null;
			}
			System.out.println(result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		GetCSBusInfo_Backup getbuBusInfo = new GetCSBusInfo_Backup();
		System.out.println(getbuBusInfo.getBusInfo("902路", "1"));
	}
}
