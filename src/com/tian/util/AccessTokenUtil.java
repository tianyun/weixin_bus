package com.tian.util;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.tian.util.DBUtilMysql;
import org.apache.log4j.Logger;
import org.json.JSONObject;


/**
 * accessToken相关方法
 * @author Dell
 *
 */
public class AccessTokenUtil {
	protected static final Logger LOGGER = Logger
			.getLogger(AccessTokenUtil.class);
//	private static final String APPID = "wxae216bce0a4bacaf";//测试账号
//	private static final String APPSECRET = "ba4506e5748f61b2a7b659f014c173eb";
//	private static final String APPID = "wxed15e7fa0cf694ec";
//	private static final String APPSECRET = "bafac7dc827ba85c69c9e15a0b28d8a6";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 获取access_token
	 * 
	 * @return
	 */
	public static String getAccessToken(String openidStr) {
		String tokenStr = "";
		// 从数据库里获取APPID APPSECRET
		String APPIDSql = " SELECT  CONTENT_APPID ,  CONTENT_APPSECRET  FROM  wx_content ";
		Map<String, String> paramAPPID = new HashMap<String, String>();
		paramAPPID.put("CONTENT_OPEN ", openidStr);
		Map<String, String> APPIDTextMap = DBUtilMysql.getSubMap(APPIDSql,
				paramAPPID);
		String APPID=APPIDTextMap.get("CONTENT_APPID");
		String APPSECRET=APPIDTextMap.get("CONTENT_APPSECRET");
		LOGGER.info("正常--####--从数据库获取APPID ："+APPID+"， APPSECRET："+ APPSECRET);
		
		// 查询数据库，判断accesstoken 是否过期
		String textSql = " SELECT  CONTENT_ACCESS_TOKEN ,  CONTEN_DATE ,  CONTEN_OPENID  FROM  wx_content_access_token ";
		Map<String, String> paramCon = new HashMap<String, String>();
		paramCon.put("CONTEN_OPENID", openidStr);
		Map<String, String> resultTextMap = DBUtilMysql.getSubMap(textSql,
				paramCon);
		
		long currDate = Long.parseLong("" + System.currentTimeMillis());
		//判断是否有accesstoken存在
		if("0001".equals(resultTextMap.get("errcode"))){
			String ttString="";
			LOGGER.error("正常--####--获取accesstoken 不存在对应公众号对应的accesstoken 即将插入");
			ttString=ttString+"正常--####--获取accesstoken 不存在对应公众号对应的accesstoken 即将插入";
			try {
				String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace(
						"APPSECRET", APPSECRET);
				String resultAcessToken = HttpUtil.httpGet(url);
				JSONObject jsonData = new JSONObject(resultAcessToken);
				LOGGER.info("正常--####--获取accesstoken:"+jsonData.getString("access_token"));
				ttString=ttString+"\n"+"正常--####--获取accesstoken:"+jsonData.getString("access_token");
				Connection conn = DBUtilMysql.getConnection();
				String sql = "insert into wx_content_access_token" + "(CONTENT_ACCESS_TOKEN,CONTEN_DATE,CONTEN_OPENID)"
						+ "values" + "(?,?,?)";
				java.sql.PreparedStatement ptmt = conn.prepareStatement(sql);
				ptmt.setString(1, jsonData.getString("access_token"));
				ptmt.setString(2, ""+currDate);
				ptmt.setString(3, openidStr);
				ptmt.execute();
				
				LOGGER.info("正常--####--获取accesstoken 插入数据成功");
				ttString=ttString+"\n"+"正常--####--获取accesstoken 插入数据成功";
				tokenStr = jsonData.getString("access_token");
			} catch (Exception e) {
				LOGGER.error(ttString+"!!错误--####--数据处理有错误"+e);
			}
			
			return tokenStr;

		}
		
		long sqlDate = Long.parseLong(resultTextMap.get("CONTEN_DATE"));
		if ((currDate - sqlDate) > 1800000) {
			LOGGER.info("正常--####--获取accesstoken 已过期");
			try {
				String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace(
						"APPSECRET", APPSECRET);
//				String resultAcessToken = HttpUtil.httpGet(url);
//				JSONObject jsonData = new JSONObject(resultAcessToken);
				net.sf.json.JSONObject jsonData = WeixinUtil.doGetStr(url);
				LOGGER.info("正常--####--获取accesstoken:"+jsonData.getString("access_token"));

				// 更新accessToken
//				Connection conn = DBUtilMysql.getWriteConnection();sae
				Connection conn = DBUtilMysql.getConnection();

				String sql = " " + " update wx_content_access_token "
						+ " set CONTENT_ACCESS_TOKEN=? , CONTEN_DATE=? "
						+ " where CONTEN_OPENID= '" + openidStr + "' ";
				java.sql.PreparedStatement ptmt = conn.prepareStatement(sql);
				ptmt.setString(1, jsonData.getString("access_token"));
				ptmt.setString(2, "" + currDate);
				ptmt.execute();
				LOGGER.info("正常--####--获取accesstoken更新数据成功");
				tokenStr = jsonData.getString("access_token");
			} catch (Exception e) {
				LOGGER.error("!!错误--####--数据处理有错误",e);
			}
			return tokenStr;

		} else {
			LOGGER.info("正常--####--获取accesstoken 没有过期，\n accesstoken："
					+ resultTextMap.get("CONTENT_ACCESS_TOKEN") + ",已经过了："
					+ (currDate - sqlDate) + "毫秒");
			tokenStr = resultTextMap.get("CONTENT_ACCESS_TOKEN");
			return tokenStr;

		}


	}

	public static void main(String[] args) {
		AccessTokenUtil.getAccessToken("gh_1579fe4d87d1");
	}
}
