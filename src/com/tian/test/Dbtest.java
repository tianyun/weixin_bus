package com.tian.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.ResultSetMetaData;
import com.tian.entity.Music;
import com.tian.util.DBUtilMysql;

public class Dbtest {
	public static void main(String[] args) {

		String sql = " SELECT  TITLE ,  THUMBMEDIAID ,  DESCRIPTION ,  MUSICURL, HQMUSICURL FROM  wx_pub_key_music WHERE  KEY_WORD = ?";
		String content = "测试音乐";

		Connection conn = DBUtilMysql.getConnection();

		PreparedStatement pst;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, content);
			ResultSet rs = pst.executeQuery();
			rs.last(); // 结果集指针知道最后一行数据
			int n = rs.getRow();
			System.out.println(n);
			rs.beforeFirst();// 将结果集指针指回到开始位置，这样才能通过while获取rs中的数据

			Music music = (Music) Dbtest.converttoModel(rs, Music.class);
			System.out.println(music);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Object converttoModel(ResultSet rs, Class clazz)
			throws SQLException {

		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		Method[] methods = clazz.getDeclaredMethods();
		Field[] fields = clazz.getDeclaredFields();
		if (rs.next()) {

			for (Field field : fields) {
				String fieldName = field.getName();
				// System.out.println("kk"+fieldName);
				System.out
				.println("====" + rs.getString(fieldName));
				for (Method method : methods) {
					if (method.getName().startsWith("set")&&method.getName().indexOf(fieldName)==3) {
						//System.out.println(method.getName());
						try {
							method.invoke(obj, (Object) rs.getString(fieldName
									.toUpperCase()));
							
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}
		}

		return obj;
	}

}
