package com.tian.cqbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tian.util.BusHttpUtil;


/**
 * 
 * @author Administrator 公交车查询地点和路线
 */
public class CQBusAction {

	protected static final Logger LOGGER = Logger.getLogger(CQBusAction.class);
	protected String queryUrl = "http://222.180.195.122:9991/index.php?c=bus&m=search&key=57424345324b564664525474734e466c686e3444467a73676b78336e45714d755a456a797a465267527937457945726f39625253374a72506d5a586c206873526f667a4e46665748574e45346b4c3476352f64486e6e79326759482f6353447341&keywords=parm";
	protected String busLine = "http://222.180.195.122:9991/index.php?c=bus&m=getLineStation&LineID=LineIDParam&lineNumber=LineNumberParam&FBackSign=FBackSignParam&key=57424345324b564664525474734e466c686e3444463165556c2f69666b426b5066636444684a7249334348457945726f39625253374a72506d5a586c206873526f667a4e46665748574e45346b4c3476352f64486e6e79326759482f6353447341";
	protected String stationInfo = "http://222.180.195.122:9991/index.php?c=bus&m=arrivleBus&stationName=stationNameParam&LineID=LineIDParam&lineNumber=lineNumberParam&FBackSign=FBackSignParam&LNodeId=LNodeIdParam&key=57424345324b564664525474734e466c686e3444467844756e4667412b4f72704c663433797650496d796e457945726f39625253374a72506d5a586c206873526f667a4e46665748574e45346b4c3476352f64486e6e79326759482f6353447341";

	/**
	 * 查询路线，返回查询结果 url:
	 * http://222.180.195.122:9991/index.php?c=bus&m=search&keywords=211&key=574
	 * 24345324b564664525474734e466c686e3444467a73676b78336e45714d755a456a797a4652675
	 * 27937457945726f39625253374a72506d5a586c206873526f667a4e46665748574e45346b4c347
	 * 6352f64486e6e79326759482f6353447341
	 * 
	 * @param parm
	 * @return
	 */
	public Map<String, String> queryLine(String parm, String FBackSignParam) {
		LOGGER.info("正常--####--进入CQBusAction-》queryLine--####,传入参数parm：" + parm + ", FBackSignParam:" + FBackSignParam);
		Map<String, String> resMap = new HashMap<String, String>();
		String queryLineUrl = queryUrl.replace("parm", parm);
		String result = BusHttpUtil.BusHttp(queryLineUrl);
		System.out.println(result.toString());
		JSONObject jsonData = new JSONObject();
		JSONArray routesJsonArrData = new JSONArray();
		JSONObject objJsonData = new JSONObject();
		try {
			jsonData = JSON.parseObject(result);
			objJsonData = (JSONObject) jsonData.get("content");
			// 判断是查询路线还是站点

			if (objJsonData.keySet().contains("lines")) {
				routesJsonArrData = (JSONArray) objJsonData.get("lines");
				System.out.println(routesJsonArrData.size());
				if (routesJsonArrData.size() >= 1 && "1".equals(FBackSignParam)) {
					routesJsonArrData = (JSONArray) routesJsonArrData.get(0);
					objJsonData = (JSONObject) routesJsonArrData.get(0);

				} else if (routesJsonArrData.size() >= 1 && "0".equals(FBackSignParam)) {
					routesJsonArrData = (JSONArray) routesJsonArrData.get(0);
					objJsonData = (JSONObject) routesJsonArrData.get(1);

				}
				// 提取信息 ， 这里获取终点站的时候0和1正好 相反

				String LineID = objJsonData.getString("LineID");
				String LineNumber = objJsonData.getString("LineNumber");
				String StartingStation = objJsonData.getString("StartingStation");
				String EndStation = objJsonData.getString("EndStation");
				String FBackSign = objJsonData.getString("FBackSign");
				String startTime = objJsonData.getString("startTime");
				String endTime = objJsonData.getString("endTime");
				String price = objJsonData.getString("price");
				String LineName = objJsonData.getString("LineName");

				// 放入map中
				resMap.put("code", "0000");
				resMap.put("startTime", startTime);
				resMap.put("endTime", endTime);
				resMap.put("price", price);
				resMap.put("LineName", LineName);
				resMap.put("LineID", LineID);
				resMap.put("LineNumber", LineNumber);
				resMap.put("code", "0000");
				resMap.put("StartingStation", StartingStation);
				resMap.put("EndStation", EndStation);
				resMap.put("FBackSign", FBackSign);

				LOGGER.info("正常--####--出来CQBusAction-》queryLine--####,返回参数：" + resMap.toString());
				return resMap;
			} else {
				resMap.put("code", "0001");
				resMap.put("msg", "没有该路线");

				LOGGER.info("正常--####--出来CQBusAction-》queryLine--####,返回参数：" + resMap.toString());
				return resMap;
				// routesJsonArrData = (JSONArray) objJsonData.get("stations");
			}

		} catch (Exception e) {
			resMap.put("code", "0003");
			resMap.put("msg", "没有该路线");
			LOGGER.error("error，json处理有问题" + resMap.toString(), e);
			return resMap;
		}

	}

