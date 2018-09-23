package com.tian.servlet;

import api.BusInfo;

import com.tian.cqbus.CQBusAction;
import com.tian.cqbus.CQBusQueryPlace;
import com.tian.cqbus.Station;
import com.tian.util.CheckUtil;
import com.tian.util.MemCachedUtil;
import com.tian.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CQBusApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(CQBusApi.class);
	protected static String signature = null;
	protected static String timestamp = null;
	protected static String nonce = null;
	protected static String echostr = null;
	protected static String Content = null;
	protected static String ToUserName = null;
	protected static String FromUserName = null;
	protected static String methodType = null;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		signature = req.getParameter("signature");
		timestamp = req.getParameter("timestamp");
		nonce = req.getParameter("nonce");
		echostr = req.getParameter("echostr");
		LOGGER.info("正常--####--传入校验参数：" + req.toString());

		PrintWriter out = resp.getWriter();
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		doPost(req, resp);
		out.close();
		out = null;
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		PrintWriter out = resp.getWriter();
		String message = null;

		MemCachedUtil memCachedUtil = new MemCachedUtil();
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			Content = (String) map.get("Content");
			ToUserName = (String) map.get("ToUserName");
			FromUserName = (String) map.get("FromUserName");
			methodType = (String) map.get("MsgType");
			LOGGER.info("正常--####--进入-》BusApi-》dopost入口程序，传入参数map：" + map.toString());

			String resString = "";
			if ("text".equals(methodType)) {
				String[] con = Content.split(" ");
				CQBusAction cq = new CQBusAction();
				Map<String, String> resLineMap = new HashMap<String, String>();
				if (con.length == 1) {
					resLineMap = cq.queryLine(con[0], "0");
				} else {
					resLineMap = cq.queryLine(con[0], con[1]);

				}
				if ("0000".equals(resLineMap.get("code"))) {
					Map<String, String> liveLineMap = cq.busInfo(resLineMap.get("LineID"), resLineMap.get("LineNumber"),
							resLineMap.get("FBackSign"));
					resString = "路线：" + resLineMap.get("LineName") + "\n" + "方向：" + resLineMap.get("StartingStation") + "开往"
							+ resLineMap.get("EndStation") + "\n" + "时间："
							+ (resLineMap.get("startTime") + "--" + resLineMap.get("endTime")) + "\n\n"
							+ liveLineMap.get("stationLive") + "\n\n发送路线+空格+1 查询反向\n如211 1";
				} else {
					// 去查路线
					CQBusQueryPlace cqBusQueryPlace = new CQBusQueryPlace();
					Map<String, Object> resStationMap = new HashMap<String, Object>();

					if (con.length == 1) {
						resStationMap = cqBusQueryPlace.queryPlace(con[0], "0");
					} else {
						resStationMap = cqBusQueryPlace.queryPlace(con[0], con[1]);
					}

					if ("0000".equals(resStationMap.get("code"))) {
						List<Station> stationList = (List<Station>) resStationMap.get("stationList");
						String stationStr = "";
						for (Station station : stationList) {
							Map<String, String> stationMap = cqBusQueryPlace.queryStationInfo(station);
							stationStr = stationStr+""+stationMap.get("stationLiveStr");
						}
						
						
						resString = "查询地点：" + resStationMap.get("stationName") + "\n\n"
								+ stationStr + "\n\n发送站点+空格+1 查询反向站点\n如重庆图书馆 1+\n发送站点+空格+0 查询正向站点\n如重庆图书馆 0";
					} else if ("0002".equals(resStationMap.get("code"))) {
						resString = "查询地点：" + resStationMap.get("StationName") + "\n有如下地点：\n\n" + resStationMap.get("stationStr")
								+ "\n请输入完整站点信息";
						memCachedUtil.setMemCached(FromUserName+"station", Content,
								new Date(System.currentTimeMillis() + 300000L));
					} else {
						resString = "没有这条路线或者站点，请输入准确信息";
					}
				}

			} else if ("event".equals(methodType)) {
				if ("subscribe".equals(map.get("Event"))) {
					resString = "欢迎关注新昌无线公交！\n\n\n1，发送准确路线查询，如3路或3查询，工业园区线查询该路线，空格+2查询反向信息如“3 2”\n2，发送地点查询地点路线经过情况，如“人民医院”\n3，地点与地点间查询正在开发中";
				} else if ("CLICK".equals(map.get("Event"))) {
					String memCachedStr = memCachedUtil.getMemCached(FromUserName);
					if ("flush".equals(map.get("EventKey"))) {
						if (("0001".equals(memCachedStr)) || ("".equals(memCachedStr)) || (memCachedStr == null)) {
							resString = "十分钟内为发送，请手动输入";
						} else {
							resString = BusInfo.dealBusContent(memCachedStr);
							memCachedUtil.setMemCached(FromUserName, Content,
									new Date(System.currentTimeMillis() + 300000L));
						}
					} else if ("desc_to".equals(map.get("EventKey"))) {
						if (("0001".equals(memCachedStr)) || ("".equals(memCachedStr)) || (memCachedStr == null)) {
							resString = "十分钟内为发送，请手动输入";
						} else {
							String[] con = memCachedStr.split(" ");
							String descStr = "";
							if (con.length == 2) {
								descStr = con[0];
							} else {
								descStr = con[0] + " 2";
							}
							resString = BusInfo.dealBusContent(descStr);
							memCachedUtil.setMemCached(FromUserName, descStr,
									new Date(System.currentTimeMillis() + 300000L));
						}
					} else if ("get_contact".equals(map.get("EventKey"))) {
						resString = "任何建议，联系，或者发新昌本地文章可邮件发送xcwxgj@sina.com\n小编定及时回复";
					}
				}
			}
			message = MessageUtil.initText(ToUserName, FromUserName, resString);
			LOGGER.info("正常--####--出来-》BusApi-》dopost--####，新昌公交返回message 数据：" + message);
			out.print(message);
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			LOGGER.error("！！错误--####--busApi接口类xml解析错误，", e);
		}
	}
}
