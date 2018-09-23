package com.tian.wechat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import com.mysql.jdbc.Connection;
import com.tian.util.DBUtilMysql;

/**
 * 记录微信用户发送内容
 * 
 * @author tianyun
 *
 */
public class RecordWord {

	public static void main(String[] args) {
		Map<String, Object> paramMap= new HashMap<String,Object>();
		paramMap.put("OPENID", "xxxxxxxkkeeo111IU4xDQxlIMeEaWAmDpqdv2-uts");
		paramMap.put("WECHAT_ID", "gh_1579fe4d87d1");
		paramMap.put("CONTENT", "1234");
		paramMap.put("CONTENT_FLAG", "1");

		RecordWord.insertRecord(paramMap);
	}
	/**
	 * 日志记录工具
	 */
	protected static final Logger LOGGER = Logger.getLogger(RecordWord.class);

	/**
	 * 记录用户发送
	 * o1IU4xDQxlIMeEaWAmDpqdv2-uts
	 * @param paramMap
	 * @return
	 */
	public static Map<String, Object> insertRecord(Map<String, Object> paramMap) {
		String logInfo = "正常--####--RecordWord -->insertRecord  ";
		LOGGER.info("正常--####--RecordWord -->insertRecord 传入 参数paramMap:"+paramMap.toString());
		String sql = "select OTHER_CONTENT from wx_pub_key_record  ";
		Map<String, String> queryMap =new HashMap<String, String>();
		queryMap.put("OPENID", (String) paramMap.get("OPENID"));
		
		try {
			ResultSet rs = DBUtilMysql.getSubResultSet(sql, queryMap);
//			rs.last(); //结果集指针知道最后一行数据  
//			int n = rs.getRow();  
//			LOGGER.info(logInfo+"res length:"+n);
			Map<String, Object>insertMap = new HashMap<String,Object>();
			if(paramMap.get("CONTENT_FLAG")=="1"){
				insertMap.put("OTHER_CONTENT", paramMap.get("CONTENT"));
			}
			insertMap.put("OPENID", paramMap.get("OPENID"));
			insertMap.put("WECHAT_ID", paramMap.get("WECHAT_ID"));
			insertMap.put("CONTENT", paramMap.get("CONTENT"));
			insertMap.put("UPDATETIME", new java.sql.Date(new java.util.Date().getTime()));
			if(rs!=null){
				DBUtilMysql.insert("wx_pub_key_record", insertMap);
			}else{
				String Flag="";
				if(paramMap.get("CONTENT_FLAG")=="1"){
					Flag = ", OTHER_CONTENT = '"+rs.getString(1)+","+paramMap.get("CONTENT")+"', CONTENT_PLACE= CONTENT_PLACE+1";
				}
				Connection conn = (Connection) DBUtilMysql.getConnection();
				String sql1="update wx_pub_key_record "
						+ " set CONTENT_NUM= CONTENT_NUM+1 ,CONTENT = '"+paramMap.get("CONTENT")+"',  UPDATETIME= '"+new java.sql.Date(new java.util.Date().getTime())+"'"+Flag
						+ " where OPENID = '"+paramMap.get("OPENID")+"' and WECHAT_ID = '"+paramMap.get("WECHAT_ID")+"'";
				LOGGER.info("正常--####--进入获取map对象更新，sql:" + sql1);
				PreparedStatement pstmt;
				try {
					pstmt = (PreparedStatement) conn.prepareStatement(sql1);
					pstmt.execute();
//					pstmt.close();
//					conn.close();
				} catch (SQLException e) {
					throw new RuntimeException("！！错误--####--数据库操作-->insert有错误", e);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 推送给用户管理员
	 * @param pushMap
	 * @return
	 */
	public static Map<String, Object> pushManager(Map<String, Object> pushMap ) {
		String logInfo = "正常--####--RecordWord -->pushManager  ";

		LOGGER.info(logInfo+" 推送给用户,pushMap:"+pushMap.toString());
		return null;
		
	}
}
