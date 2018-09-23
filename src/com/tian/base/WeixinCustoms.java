package com.tian.base;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mysql.jdbc.ResultSet;
import com.tian.util.AccessTokenUtil;
import com.tian.util.DBUtilMysql;
import com.tian.util.HttpUtil;

public class WeixinCustoms {
	protected static final Logger LOGGER = Logger
			.getLogger(WeixinCustoms.class);

	/**
	 * 从数据库 获取24小时互动用户
	 * @return
	 */
	public static List<String> getCustoms() {
		
		List<String> listOpenid = new ArrayList<String>();
		String getCustomsSql=" SELECT OPENID FROM  wx_makefriend";
		try {
			ResultSet rs= (ResultSet) DBUtilMysql.getSubResultSet(getCustomsSql, null);
			while(rs.next()){
				LOGGER.info("正常--####--用户操作，获取24小时用户");
				listOpenid.add(rs.getString("OPENID"));
				
			}
			return listOpenid;
		} catch (Exception e) {
			LOGGER.error("！！错误--####--用户操作，数据库 操作失败",e);
			return listOpenid;
		}
		
		
	}
	
	public  static String getCustomInfo(String openid) {
		String customUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1")+"&openid="+openid+"&lang=zh_CN";
		return (HttpUtil.httpGet(customUrl));
		
	}
	
	public static void main(String[] args) {
		System.out.println(getCustomInfo("o8c52uH_HTlls0Lhi8WILUmNcZ14"));
	}
}
