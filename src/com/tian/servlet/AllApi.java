package com.tian.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import api.BusGetPlace;

import com.tian.util.CheckUtil;
import com.tian.util.MessageUtil;
import com.tian.web.MakeFriend.MakeFriend_To;

/**
 * 这个接口是托管接口，直接接管所有，即查询用户 发送内容
 */
public class AllApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(AllApi.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AllApi() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		LOGGER.info("正常--####--传入校验参数：" + req.toString());

		PrintWriter out = resp.getWriter();
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		doPost(req, resp);
		out.close();
		out = null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		PrintWriter out = resp.getWriter();// 获取输出流
		String message = null;
		// 获取微信公众号类名

		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);// 用dom4j解析传入的xml
			String Content = map.get("Content");
			String ToUserName = map.get("ToUserName");
			String FromUserName = map.get("FromUserName");
			LOGGER.info("正常--####--进入入口程序" + map.toString());
			String methodType = map.get("MsgType");
			LOGGER.info("正常--####--传入的消息类型：" + methodType);
			String busPlace = Content;
			String busNearPlace = "";
//			// 进行模糊查询
//			try {
//				Map<String, String> like_name_map = BusGetPlace.get_Place_Query(Content);
//				LOGGER.info("正常--####--模糊查询，获取数据" + like_name_map);
//				if ("0000".equals(like_name_map.get("errcode"))) {
//					LOGGER.info("正常--####--修改传入content 参数为：" + like_name_map.get("place_name"));
//					Content = like_name_map.get("place_name");
//					busNearPlace = "\n附近站点：" + Content;
//					System.out.println("1" + Content);
//				} else {
//					Content = Content;
//
//				}
//			} catch (Exception e) {
//				Content = Content;
//				LOGGER.error("！！错误，数据库地点模糊查询错误 ",e);
//				// TODO: handle exception
//			}
			

			// 查询地址，
			String resString = null;
			LOGGER.info("查询地址" + resString);
			if ("0003".equals(resString)) {
				message = MessageUtil.initText(ToUserName, FromUserName,
						"请输入精确地址查询，或找不到对应的站点，没有这条公交路线\n\n\n发送数字查询公交实时信息\n发送地点名称查询地址");
				// resString="找不到对应的站点或没有这条公交路线";
				// MakeFriend_To.pushToCustom(FromUserName , resString);

				LOGGER.info("正常--####--新昌公交返回message 数据： " + message);
			} else {
				message = MessageUtil.initText(ToUserName, FromUserName,
						"查询地点：" + busPlace + busNearPlace + "\n\n" + resString);
				// MakeFriend_To.pushToCustom(FromUserName , resString);
				LOGGER.info("正常--####--新昌公交，地点查公交，返回message 数据： " + message);
			}
			// message = MessageUtil.initText(ToUserName, FromUserName,
			// "该功能正在开发中\n\n");
			out.print(message);

			// out.print(message);
			out.flush();
			out.close();
		} catch (Exception e)

		{

			LOGGER.error("！！错误，", e);
			// TODO: handle exception
		}

	}

}
