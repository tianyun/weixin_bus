package com.tian.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

import com.tian.xcbus.Bus;

public class BusHttpUtil
{
  private static final Logger LOGGER = Logger.getLogger(Bus.class);
  private static String COOKIE = "";
  
  public static String getCOOKIE()
  {
    return COOKIE;
  }
  
  public static void setCOOKIE(String cOOKIE)
  {
    COOKIE = cOOKIE;
  }
  
  public static String BusHttp(String url)
  {
    LOGGER.info("正常--####--进入BusHttpUtil-》BusHttp--####,传入参数url：" + url);
    try
    {
      StringBuilder resStr = null;
      String s = url;
      
      URL urlLink = new URL(s);
      HttpURLConnection resumeConnection = (HttpURLConnection)urlLink.openConnection();
      if (COOKIE == null) {
        LOGGER.error("！！错误 --####--新昌公交，错误信息：没有cooke");
      }
//      resumeConnection.setRequestProperty("Cookie", COOKIE);
//      resumeConnection.setRequestProperty("User-Agent", "LantaiyuanBus/1.2.2 (iPhone; iOS 11.0; Scale/2.00)");
//      resumeConnection.setRequestProperty("Host", "120.77.82.161");
      resumeConnection.connect();
      
      InputStream urlStream = resumeConnection.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));
      String result = null;
      resStr = new StringBuilder("");
      while ((result = bufferedReader.readLine()) != null) {
        resStr.append(result + "\n");
      }
      result = null;
      bufferedReader.close();
//      LOGGER.info("正常--####--出来BusHttpUtil-》BusHttp--####,返回：" + resStr.toString());
      return resStr.toString();
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--新昌公交,busHttpUtil 有异常", e);
    }
    return "0001";
  }
  
  public static void main(String[] args)
  {
    String s = "http://120.77.82.161/travel/realTime?routeid=31&stationid=5412&direction=0&userstationno=2&citycode=330624";
    String businfoString = BusHttp(s);
    System.out.println(businfoString);
  }
}
