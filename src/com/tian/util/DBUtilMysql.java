package com.tian.util;

import com.mysql.jdbc.ResultSetMetaData;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.log4j.Logger;

public class DBUtilMysql
{
  protected static final Logger LOGGER = Logger.getLogger(DBUtilMysql.class);
  private static final Map<String, String> resMap = new HashMap();
  private static String URL;
  private static String USER;
  private static String PASSWORD;
  private static String DRIVER;
  private static Connection conn = null;
  private static Statement stmt = null;
  
  static
  {
    getDBInfo();
  }
  
  public static void getDBInfo()
  {
    try
    {
      InputStream inStream = DBUtilMysql.class.getResourceAsStream("/jdbc.properties");
      Properties prop = new Properties();
      
      prop.load(inStream);
      USER = prop.getProperty("jdbc.username");
      PASSWORD = prop.getProperty("jdbc.password");
      URL = prop.getProperty("jdbc.url");
      DRIVER = prop.getProperty("jdbc.driver");
    }
    catch (Exception e)
    {
      throw new RuntimeException("数据库读取异常", e);
    }
  }
  
  public static Connection getConnection()
  {
    if (conn == null) {
      try
      {
        closeConnection(conn);
        LOGGER.info("------ conn is not  null and closed");
        
        LOGGER.info("正常--####--数据库连接信息：" + URL + ":" + USER + ":" + PASSWORD + ":" + DRIVER);
        
        Class.forName(DRIVER);
        
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (conn == null) {
          LOGGER.error("！！错误--####--入口程序，数据库连接异常");
        }
        LOGGER.info("正常--####--入口程序，mysql connection is ok");
        return conn;
      }
      catch (Exception e)
      {
        LOGGER.error("！！错误--####--入口程序，数据库连接异常", e);
      }
      finally
      {
        try
        {
          if (stmt != null) {
            stmt.close();
          }
        }
        catch (Exception e2)
        {
          LOGGER.error("！！错误--####--数据库连接异常", e2);
        }
      }
    }
    return conn;
  }
  
  public static void closeConnection(Connection c)
  {
    try
    {
      if (c != null) {
        c.close();
      }
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--数据库关闭异常" + e);
      
      e.printStackTrace();
    }
  }
  
  public static Map<String, String> rs2Map(ResultSet rs)
  {
    try
    {
      Map<String, String> hm = new HashMap();
      


      LOGGER.info("正常--####--进入数据库格式转换为map格式");
      ResultSetMetaData rsmd = (ResultSetMetaData)rs.getMetaData();
      int count = rsmd.getColumnCount();
      for (int i = 1; i <= count; i++)
      {
        String key = rsmd.getColumnLabel(i);
        String value = rs.getString(i);
        hm.put(key, value);
      }
      return hm;
    }
    catch (SQLException e)
    {
      LOGGER.error("！！错误--####--数据库操作有错误" + e);
      
      e.printStackTrace();
    }
    return null;
  }
  
  public static Object rs2Bean(ResultSet rs, Class clazz)
    throws SQLException
  {
    Object obj = null;
    try
    {
      obj = clazz.newInstance();
    }
    catch (InstantiationException e1)
    {
      e1.printStackTrace();
    }
    catch (IllegalAccessException e1)
    {
      e1.printStackTrace();
    }
    Method[] methods = clazz.getDeclaredMethods();
    Field[] fields = clazz.getDeclaredFields();
    if (rs.next()) {
      for (Field field : fields)
      {
        String fieldName = field.getName();
        
        System.out.println("====" + rs.getString(fieldName));
        for (Method method : methods) {
          if ((method.getName().startsWith("set")) && (method.getName().indexOf(fieldName) == 3)) {
            try
            {
              method.invoke(obj, new Object[] { rs.getString(fieldName.toUpperCase()) });
            }
            catch (Exception e)
            {
              LOGGER.error("！！错误--####--数据库 数据库对象转成javabean 有异常" + e);
            }
          }
        }
      }
    }
    return obj;
  }
  
  public static ResultSet getSubResultSet(String sql, Map<String, String> paramMap)
  {
    if (paramMap != null)
    {
      sql = sql + "where 1=1 ";
      for (String key : paramMap.keySet()) {
        sql = sql + " and " + key + " = '" + (String)paramMap.get(key) + "' ";
      }
    }
    ResultSet rs = null;
    Connection conn = getConnection();
    try
    {
      PreparedStatement pst = conn.prepareStatement(sql);
      return pst.executeQuery();
    }
    catch (SQLException e)
    {
      LOGGER.error("！！错误--####--数据库操作有错误" + e);
      
      e.printStackTrace();
    }
    return rs;
  }
  
