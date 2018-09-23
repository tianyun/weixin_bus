package com.tian.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tian.njBus.NJBus;
import com.tian.njBus.NJBusAction;
import com.tian.util.CheckUtil;
import com.tian.util.MessageUtil;
import com.tian.web.MakeFriend.MakeFriend_To;

/**
 * 该接口是南京公交车微信接口
 */
public class NJBusApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(NJBusApi.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NJBusApi() {
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
			// out.print(echostr);
		}
		// doPost(req, resp);
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

//		PrintWriter out = resp.getWriter();// 获取输出流
		String message = "";
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
			// out.print("success");
			// message = MessageUtil.initText(ToUserName, FromUserName, null);
			// LOGGER.info("正常--####--返回到微信端1：" + message);
			OutputStream os = resp.getOutputStream();  
            os.write("".getBytes("utf-8"));  
            os.flush();  
            os.close();
			
//			out.flush();
//			out.print("");
//			out.flush();
//			out.close();
			Njbus Njbus = new Njbus();
			Thread thread = new Thread(Njbus);
			thread.start();
			// Content = Content.replace("nj", message);
			//
			// String resStr = NJBusAction.njBusAction(Content);

			// MakeFriend_To.pushToCustom(FromUserName, ""+resStr);

			// PrintWriter out1 = resp.getWriter();// 获取输出流
			//
			// Content = Content.replace("nj", "");
			// String resStr = NJBusAction.njBusAction(Content);
			// MakeFriend_To.pushToCustom(FromUserName, resStr);
			//
			// message = MessageUtil.initText(ToUserName, FromUserName,
			// "南京公交测试\n\n" + resStr);
			// LOGGER.info("正常--####--返回到微信端2：" + message);
			//
			// out.print(message);
			// out.flush();
			// out.close();
			// String resStr = NJBus.getBusInfo(Content);

			// MakeFriend_To.pushToCustom(FromUserName, resStr);
			// LOGGER.info("正常--####--返回到微信端："+message);

		} catch (Exception e) {
			LOGGER.error("！！错误--####--busApi接口类xml解析错误，", e);
			// TODO: handle exception
		}

	}

	class Njbus implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			LOGGER.info("进入线程");
		}

	}
}
