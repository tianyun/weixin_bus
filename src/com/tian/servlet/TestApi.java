package com.tian.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tian.entity.News;
import com.tian.util.CheckUtil;
import com.tian.util.MessageUtil;
import com.tian.web.MakeFriend.MakeFriend_To;

/**
 * 这个接口是微信接口测试类
 */
public class TestApi extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger.getLogger(TestApi.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestApi() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		String signature = req.getParameter("signature");
//		String timestamp = req.getParameter("timestamp");
//		String nonce = req.getParameter("nonce");
//		String echostr = req.getParameter("echostr");
//		LOGGER.info("正常--####--传入校验参数：" + req.toString());
//
//		PrintWriter out = resp.getWriter();
//		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
//			out.print(echostr);
//		}
		doPost(req, resp);
//		out.close();
//		out = null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		PrintWriter out = resp.getWriter();// 获取输出流
		resp.setContentType("application/json;charset=utf-8");
		System.out.println(req.getParameter("mobileval")+"code:"+req.getParameter("codeMsg"));
		out.println("{\"optionKey\":\"0000\"}");
		out.flush();
		out.close();
//		String message = null;
//		// 获取微信公众号类名
//
//		try {
//			Map<String, String> map = MessageUtil.xmlToMap(req);// 用dom4j解析传入的xml
//			String Content = map.get("Content");// 关键字
//			String ToUserName = map.get("ToUserName");// 公众号id
//			String FromUserName = map.get("FromUserName");// 用户id
//			LOGGER.info("正常--####--进入入口程序" + map.toString());
//			String methodType = map.get("MsgType");// 消息类型
//			LOGGER.info("正常--####--传入的消息类型：" + methodType);
//			// 开始接口编写
//			 if ("makefriend_to".equals(Content)) {
//				//交友功能
//				message = MakeFriend_To.getMessage(ToUserName,
//						FromUserName);
////				message = MessageUtil.initText(ToUserName,
////						FromUserName,
////						"交友");
//			} else if ("serach_more".equals(Content)) {
//				//更多开发功能
//				message = MessageUtil.initText(ToUserName,
//						FromUserName,
//						"其他查询正在开发中，如果你有好的需求要开发，请在联系我们-问题反馈中留言，谢谢哈");
//				
//			} else if ("feedback".equals(Content)) {
//				//问题反馈功能
//				News news = new News();
//				news.setTitle("新昌公交问题（需求）反馈");
//				news.setDescription("请填写您的需求，或者直接添加我们微信好友");
//				news.setPicUrl("http://nbut-sucai.stor.sinaapp.com/qqq.png");
//				news.setUrl("http://1.nbut.sinaapp.com/activities/nbut_vote/index.php?openid="
//						+ FromUserName);
//				List<News> newsList = new ArrayList<News>();
//				newsList.add(news);
//
//				// message = MessageUtil.newsMessageToXml((NewsMessage) newsList);
//				message = MessageUtil.initNewsMessage(
//						ToUserName,
//						FromUserName, newsList);
//			}
//			
//			out.print(message);
//			out.flush();
//			out.close();
//		} catch (Exception e)
//		{
//			LOGGER.error("！！错误--####--busApi接口类xml解析错误，", e);
//			// TODO: handle exception
//		}

	}

}
