package com.tian.dao;

import java.util.HashMap;
import java.util.Map;


import org.apache.log4j.Logger;

/**
 * 微信相关的数据库操作
 * @author Dell
 *
 */
public class WeixinMsgQuery implements WeixinDao {

	protected static final Logger LOGGER = Logger
			.getLogger(WeixinMsgQuery.class);

	

	public static void main(String[] args) {
		WeixinMsgQuery weixinMsgQuery = new WeixinMsgQuery();
		String sql = "SELECT  TITLE ,  THUMBMEDIAID ,  DESCRIPTION ,  MUSICURL, HQMUSICURL FROM  wx_pub_key_music ";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("KEY_WORD", "测试音乐");
		paramMap.put("KEY_TEXT_ID", "1");

	}
}
