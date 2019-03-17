package com.tian.servlet;

import com.tian.api.BusGetPlace;
import com.tian.csbus.BusFunction;
import com.tian.util.CheckUtil;
import com.tian.util.MemCachedUtil;
import com.tian.util.MessageUtil;
import com.tian.xcbus.BusAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class CSBusApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(CSBusApi.class);
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
				BusFunction busFunction = new BusFunction();
				String result = "";
				if (con.length == 1) {
					result = busFunction.queryByLine(con[0], "0");
				} else {
					result = busFunction.queryByLine(con[0], con[1]);
				}
				resString = result;
				
			} else if ("event".equals(methodType)) {
				if ("subscribe".equals(map.get("Event"))) {
					resString = "欢迎关注新昌无线公交！\n\n\n1，发送准确路线查询，如3路或3查询，工业园区线查询该路线，空格+2查询反向信息如“3 2”\n2，发送地点查询地点路线经过情况，如“人民医院”\n3，地点与地点间查询正在开发中";
				} else if ("CLICK".equals(map.get("Event"))) {
					String memCachedStr = memCachedUtil.getMemCached(FromUserName);
					if ("flush".equals(map.get("EventKey"))) {
						if (("0001".equals(memCachedStr)) || ("".equals(memCachedStr)) || (memCachedStr == null)) {
							resString = "十分钟内为发送，请手动输入";
						} else {
							resString = (BusAction.getBusWx(memCachedStr)).get("info");
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
							resString = (BusAction.getBusWx(descStr)).get("info");
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
