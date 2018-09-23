package com.tian.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tian.util.CheckUtil;
import com.tian.util.MessageUtil;

/**
 * 这个接口是标准接口类
 */
public class StandardApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(StandardApi.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StandardApi() {
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
			String Content = map.get("Content");// 关键字
			String ToUserName = map.get("ToUserName");// 公众号id
			String FromUserName = map.get("FromUserName");// 用户id
			LOGGER.info("正常--####--进入入口程序" + map.toString());
			String methodType = map.get("MsgType");// 消息类型
			LOGGER.info("正常--####--传入的消息类型：" + methodType);
			// 开始接口编写
			message = MessageUtil.initText(ToUserName, FromUserName, "该功能正在开发中\n\n");

			out.print(message);
			out.flush();
			out.close();
		} catch (Exception e) {
			LOGGER.error("！！错误--####--busApi接口类xml解析错误，", e);
			// TODO: handle exception
		}

	}

}
