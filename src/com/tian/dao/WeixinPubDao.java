package com.tian.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.tian.base.WeixinContent;
import com.tian.util.DBUtilMysql;


public class WeixinPubDao {
	protected static final Logger logger = Logger.getLogger(WeixinPubDao.class);

	
	
	public  WeixinContent findByName(String CONTENT_OPEN) {
		Connection conn = DBUtilMysql.getConnection();
		try {
			String sql = "select CONTENT_ID, CONTENT_OPEN, CONTENT_CLASS_NAME from wx_content  where CONTENT_OPEN=?";
			logger.info("正常--####--入口程序:sql"+sql);

			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, CONTENT_OPEN);
			ResultSet rs = pst.executeQuery();
			WeixinContent user = new WeixinContent();
			
			if (rs.next()) {
				user.setId(rs.getString("CONTENT_ID"));
				user.setCONTENT_OPEN(rs.getString("CONTENT_OPEN"));
				user.setCONTENT_CLASS_NAME(rs.getString("CONTENT_CLASS_NAME"));
			}
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
