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
 * 用于查地点
 * 
 * @author Administrator
 *
 */
public class CQBusQueryPlace {

	protected static final Logger LOGGER = Logger
			.getLogger(CQBusQueryPlace.class);

	protected String stationInfo = "http://222.180.195.122:9991/index.php?c=bus&m=StationSearch&stationName=stationNameParam&userLng=112.952339&userLat=28.180733&key=57424345324b564664525474734e466c686e344446776f71667151634949664938554a4e3836374a5a6344457945726f39625253374a72506d5a586c206873526f667a4e46665748574e45346b4c3476352f64486e6e79326759482f6353447341";
	protected String queryStationInfo = "http://222.180.195.122:9991/index.php?c=bus&m=LineSelect&stationName=stationNameParam&FBackSign=FBackSignParam&stationLng=stationLngParam&stationLat=stationLatParam&key=57424345324b564664525474734e466c686e34444637566664324e497a52324c716f7153784a4c6e35507a457945726f39625253374a72506d5a586c206873526f667a4e46665748574e45346b4c3476352f64486e6e79326759482f6353447341";

	public boolean isJson(String content) {

		try {
			JSONObject jsonStr = JSONObject.parseObject(content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 传入地址名称去寻找
	 * 
	 * @param placeParam
	 * @return
	 */
	public Map<String, Object> queryPlace(String stationNameParam,String FBackSignParam) {
		LOGGER.info("正常--####--进入CQBusQueryPlace-》queryPlace--####,传入参数stationNameParam："
				+ stationNameParam + "FBackSignParam: " + FBackSignParam);

		String queryUrl = stationInfo.replace("stationNameParam",
				stationNameParam);
		String result = BusHttpUtil.BusHttp(queryUrl);
		Map<String, Object> resMap = new HashMap<String, Object>();

		if (false == this.isJson(result)) {
			resMap.put("StationName", stationNameParam);
			resMap.put("code", "0001");
			resMap.put("msg", "没有该站点");
			LOGGER.info("正常--####--出来CQBusQueryPlace-》queryPlace--####,没有匹配到站点信息，返回参数："
					+ resMap.toString());
			return resMap;
		}
		JSONObject jsonData = new JSONObject();
		JSONArray stationJsonArrData = new JSONArray();
		JSONObject objJsonData = new JSONObject();

		try {
			jsonData = JSON.parseObject(result);
			stationJsonArrData = (JSONArray) jsonData.get("content");
			JSONArray stationOverTempArr = new JSONArray();

			System.out.println("stationJsonArrData size:  "
					+ stationJsonArrData.size());
			int isMatch = 0;

			for (int i = 0; i < stationJsonArrData.size(); i++) {
				System.out.println("StationName:"
						+ ((JSONObject) stationJsonArrData.get(i))
								.getString("StationName"));
				System.out.println("data:"
						+ ((JSONObject) stationJsonArrData.get(i)).toString()
						+ "\n\n");

				// 如果匹配到就停止搜索
				if (stationNameParam.equals(((JSONObject) stationJsonArrData.get(i)).getString("StationName"))
						&& FBackSignParam.equals(((JSONObject) stationJsonArrData.get(i)).getString("FBackSign"))) {
					isMatch = 1;
					System.out.println("match");
					break;
				} 
				
			}
			List<Station> stationList = new ArrayList<Station>();
			if(1==isMatch){
				resMap.put("code", "0000");
				resMap.put("stationName", stationNameParam);
				for (int i = 0; i < stationJsonArrData.size(); i++) {
					if (stationNameParam.equals(((JSONObject) stationJsonArrData.get(i)).getString("StationName"))
							&& FBackSignParam.equals(((JSONObject) stationJsonArrData.get(i)).getString("FBackSign"))) {
						System.out.println("第"+i+"次匹配成功");
						objJsonData = ((JSONObject) stationJsonArrData.get(i));
						Station stationTemp = new Station();
						// 开始获取信息
						System.out.println("LNodeId："+objJsonData.getString("LNodeId"));
						stationTemp.setFBackSign(objJsonData.getString("FBackSign"));
						stationTemp.setStationName(objJsonData.getString("StationName"));
						stationTemp.setLNodeId(objJsonData.getString("LNodeId"));
						stationTemp.setMinlongitude(objJsonData.getString("Minlongitude"));
						stationTemp.setMinlatitude(objJsonData.getString("Minlatitude"));
						stationList.add(stationTemp);
					}
				}
				resMap.put("stationList", stationList);
				LOGGER.info("正常--####--出来CQBusQueryPlace-》queryPlace--####,唯一匹配到公交车站点返回参数："
						+ resMap.toString());
			}else{
				// 存在多个相似站点信息
				String stationStr = "";
				String stationTemp = "";
				for (int i = 0; i < stationJsonArrData.size(); i++) {
					if (!stationTemp.equals(((JSONObject) stationJsonArrData.get(i)).getString("StationName"))&&!stationNameParam.equals(((JSONObject) stationJsonArrData.get(i)).getString("StationName"))) {
						stationStr = stationStr + ((JSONObject) stationJsonArrData.get(i)).getString("StationName")+"\n";
											}
				}
				resMap.put("StationName", stationNameParam);
				if("".equals(stationStr)){
					resMap.put("code", "0001");
					resMap.put("msg", "没有该占点");
				}else{
					resMap.put("code", "0002");
					resMap.put("msg", "多于一个站点");
					resMap.put("stationStr", stationStr);
				}
				LOGGER.info("正常--####--出来CQBusQueryPlace-》queryPlace--####,匹配到多个公交车站点返回参数："
						+ resMap.toString());
			
			}
			return resMap;

		} catch (Exception e) {
			resMap.put("code", "0003");
			resMap.put("msg", result);
			LOGGER.error(
					"！！错误--####--CQBusQueryPlace-》queryPlace 没有该站点或者解析错误，", e);
			return resMap;
		}

	}

	/**
	 * 传入站点相关信息和方向，查询所有路线的信息
	 * 
	 * @param stationNameParam
	 * @param FBackSignParam
	 * @param stationLngParam
	 * @param stationLatParam
	 * @return
	 */
	public Map<String, String> queryStationInfo(Station stationParam) {
		LOGGER.info("正常--####--进入CQBusQueryPlace-》queryStationInfo--####,传入参数stationNameParam："
				+ stationParam.getStationName()
				+ ",FBackSignParam: "
				+  stationParam.getFBackSign()
				+ ",stationLngParam:"
				+  stationParam.getMinlongitude()
				+ ",stationLatParam"
				+  stationParam.getMinlatitude());
		Map<String, String> resMap = new HashMap<String, String>();

		String queryStationUrl = queryStationInfo
				.replace("stationNameParam", stationParam.getStationName())
				.replace("FBackSignParam", stationParam.getFBackSign())
				.replace("stationLngParam", stationParam.getMinlongitude())
				.replace("stationLatParam",  stationParam.getMinlatitude());
		String result = BusHttpUtil.BusHttp(queryStationUrl);

		JSONObject jsonData = new JSONObject();
		JSONArray stationJsonArrData = new JSONArray();
		JSONObject objJsonData = new JSONObject();
		try {
			jsonData = JSON.parseObject(result);
			stationJsonArrData = (JSONArray) jsonData.get("content");

			String stationLiveStr = "";
			for (int i = 0; i < stationJsonArrData.size(); i++) {
				stationLiveStr = stationLiveStr
						+ ((JSONObject) stationJsonArrData.get(i))
								.getString("LineName")
						+ "路:"
						+ ((JSONObject) stationJsonArrData.get(i))
								.getString("State")
						+ "\n方向："
						+ ((JSONObject) stationJsonArrData.get(i))
								.getString("StartingStation")
						+ "->"
						+ ((JSONObject) stationJsonArrData.get(i))
								.getString("EndStation") + "\n";
			}
			resMap.put("stationLiveStr", stationLiveStr);
			resMap.put("stationName", stationParam.getStationName());
			LOGGER.info("stationStr:" + resMap.toString());
			return resMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testQueryPlace() {
		CQBusQueryPlace cqBusQueryPlace = new CQBusQueryPlace();
		Station station = new Station();
		station.setStationName("重庆图书馆");
		station.setFBackSign("0");
		Map<String, Object> resMap = cqBusQueryPlace.queryPlace("重asdfasdf庆图书馆","1");
	}

	@Test
	public void testQueryStationInfo() {
		CQBusQueryPlace cqBusQueryPlace = new CQBusQueryPlace();
		
		Map<String, Object> resMap = cqBusQueryPlace.queryPlace("重庆","0");
		if ("0001".equals(resMap.get("code"))) {
			System.out.println("none");
		} else if ("0002".equals(resMap.get("code"))) {
			System.out.println(resMap.toString());
		} else {
			List<Station> stationList = (List<Station>) resMap.get("stationList");
			int i=0;
			String stationStr = "";
			for (Station station : stationList) {
				Map<String, String> stationMap = cqBusQueryPlace.queryStationInfo(station);
				stationStr = stationStr+""+stationMap.get("stationLiveStr");
			}
			System.out.println("stationStr:\n"+stationStr);
		}
	}

	public static void main(String[] args) {
		JSONObject json = JSONObject.parseObject("待解析的json字符串");
		System.out.println(JSONObject.toJSONString(json, true));
	}
}
