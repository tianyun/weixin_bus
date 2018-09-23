package com.tian.web.MakeFriend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import com.sina.sae.memcached.SaeMemcache;
import com.tian.base.WeixinCustoms;
import com.tian.entity.News;
import com.tian.entity.PushType;
import com.tian.util.AccessTokenUtil;
import com.tian.util.MessageUtil;
import com.tian.util.WeixinUtil;

public class MakeFriend_To extends HttpServlet {
	protected static final Logger LOGGER = Logger
			.getLogger(MakeFriend_To.class);
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
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String conParam = request.getParameter("conParam");
		String ToOpenid = request.getParameter("ToOpenid");//发送给的人
		String MK_FromUserName = request.getParameter("MK_FromUserName");//自己
		
		MK_FromUserName=new   String(MK_FromUserName.getBytes("ISO8859_1"),"utf-8");
//		conParam=new   String(conParam.getBytes("ISO8859_1"),"utf-8");
		ToOpenid=new   String(ToOpenid.getBytes("ISO8859_1"),"utf-8"); 
		
//		String fromUserInfo= WeixinCustoms.getCustomInfo(MK_FromUserName);
		
		String customUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1")+"&openid="+MK_FromUserName+"&lang=zh_CN";
		net.sf.json.JSONObject jsonData= WeixinUtil.doGetStr(customUrl);
//		JSONObject jsonData;
		try {
//			jsonData = new JSONObject(fromUserInfo);
			String Name = jsonData.getString("nickname");
			String uur="或者点击<a href='http://1.wxfinal.sinaapp.com/MakeFriend/MakeFriend_view.html?FromUserName="+ToOpenid+"&tocustom="+MK_FromUserName+"'>此处回复</a>";
			conParam=conParam.replace("@", "");
			conParam=Name+" 发来消息：\n"+conParam+"\n\n快速回复可回复@+内容\n"+uur;
			PrintWriter out = response.getWriter();
			response.setContentType("application/json;charset=utf-8");
			
			String errcode=pushToCustom(ToOpenid, conParam);
			if ("0000".equals(errcode)) {
				SaeMemcache mc = null;
				mc = new SaeMemcache();
				mc.init();
				mc.set(ToOpenid, MK_FromUserName, 1800);

				out.println("{\"optionKey\":\"0000\"}");
			}else {
				out.println("{\"optionKey\":\"0001\"}");
			}
			
				out.flush();
				out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

	/**
	 * 发送消息 0000即推送成功
	 * @param FromUserName
	 * @param conParam
	 * @return
	 */
	public static String pushToCustom(String ToOpenid,String conParam) {
//		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1");
		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=CC44jJsfhTVuybeXxAy7Ltjp3QudZVjg9s3ik8iLVR_TtyXFyw5ZMfpuWewu_lsdKezY2gtCsGVEQsgcJ13qx7rAKJvC6XSERCNf_ZgtCHYDQUcACAREI";

		String param = PushType.getType("text", conParam,
				ToOpenid);
		LOGGER.info("正常--####--传入参数：内容"+conParam+":"+ToOpenid);

		LOGGER.info("正常--####--交友，推送内容："+param);
		
		net.sf.json.JSONObject jsonData =WeixinUtil.doPostStr(postUrl, param);
//		String resulStr = HttpUtil.httpPOST(postUrl, param);
		try {
//			JSONObject jsonData = new JSONObject(resulStr);
			String res = jsonData.getString("errcode");
			
			
			if ("0".equals(res)) {
				
				LOGGER.info("正常--####--交友，消息推送成功：");
				return "0000";
			}else {
				LOGGER.error("正常--####--交友，消息推送失败");
				return "0001";
			}
			
		} catch (Exception e) {
			LOGGER.error("！！错误--####--交友，json格式转换错误",e);
			return "0001";
		}
		
		
	}
	
	/**
	 * 同一报文返回方法
	 * @param ToUserName
	 * @param FromUserName
	 * @return
	 */
	public static String getMessage(String ToUserName, String FromUserName) {
		String message="";
		//获取用户
		List<String> userList= WeixinCustoms.getCustoms();
		LOGGER.info("正常--####--交友，获取用户openid长度："+userList.size());
		Random rand=new Random();
		int friendNum=userList.size();
		
		if (friendNum==0) {
			LOGGER.info("正常--####--交友，没有用户参与交友");
		}else if(friendNum>10) {
			friendNum=10;
		}
		//拼装用户openid str
		List<String> finaUserList= new ArrayList<String>();
		for (int i = 0; i < friendNum; i++) {
			int randNum = rand.nextInt(userList.size());
			finaUserList.add(userList.get(randNum));
			userList.remove(randNum);
			
		}
		LOGGER.info("正常--####--交友，得到用户字符串"+finaUserList.toString());
		//拼装图文
		List<News> listNews= new ArrayList<News>();
		News news = new News();
		news.setTitle("新昌公交交友(正在开发中)");
		news.setDescription("新昌公交交友beat 1.0");
		news.setPicUrl("http://nbut-sucai.stor.sinaapp.com/qqq.png");
		news.setUrl("");
		listNews.add(news);
		for (int i = 0; i < friendNum; i++) {
			LOGGER.info("正常--####--交友，获得发送openid"+finaUserList.get(i));
			String customUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1")+"&openid="+finaUserList.get(i)+"&lang=zh_CN";
			net.sf.json.JSONObject jsonData= WeixinUtil.doGetStr(customUrl);
			LOGGER.info("正常--####--交友，获取用户信息返回报文"+jsonData.toString());

//			String info= WeixinCustoms.getCustomInfo(finaUserList.get(i));
			try {
//				JSONObject jsonData = new JSONObject(info);
				String Name = jsonData.getString("nickname");
				String Sex = jsonData.getString("sex");
				String  City= jsonData.getString("city");
				String  Province= jsonData.getString("province");
				String  Headimgurl= jsonData.getString("headimgurl");
				
				News news1 = new News();
				news1.setTitle("姓名："+Name+"\nsex"+Sex+"\n city"+Province+":"+City);
				news1.setDescription("");
				news1.setPicUrl(Headimgurl);
				news1.setUrl("http://1.wxfinal.sinaapp.com/MakeFriend/MakeFriend_view.html?FromUserName="+FromUserName+"&tocustom="+finaUserList.get(i));
				listNews.add(news1);
			} catch (Exception e) {
				LOGGER.error("！！错误--####--交友，json格式转换错误",e);
				// TODO: handle exception
			}
			
		}
		message=MessageUtil.initNewsMessage(ToUserName, FromUserName, listNews);
		LOGGER.info("正常--####--交友，统一报文返回返回报文"+message);

		
		return message;
	}

}
