package com.tian.api;

import com.tian.util.HttpUtil;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Wether
{
  protected static final Logger LOGGER = Logger.getLogger(Wether.class);
  
  public static Map<String, String> getWetherInfo(String city)
  {
    LOGGER.info("正常--####--进入Wether-》getWetherInfo--####,传入参数city：" + city);
    Map<String, String> resMap = new HashMap();
    String resStr = "";
    String WETHER_URL = "http://api.map.baidu.com/telematics/v2/weather?location=CITY&ak=1a3cde429f38434f1811a75e1a90310c";
    try
    {
      city = URLEncoder.encode(city, "UTF-8");
      
      WETHER_URL = WETHER_URL.replace("CITY", city);
      
      String resutl = HttpUtil.httpGet(WETHER_URL);
      try
      {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new ByteArrayInputStream(resutl
          .getBytes("utf-8")));
        
        Element root = document.getRootElement();
        
        Element ticket = null;
        
        Iterator tickets = null;
        tickets = root.element("results").elementIterator();
        if (tickets.hasNext())
        {
          ticket = (Element)tickets.next();
          Element date = ticket.element("date");
          
          Element temperature = ticket.element("temperature");
          
          Element weather = ticket.element("weather");
          
          Element wind = ticket.element("wind");
          
          resStr = resStr + "今日天气：" + temperature.getText() + "，" + weather.getText() + "，" + wind.getText() + "\n\n";
        }
      }
      catch (Exception e)
      {
        LOGGER.error("！！错误--####--天气接口，xml解析错误", e);
      }
      resMap.put("errcode", "0000");
      resMap.put("errmsg", "返回正常");
      resMap.put("wether_info", resStr);
      LOGGER.info("正常--####--出来Wether-》getWetherInfo--####,返回resStr" + resStr);
      
      return resMap;
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--天气接口，获取数据异常，", e);
      resMap.put("errcode", "0002");
      resMap.put("errmsg", "Get请求异常，或者encode异常");
    }
    return resMap;
  }
  
  public static void main(String[] args)
  {
    getWetherInfo("宁波");
  }
}
