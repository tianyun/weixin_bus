package com.tian.xcbus;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 公交车类 包含公交车到站信息，信息处理方法
 * 
 * @author tianyun
 *
 */
public class Bus {
	protected static final Logger LOGGER = Logger.getLogger(Bus.class);

	/**
	 * 传入公交车路线信息，进行处理
	 * 
	 * @param busInfo
	 * @return
	 */
	public static Map<String, String> dealInfo(String busInfo) {
		LOGGER.info("正常--####--进入Bus->dealInfo--####,传入参数busInfo：\n" + busInfo);

		Map<String, String> resMap = new HashMap<String, String>();
		JSONObject jsonData;
		try {
			jsonData = JSON.parseObject(busInfo);
			JSONObject result = (JSONObject) jsonData.get("result");
			if ("{}".equals(result.toString())) {
				resMap.put("code", "0001");
				resMap.put("errmsg", "没有这辆车");

				return resMap;
			}
			// 获得公交车路线，公交车站，到站信息
			String name = result.getString("name");
			String stas = result.getString("stas");
			if ("[]".equals(stas)) {
				resMap.put("code", "0001");
				resMap.put("errmsg", "没有这辆车");

				return resMap;
			}
			String live = result.getString("live");

			// 处理相应的特殊符
			stas = stas.replace("[", "");
			stas = stas.replace("]", "");
			// 切割
			String[] stasArr = stas.split(",");
			String desc = stasArr[0] + " 开往 " + stasArr[stasArr.length - 1];
			// 如果公交车已经没有
			resMap.put("isBusness", "no");

			if (!"[]".equals(live)) {
				resMap.put("isBusness", "ok");

				live = live.replace("],[", "#");
				live = live.replace("]", "");
				live = live.replace("[", "");
				// 切割
				String[] liveArr = live.split("#");

				for (int i = 0; i < liveArr.length; i++) {
					String[] liveinfo = liveArr[i].split(",");
					// System.out.println(liveArr[i]);
					int n = Integer.parseInt(liveinfo[0]);
					int m = Integer.parseInt(liveinfo[1]);

					if (m == 1) {
						stasArr[n - 1] = "=" + stasArr[n - 1];
					} else {
						if ((n + 1) > liveArr.length) {
							stasArr[n] = ">" + stasArr[n];

						} else {
							stasArr[n] = ">" + stasArr[n];

						}
					}

				}
			}

			String resutlStas = "";
			for (int i = 0; i < stasArr.length; i++) {
				stasArr[i] = stasArr[i].replaceAll("\"", "");
				if (!(stasArr[i].contains(">") || stasArr[i].contains("="))) {
					resutlStas = resutlStas + "  " + stasArr[i] + "\n";

				} else {
					resutlStas = resutlStas + stasArr[i] + "\n";

				}
			}
			resMap.put("code", "0000");
			resMap.put("name", name);
			resMap.put("stas", resutlStas);
			resMap.put("desc", desc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resMap.put("code", "0003");
			LOGGER.error("！！错误--####--bus信息查询，json解析错误", e);
		}
		LOGGER.info("正常--####--出来Bus->dealInfo--####,返回resMap：" + resMap);

		return resMap;
	}

	/**
	 * 传入公交车路线，获取实时信息
	 * 
	 * @param busline
	 * @return
	 */
	public static String getBusInfo(String busline, String desc) {
		LOGGER.info("正常--####--进入Bus->getBusInfo--####,传入参数busline：" + busline + ",desc:" + desc);
		if ("1".equals(desc)) {
			busline = busline;
		} else if ("2".equals(desc)) {
			busline = busline.substring(0, busline.length()-1)+"2";
		}
		String resString = "";
		try {
			String url = "http://m.doudou360.com/bus/i/live.ashx?lid=" + busline + "&_=1452005192749";
			resString = DDBusUtil.httpUtil(url);
			LOGGER.info("正常--####--出来Bus->getBusInfo--####,返回resString：" + resString);
			return resString;
		} catch (Exception e) {
			LOGGER.error("！！错误--####--新昌公交:", e);
			return "0001";
		}

	}

	/**
	 * http://m.doudou360.com/bus/i/line.ashx?query=飞龙
	 * 
	 * @param queryParam
	 * @return
	 */
	public static Map<String, String> queryLine(String queryParam) {
		LOGGER.info("正常--####--进入Bus->queryLine--####,传入参数queryParam：" + queryParam);
		String[] con = queryParam.split(" ");
		String desc = "0";
		if (con.length == 2) {
			desc = "2";
		} else {
			desc = "1";
		}
		queryParam = con[0];
		Map<String, String> resMap = new HashMap<String, String>();
		String queryLine = "http://m.doudou360.com/bus/i/line.ashx?query=" + queryParam;
		String resultStr = DDBusUtil.httpUtil(queryLine);
		JSONObject jsonData;
		try {
			jsonData = JSON.parseObject(resultStr);
			JSONArray resultArr = (JSONArray) jsonData.get("result");
			String successStr = jsonData.getString("success");
			if ("[]".equals(resultArr.toString()) && "1".equals(successStr)) {
				resMap.put("code", "0001");
				resMap.put("msg", "没有此线路");
				resMap.put("line", queryParam);

			} else if ("1".equals(successStr)) {
				JSONObject tempJsonData = (JSONObject) resultArr.get(0);
				resMap.put("desc", desc);
				resMap.put("code", "0000");
				resMap.put("id", tempJsonData.getString("id"));
				resMap.put("name", tempJsonData.getString("name"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.info("正常--####--出来Bus->queryLine--####,返回resMap：" + resMap.toString());
		return resMap;
	}

	public static void main(String[] args) {
		String  tt = "123456";
		tt = tt.substring(0,tt.length()-1)+"2";
		System.out.println(tt);
	}
	
	//@Test
	public void testGetBusInfo() {
		String businfoString = Bus.getBusInfo("111","2");
		System.out.println(businfoString);
	}

	//@Test
	public void testQueryLine() {
		Bus.queryLine("2 2");
	}
	
	@Test
	public void testAll() {
		Map<String, String> resMap = Bus.queryLine("2 2");
		if("0001".equals(resMap.get("code"))) {
			System.out.println("nothing");
		}else if("0000".equals(resMap.get("code"))) {
			String busInfo = Bus.getBusInfo(resMap.get("id"), resMap.get("desc"));
			System.out.println("testAll:"+busInfo);
		}
		
	}
	
}
