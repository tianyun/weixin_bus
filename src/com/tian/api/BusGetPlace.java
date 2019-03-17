package com.tian.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tian.util.BusHttpUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class BusGetPlace
{
  private static final Logger LOGGER = Logger.getLogger(BusGetPlace.class);
  private static String BUSSTATIONQUERY = "http://120.77.82.161/queryRouteStation/routeAndStation?citycode=330624&name=";
  private static String BUSPLACEINFO = "http://120.77.82.161/queryRouteStation/route?citycode=330624&stationid=";
  
  public static Map<String, String> getPlaceInfo(String param)
    throws JSONException
  {
    LOGGER.info("正常--####--进入BusGetPlace-》getPlaceInfo--####,传入参数param：" + param);
    Map<String, String> resMap = new HashMap();
    if ((param == null) || ("".equals(param)))
    {
      resMap.put("code", "0001");
      resMap.put("msg", "busline 传入空值");
      LOGGER.error("！！错误--####--新昌公交,BusInfo-》getBusLine####busline 传入空值");
      return resMap;
    }
    String palceUrl = BUSSTATIONQUERY + param;
    String result = BusHttpUtil.BusHttp(palceUrl);
    JSONObject resultJsonData =  JSONObject.parseObject(result);
    JSONObject infoObj = (JSONObject)resultJsonData.get("obj");
    try
    {
      JSONArray stationArr = (JSONArray)infoObj.get("stations");
      if (!"[]".equals(stationArr.toString()))
      {
        if (stationArr.size() > 2)
        {
          StringBuilder tempStationStr = new StringBuilder();
          for (int i = 0; i < stationArr.size(); i += 2) {
            tempStationStr.append(((JSONObject)stationArr.get(i)).getString("name") + " ");
          }
          resMap.put("stationName", "有以下多个相似地点，请选择：\n" + tempStationStr.toString());
          resMap.put("msg", "处理正常，有多组数据");
          resMap.put("size", ""+stationArr.size());
          resMap.put("code", "0000");
        }
        else
        {
          resMap.put("stationName", ((JSONObject)stationArr.get(0)).getString("name"));
          resMap.put("stationid0", ((JSONObject)stationArr.get(0)).getString("stationid"));
          resMap.put("stationid1", ((JSONObject)stationArr.get(1)).getString("stationid"));
          
          resMap.put("size", "2");
          resMap.put("msg", "处理正常，只有单一数据");
          resMap.put("code", "0000");
        }
      }
      else
      {
        resMap.put("code", "0003");
        resMap.put("msg", "没有该站点或路线，请输入精确路线，");
      }
      LOGGER.info("正常--####--出来BusGetPlace-》getPlaceInfo--####,返回" + resMap.toString() + "\n");
      return resMap;
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--新昌公交,BusGetPlace-》getPlaceInfo####判断路线有问题 有异常", e);
      resMap.put("code", "0002");
      resMap.put("msg", "json处理有异常");
    }
    return resMap;
  }
  
  public static Map<String, String> getPlace(String stationid)
    throws JSONException
  {
    LOGGER.info("正常--####--进入BusGetPlace-》getPlace--####,传入参数stationid：" + stationid);
    Map<String, String> resMap = new HashMap();
    if ((stationid == null) || ("".equals(stationid)))
    {
      resMap.put("code", "0001");
      resMap.put("msg", "busline 传入空值");
      LOGGER.error("！！错误--####--新昌公交,BusGetPlace-》getPlace####stationid 传入空值");
      return resMap;
    }
    String placeUrl = BUSPLACEINFO + stationid;
    String result = BusHttpUtil.BusHttp(placeUrl);
    

    JSONObject resultJsonData = JSONObject.parseObject(result);
    JSONArray objJsonDataArr = (JSONArray)resultJsonData.get("obj");
    StringBuilder lineSB = new StringBuilder();
    for (int i = 0; i < objJsonDataArr.size(); i++)
    {
      System.out.println("num :" + i + "size:" + objJsonDataArr.size());
      System.out.println("objJsonDataArr :" + objJsonDataArr.toString());
      
      JSONObject tempObj = (JSONObject)objJsonDataArr.get(i);
      System.out.println("tempObj:" + tempObj.toString());
      






      JSONObject routeInBaseLineOjb = (JSONObject)tempObj.get("routeInBaseLine");
      try
      {
        JSONObject realTimeOjb = (JSONObject)tempObj.get("realTime");
        System.out.println("realTimeOjb:" + realTimeOjb.toString());
        
        String tempStationId = ((JSONObject)realTimeOjb.get("desc")).getString("stationid");
        String tempDirectionValue = ((JSONObject)realTimeOjb.get("desc")).getString("type");
        System.out.println("tempStationId：" + tempStationId + "路线" + routeInBaseLineOjb.getString("routeid") + "tempDirectionValu" + tempDirectionValue);
        
        Map<String, Object> stationMap = BusInfo.getBusLineInfo(routeInBaseLineOjb.getString("routeid"), tempDirectionValue);
        List<JSONObject> tempList = (List)stationMap.get("busBean");
        String nowStationStr = "";
        for (JSONObject jsonObject : tempList) {
          if (tempStationId.equals(jsonObject.getString("stationid")))
          {
            nowStationStr = jsonObject.getString("name");
            System.out.println("-------------" + nowStationStr);
            
            break;
          }
        }
        String tempStr = "无车辆";
        if (!"".equals(nowStationStr)) {
          tempStr = nowStationStr;
        }
        lineSB.append(routeInBaseLineOjb.getString("routename") + "   开往：" + routeInBaseLineOjb.getString("endstation") + "   目前靠近：" + tempStr + "\n");
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    resMap.put("msg", lineSB.toString());
    resMap.put("code", "0000");
    LOGGER.info("正常--####--出来BusGetPlace-》getPlaceInfo--####,返回" + resMap.toString() + "\n");
    return resMap;
  }
  
  public static void main(String[] args)
  {
    try
    {
      Map<String, String> resMap = new HashMap();
      resMap = getPlaceInfo("人民医院（北）");
      Map<String, String> tempMap = new HashMap();
      if ((resMap != null) && ("0000".equals(resMap.get("code"))) && 
        ("2".equals(resMap.get("size")))) {
        getPlace((String)resMap.get("stationid1"));
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
}