	/**
	 * 传入公交车路线，查看相关信息 LineID 路线id lineNumber 路线名称 FBackSign 方向
	 * 
	 * @param LineID
	 * @return
	 */
	public Map<String, String> busInfo(String LineIDParam, String LineNumberParam, String FBackSignParam) {
		LOGGER.info("正常--####--进入CQBusAction-》busInfo--####,传入参数LineIDParam:" + LineIDParam + "; LineNumberParam:"
				+ LineNumberParam + "; FBackSignParam:" + FBackSignParam);

		Map<String, String> resMap = new HashMap<String, String>();
		String busLineUrl = busLine.replace("LineIDParam", LineIDParam).replace("LineNumberParam", LineNumberParam)
				.replace("FBackSignParam", FBackSignParam);
		String result = BusHttpUtil.BusHttp(busLineUrl);
		JSONObject jsonData;
		JSONObject objJsonData;
		JSONArray lineStationJsonArrData = new JSONArray();

		try {
			jsonData = JSON.parseObject(result);
			objJsonData = (JSONObject) jsonData.get("content");
			lineStationJsonArrData = (JSONArray) objJsonData.get("lineStation");

			JSONObject jsonTempData;
			Map<String, String> tempMap = new HashMap<String, String>();
			String tempStr = "";
			List<Map<String, String>> liveList = new ArrayList<Map<String, String>>();

			for (int i = lineStationJsonArrData.size() - 1; i >= 0; i--) {
				jsonTempData = (JSONObject) lineStationJsonArrData.get(i);
				System.out.println("i= " + i + "  StationName: " + jsonTempData.get("StationName"));
				tempMap = this.stationInfo(jsonTempData.get("StationName") + "", jsonTempData.get("LNodeId") + "",
						LineIDParam, LineNumberParam, FBackSignParam);
				if (tempMap != null && "0000".equals(tempMap.get("code"))) {
					tempStr = tempMap.get("endStation");
					liveList.add(tempMap);

					// 开始拼接实时信息
					if (tempStr.equals(jsonTempData.get("StationName"))) {
						// 拼接车辆到站信息

						if ("即将进站".equals(tempMap.get("State")) || "正在进站".equals(tempMap.get("State"))) {
							tempStr = "=" + tempStr;
						} else {
							tempStr = ">" + tempStr + tempMap.get("State").replace("距离1站", "");
						}
						((JSONObject) lineStationJsonArrData.get(i)).put("StationName", tempStr);
					} else {
						for (int j = i; j >= 0; j--) {
							if (tempStr.equals(((JSONObject) lineStationJsonArrData.get(j)).getString("StationName"))) {
								i = j + 1;
								break;
							} else {
								continue;
							}
						}
					}
					tempStr = null;
				}
			}

			System.out.println(lineStationJsonArrData.size());
			LOGGER.info("lineStationJsonArrData: " + lineStationJsonArrData);
			String stationLive = "";
			for (int i = 0; i < lineStationJsonArrData.size(); i++) {
				if ((((JSONObject) lineStationJsonArrData.get(i)).getString("StationName")).indexOf(">") == -1
						&& (((JSONObject) lineStationJsonArrData.get(i)).getString("StationName")).indexOf("=") == -1) {
					stationLive = stationLive + "   "
							+ ((JSONObject) lineStationJsonArrData.get(i)).getString("StationName") + " \n";
				} else {
					stationLive = stationLive + ((JSONObject) lineStationJsonArrData.get(i)).getString("StationName")
							+ " \n";
				}
			}
			resMap.put("stationLive", stationLive);
			resMap.put("code", "0000");
			// LOGGER.info("stationLive: " + stationLive);
			return resMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询站点的实时情况
	 * 
	 * @param stationNameParam
	 * @param LNodeIdParam
	 * @param lineIDParam
	 * @param lineNumberParm
	 * @return
	 */
	public Map<String, String> stationInfo(String stationNameParam, String LNodeIdParam, String LineIDParam,
			String lineNumberParam, String FBackSignParam) {
		LOGGER.info("正常--####--进入CQBusAction-》stationInfo--####,传入参数stationNameParam:" + stationNameParam
				+ "; LNodeIdParam:" + LNodeIdParam + "; LineIDParam:" + LineIDParam + "; lineNumberParam:"
				+ lineNumberParam + "; FBackSignParam:" + FBackSignParam);

		Map<String, String> resMap = new HashMap<String, String>();
		String stationInfoUrl = stationInfo.replace("stationNameParam", stationNameParam)
				.replace("LNodeIdParam", LNodeIdParam).replace("LineIDParam", LineIDParam)
				.replace("lineNumberParam", lineNumberParam).replace("FBackSignParam", FBackSignParam);
		String result = BusHttpUtil.BusHttp(stationInfoUrl);
		JSONObject jsonData = new JSONObject();
		JSONObject objJsonData = new JSONObject();
		JSONArray objBusLiveStationArr = new JSONArray();

		try {
			jsonData = JSON.parseObject(result);
			;
			objJsonData = (JSONObject) jsonData.get("content");
			if (objJsonData.keySet().contains("busLocation")) {
				objBusLiveStationArr = (JSONArray) objJsonData.get("busLocation");
				if (objBusLiveStationArr.size() > 0) {
					resMap.put("code", "0000");
					resMap.put("queryStation", stationNameParam);
					resMap.put("State", ((JSONObject) objBusLiveStationArr.get(0)).getString("State"));
					resMap.put("startStation", ((JSONObject) objBusLiveStationArr.get(0)).getString("startStation"));
					resMap.put("endStation", ((JSONObject) objBusLiveStationArr.get(0)).getString("endStation"));
					LOGGER.info("正常--####--出来CQBusAction-》stationInfo--####,返回参数：" + resMap.toString() + "\n");
					return resMap;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testQueryLine() {
		queryLine("211", "0");
	}
	
	@Test
	public void testBusInfo() {
		busInfo("1001311140002082", "211", "0");
	}
	
}