  public static Map<String, String> getSubMap(String sql, Map<String, String> paramMap)
  {
    sql = sql + "where 1=1 ";
    for (String key : paramMap.keySet()) {
      sql = sql + " and " + key + " = '" + (String)paramMap.get(key) + "' ";
    }
    Connection conn = getConnection();
    LOGGER.info("正常--####--进入获取map对象查询，sql:" + sql);
    Map<String, String> resultMap = new HashMap();
    try
    {
      Statement stm = conn.createStatement();
      ResultSet rs = stm.executeQuery(sql);
      if (rs.next())
      {
        LOGGER.info("正常--####--从数据库查询到数据");
        ((Map)resultMap).put("errcode", "0000");
        

        resultMap = rs2Map(rs);
      }
      else
      {
        ((Map)resultMap).put("errcode", "0001");
        return resultMap;
      }
      return resultMap;
    }
    catch (SQLException e)
    {
      LOGGER.error("！！错误--####--数据库操作有错误" + e);
      e.printStackTrace();
    }
    return resultMap;
  }
  
  public static void update(String tableName, Map<String, Object> updateMap, Map<String, Object> condMap)
    throws Exception
  {
    String updateSql = "";
    for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
      updateSql = updateSql + (String)entry.getKey() + " = ' " + entry.getValue() + "' ,";
    }
    updateSql = updateSql.substring(0, updateSql.length() - 1);
    
    String condSql = "";
    for (Object entry : condMap.entrySet()) {
      condSql = condSql + " and " + (String)((Map.Entry)entry).getKey() + " = ' " + ((Map.Entry)entry).getValue() + "'";
    }
    String sql = "update " + tableName + " set " + updateSql + " where 1 =1 " + condSql;
    LOGGER.info("正常--####--进入获取map对象更新，sql:" + sql);
    
    conn = getConnection();
    try
    {
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.execute();
    }
    catch (SQLException e)
    {
      throw new RuntimeException("！！错误--####--数据库操作-->insert有错误", e);
    }
    PreparedStatement pstmt;
  }
  
  public static void insert(String tableName, Map<String, Object> paramMap)
    throws Exception
  {
    LOGGER.info("正常--####--进入数据库DBUtilMysql->insert 操作 tableName:" + tableName + ",paramMap:" + paramMap.toString());
    
    String key = "";
    String value = "";
    for (Map.Entry<String, Object> entry : paramMap.entrySet())
    {
      key = key + (String)entry.getKey() + " ,";
      value = value + "'" + entry.getValue() + "'" + " ,";
    }
    key = key.substring(0, key.length() - 1);
    value = value.substring(0, value.length() - 1);
    String sql = "insert into " + tableName + " (  " + key + " ) values (  " + value + " )";
    LOGGER.info("正常--####--数据库插入语句：" + sql);
    try
    {
      conn = getConnection();
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.execute();
      

      LOGGER.info("正常--####--出来-》DBUtilMysql-》insert--####，数据库插入正常");
    }
    catch (SQLException e)
    {
      throw new RuntimeException("！！错误--####--数据库操作-->insert有错误", e);
    }
    PreparedStatement pstmt;
  }
  
  public static void remove(String tableName, Map<String, Object> removeParam)
    throws Exception
  {
    LOGGER.info("正常--####--进入数据库->remove 操作 removeParam:" + removeParam.toString());
    String removeSql = "";
    for (Map.Entry<String, Object> entry : removeParam.entrySet()) {
      removeSql = removeSql + " and " + (String)entry.getKey() + "= " + entry.getValue();
    }
    String sql = "delete from " + tableName + " where 1=1 " + removeSql;
    LOGGER.info("正常--####--数据库插入语句->remove：" + sql);
    
    conn = getConnection();
    try
    {
      PreparedStatement pstmt = conn.prepareStatement(sql);
      pstmt.execute();
    }
    catch (SQLException e)
    {
      throw new RuntimeException("！！错误--####--数据库操作-->insert有错误", e);
    }
    PreparedStatement pstmt;
  }
  
  public static Map<String, String> getMap(String errcode, String errmsg)
  {
    resMap.put("errcode", errcode);
    resMap.put("errmsg", errmsg);
    return resMap;
  }
  
  public static void main(String[] args)
    throws Exception
  {
    getConnection();
    
    Map<String, Object> tempMap = new HashMap();
    tempMap.put("user ", "zty ");
    tempMap.put("content ", "hhhh ");
    tempMap.put("error ", "ok");
    insert("xc_bus", tempMap);
  }
}
