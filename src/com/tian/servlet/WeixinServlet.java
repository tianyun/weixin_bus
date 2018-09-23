package com.tian.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.mysql.jdbc.ResultSet;
import com.tian.base.WeixinBase;
import com.tian.util.CheckUtil;
import com.tian.util.DBUtilMysql;
import com.tian.util.MessageUtil;

/**
 * 本地测试 纯java 接口
 * 微信入口程序 配置公网：ngrok -config ngrok.cfg -subdomain tianyun 8080
 * URL:http://tianyun.tunnel.mobi/WEIXIN/wx.do
 * 检查：返回失败：1，检查xml格式，2，检查发送人和收到人位置，3，检查utf-8是否转到ISO8859_1格式
 * @author zty
 * 
 */

public class WeixinServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = Logger
			.getLogger(WeixinServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		
		PrintWriter out = resp.getWriter();// 获取输出流
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);// 用dom4j解析传入的xml
			String toUserName = map.get("ToUserName");
			LOGGER.info("正常--####--进入入口程序" + map.toString());
			LOGGER.info("正常--####--微信公共号openid：" + toUserName);

			// 获取微信公众号类名
			
			// 查找关键字对应的消息类型　
			String conSql = " SELECT  CONTENT_ID ,  CONTENT_OPEN ,  CONTENT_CLASS_NAME   from  wx_content ";
			Map<String, String> paramCon = new HashMap<String, String>();
			paramCon.put("CONTENT_OPEN", toUserName);
			String className="";
			ResultSet rs = (ResultSet) DBUtilMysql.getSubResultSet(conSql, paramCon);
			try {
				if(rs.next()){
					LOGGER.info("正常--####--微信入口程序，从类型关系对应数据库获得消息");
					 className=rs.getString("CONTENT_CLASS_NAME");
				}
			} catch (SQLException e1) {
				LOGGER.error("!!错误--####--微信入口程序，数据库查询失败");
				e1.printStackTrace();
			}
			
			String message = null;

			if(className==null){
				LOGGER.error("！！错误--####--没有查到对应的类名");
				message="";
			}else{
				try {
					message = this.exce(className, map);
				} catch (Exception e) {
					LOGGER.error("！！错误--####--入口程序，执行反射有误");
					e.printStackTrace();
				}
			}
			

			out.print(message);
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			out.close();

		}

	}

	// 执行相对应类型的反射程序
	public String exce(String className, Map<String, String> param)
			throws Exception {
		String classNameString = "com.tian.servlet."
				+ className;
		LOGGER.info("正常--####--执行的类名是：" + classNameString);
		// 获取类类型
		Class cee = Class.forName(classNameString);

		// 类类型构造函数中的参数
		Class[] params = new Class[] { Map.class };

		// 获取构造参数
		Constructor constructor = cee.getConstructor(params);
		// 模拟类类型参数
		Object[] paramObjects = new Object[] { param };

		// 获取实例参数
		Object ooc = constructor.newInstance(paramObjects);

		// 强转，反射
		WeixinBase weixinBase = (WeixinBase) ooc;

		return weixinBase.exec();

	}
}
