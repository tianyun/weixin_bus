package com.tian.web.MakeFriend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.tian.util.AccessTokenUtil;
import com.tian.util.WeixinUtil;

public class MakeFriend_GetInfo extends HttpServlet {
	protected static final Logger LOGGER = Logger
			.getLogger(MakeFriend_GetInfo.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		LOGGER.info("正常--####--交友，开始进入获取朋友信息DoGet请求");
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("正常--####--交友，开始进入获取朋友信息DoPost请求");

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String conParam = request.getParameter("tocustom");
		LOGGER.info("正常--####--交友，开始传入参数："+conParam);

		String customUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1")+"&openid="+conParam+"&lang=zh_CN";
		JSONObject result =  WeixinUtil.doGetStr(customUrl);
//		String result= WeixinCustoms.getCustomInfo(conParam);
		PrintWriter out = response.getWriter();
		response.setContentType("application/text;charset=utf-8");
//		out.println(result.toString());
		out.println(result.toString());

		out.flush();
		out.close();
	}

}
