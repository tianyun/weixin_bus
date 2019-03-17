package com.tian.csbus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tian.util.ErrorInfo;

public class BusFunction {
	private static final Logger LOGGER = Logger.getLogger(BusFunction.class);
	GetBusInfo getBusInfo = new GetBusInfo();

	/**
	 * 传入车站id 和 线路 id 如
	 * 
	 * @param stationid
	 * @param runlineid
	 * @return
	 */
	public String queryCar(String stationid, String runlineid) {
		LOGGER.info("正常--####--进入BusFunction-》queryCar--####,传入参数stationid：" + stationid + " runlineid:" + runlineid);

		String param = "stationid=" + stationid + "&runlineid=" + runlineid + "&carnum=47";
		String url = "http://58.20.113.233:9000/csTravel/car/queryCar";
		String result = getBusInfo.busUtil(url, param);
		if ("[]".equals(result)) {
			return ErrorInfo.INTERFACEERROR.getName();
		} else {
			return result;
		}
	}

	/**
	 * 根据线路名称查询出此线路所有站点名称(分上下行) ，如 立珊专线
	 * 
	 * @param linename
	 * @return
	 */
	public String queryStationByLinename(String linename) {
		LOGGER.info("正常--####--进入BusFunction-》queryStationByLinename--####,传入参数linename：" + linename);

		linename = "linename=" + linename;
		String url = "http://58.20.113.233:9000/csTravel/lineQuery/queryStationByLinename";
		String result = getBusInfo.busUtil(url, linename);
		if ("[]".equals(result)) {
			return ErrorInfo.INTERFACEERROR.getName();
		} else {
			return result;
		}
	}

	/**
	 * 根据站点id去查询路线 如：渔湾市 7072391
	 * 
	 * @param linename
	 * @return
	 */
	public String querySIdByStationname(String stationid) {
		LOGGER.info("正常--####--进入BusFunction-》querySIdByStationname--####,传入参数stationid：" + stationid);

		stationid = "stationid=" + stationid;
		String url = "http://58.20.113.233:9000/csTravel/stationQuery/querySIdByStationname";
		String result = getBusInfo.busUtil(url, stationid);
		if ("[]".equals(result)) {
			return ErrorInfo.INTERFACEERROR.getName();
		} else {
			return result;
		}
	}

	/**
	 * 总的通过路线查询，输入方向， 如 5路 0
	 * 
	 * @param lineName
	 * @param desc
	 * @return
	 */
	public String queryByLine(String lineName, String desc) {
		Pattern pattern = Pattern.compile("[0-9]*");
		lineName = lineName.replace("路", "");
		Matcher isNum = pattern.matcher(lineName);
		if (isNum.matches()) {
			lineName = lineName + "路";
		} else {
			String result = queryStationByLinename(lineName);
			return result;
		}

		// 获取路线
		String result = queryStationByLinename(lineName);
		// 处理
		JSONArray resultArr = JSONArray.parseArray(result);
		JSONArray UPRUNLine = new JSONArray();
		JSONArray DOWNRUNLine = new JSONArray();

		for (int i = 0; i < resultArr.size(); i++) {
			JSONObject tempJsonData = (JSONObject) resultArr.get(i);
			if ("UPRUN".equals(tempJsonData.getString("UPDOWN"))) {
				UPRUNLine.add(tempJsonData);
			} else if ("DOWNRUN".equals(tempJsonData.getString("UPDOWN"))) {
				DOWNRUNLine.add(tempJsonData);
			}
			tempJsonData = null;
		}
		// 获取实时消息
		JSONArray liveLine = new JSONArray();
		if ("0".equals(desc)) {
			liveLine = UPRUNLine;
		} else {
			liveLine = DOWNRUNLine;
		}
		JSONObject lastStationData = (JSONObject) liveLine.get(liveLine.size() - 1);
		result = queryCar(lastStationData.getString("STATIONID"), lastStationData.getString("RUNLINEID"));
		LOGGER.info("最后一个站点信息：" + lastStationData);
		LOGGER.info("获取实时消息: " + result);

		// 数据拼接
		JSONArray liveResultArr = JSONArray.parseArray(result);
		for (int i = liveResultArr.size() - 1; i >= 0; i--) {
			JSONObject tempJsonData = (JSONObject) liveResultArr.get(i);
			int num = Integer.parseInt(tempJsonData.getString("station_num")) - 1;
			JSONObject beData = (JSONObject) liveLine.get(num);
			if (beData.getString("STATIONNAME").contains(">")) {
				continue;
			}
			beData.put("STATIONNAME", ">" + beData.getString("STATIONNAME"));
			liveLine.remove(num);
			liveLine.add(num, beData);
			tempJsonData = null;
		}

		// 打印数据
		String retStr = "";
		for (int i = 0; i < liveLine.size(); i++) {
			JSONObject tempJsonData = (JSONObject) liveLine.get(i);
			if (tempJsonData.getString("STATIONNAME").contains(">")) {
				retStr = retStr + "" + tempJsonData.getString("STATIONNAME") + "\n";
			} else {
				retStr = retStr + "  " + tempJsonData.getString("STATIONNAME") + "\n";
			}
		}
		return retStr;
	}

	public static void main(String[] args) {
		BusFunction busFunction = new BusFunction();

		String retStr = busFunction.queryStationByLinename("旅1");
		LOGGER.info("\n\n-------------------------------------------------------------");
		LOGGER.info(retStr);

	}
}