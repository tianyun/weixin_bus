package api;

import com.tian.util.BusHttpUtil;
import com.tian.util.DBUtilMysql;
import com.tian.xcBus.Bus;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class BusInfo
{
  private static final Logger LOGGER = Logger.getLogger(Bus.class);
  private static String BUSLINENAMEJUDGE = "http://120.77.82.161/queryRouteStation/routeAndStation?citycode=330624&name=";
  private static String BUSLINEINFO = "http://120.77.82.161/travel/routeDetail?routeid=routeidValue&longitude=112.945945&latitude=28.174596&direction=directionValue&citycode=330624";
  private static String BUSREAL = "http://120.77.82.161/travel/realTime?routeid=routeidValue&stationid=5412&direction=directionValue&userstationno=2&citycode=330624";
  
  public static Map<String, Object> getBusLine(String lineName)
  {
    LOGGER.info("正常--####--进入BusInfo-》getBusLine--####,传入参数lineName：" + lineName);
    
    Map<String, Object> resMap = new HashMap();
    if ((lineName == null) || ("".equals(lineName)))
    {
      resMap.put("code", "0001");
      resMap.put("msg", "busline 传入空值");
      LOGGER.error("！！错误--####--新昌公交,BusInfo-》getBusLine####busline 传入空值");
      return resMap;
    }
    if (("8".equals(lineName)) || ("8路".equals(lineName)))
    {
      resMap.put("size", "2");
      resMap.put("msg", "81");
      resMap.put("code", "0000");
      return resMap;
    }
    if (("1".equals(lineName)) || ("1路".equals(lineName)))
    {
      resMap.put("size", "2");
      resMap.put("msg", "11");
      resMap.put("code", "0000");
      return resMap;
    }
    if (("2".equals(lineName)) || ("2路".equals(lineName)))
    {
      resMap.put("size", "2");
      resMap.put("msg", "21");
      resMap.put("code", "0000");
      return resMap;
    }
    if (("3".equals(lineName)) || ("3路".equals(lineName)))
    {
      resMap.put("size", "2");
      resMap.put("msg", "31");
      resMap.put("code", "0000");
      return resMap;
    }
    String lineJudge = BUSLINENAMEJUDGE + lineName;
    String result = BusHttpUtil.BusHttp(lineJudge);
    if (!"0001".equals(result)) {
      try
      {
        JSONObject jsonData = new JSONObject(result);
        JSONObject objJsonData = (JSONObject)jsonData.get("obj");
        JSONArray routesJsonData = (JSONArray)objJsonData.get("routes");
        
        List<JSONObject> resList = new ArrayList();
        if (routesJsonData.length() > 0) {
          for (int i = 0; i < routesJsonData.length(); i++) {
            resList.add((JSONObject)routesJsonData.get(i));
          }
        }
        if (resList.size() > 2)
        {
          String str = "";
          for (int i = 0; i < resList.size(); i += 2)
          {
            JSONObject obj = (JSONObject)resList.get(i);
            str = str + obj.getString("routename") + "   ";
          }
          resMap.put("size", resList.size());
          resMap.put("msg", "你可能想找以下路线，请输入精确名字：\n" + str);
          resMap.put("code", "0000");
        }
        else if (resList.size() == 2)
        {
          resMap.put("size", resList.size());
          resMap.put("msg", ((JSONObject)resList.get(1)).getString("routeid"));
          resMap.put("code", "0000");
        }
        else
        {
          resMap.put("size", "0");
          resMap.put("msg", "没有该路车");
          resMap.put("code", "0002");
        }
      }
      catch (Exception e)
      {
        LOGGER.error("！！错误--####--新昌公交,BusInfo-》getBusLine####判断路线有问题 有异常", e);
      }
    }
    LOGGER.info("正常--####--出来BusInfo-》getBusLine--####,返回" + resMap.toString() + "\n");
    
    return resMap;
  }
  
  public static Map<String, Object> getBusLineInfo(String routeid, String direction)
  {
    Map<String, Object> resMap = new HashMap();
    LOGGER.info("正常--####--进入BusInfo-》getBusLineInfo--####,传入参数routeid：" + routeid + "；direction" + direction);
    if ((routeid == null) || ("".equals(routeid)))
    {
      resMap.put("code", "0001");
      resMap.put("msg", "busline 传入空值");
      LOGGER.error("！！错误--####--新昌公交,BusInfo-》getBusLineInfo####busline desc传入空值");
      return resMap;
    }
    String lineInfo = BUSLINEINFO.replace("routeidValue", routeid).replace("directionValue", direction);
    String result = BusHttpUtil.BusHttp(lineInfo);
    try
    {
      JSONObject jsonData = new JSONObject(result);
      JSONObject objJsonData = (JSONObject)jsonData.get("obj");
      
      List<JSONObject> stationList = null;
      


      JSONArray stationJsonArr = (JSONArray)objJsonData.get("station");
      List<JSONObject> tempList = new ArrayList();
      for (int i = 0; i < stationJsonArr.length(); i++)
      {
        JSONObject tempData = new JSONObject(stationJsonArr.getString(i));
        tempList.add(tempData);
      }
      stationList = tempList;
      



      String realInfoUrl = BUSREAL.replace("routeidValue", routeid).replace("directionValue", direction);
      String realRE = BusHttpUtil.BusHttp(realInfoUrl);
      JSONObject realJsonData = new JSONObject(realRE);
      
      JSONArray carListJsonArr = (JSONArray)realJsonData.get("obj");
      JSONObject carAttTemp;
      for (int i = 0; i < carListJsonArr.length(); i++)
      {
        JSONObject carTemp = new JSONObject(carListJsonArr.getString(i));
        carAttTemp = (JSONObject)carTemp.get("desc");
        for (int j = 0; j < stationList.size() - 1; j++)
        {
          JSONObject stationTemp = (JSONObject)stationList.get(j);
          if (carAttTemp.getString("stationid").equals(stationTemp.getString("stationid"))) {
            if ("0".equals(carAttTemp.getString("type"))) {
              stationTemp.put("name", "=" + stationTemp.getString("name"));
            } else {
              ((JSONObject)stationList.get(j + 1)).put("name", ">" + ((JSONObject)stationList.get(j + 1)).getString("name"));
            }
          }
        }
      }
      StringBuilder resSB = new StringBuilder();
      for (JSONObject jsonObject : stationList)
      {
        if ((!jsonObject.getString("name").contains(">")) && (!jsonObject.getString("name").contains("="))) {
          jsonObject.put("name", "   " + jsonObject.getString("name"));
        }
        resSB.append(jsonObject.getString("name") + "\n");
      }
      JSONObject busObj = (JSONObject)objJsonData.get("route");
      resMap.put("busBean", stationList);
      resMap.put("routename", busObj.getString("routename"));
      resMap.put("desc", busObj.getString("startstation") + "-->" + busObj.getString("endstation"));
      resMap.put("time", busObj.getString("starttime") + ":" + busObj.getString("endtime"));
      
      resMap.put("msg", resSB.toString());
      resMap.put("code", "0000");
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--新昌公交,busInfo->获取线路信息有问题", e);
    }
    return resMap;
  }
  
  public static Map<String, String> dealInfo(Map<String, String> busInfo)
  {
    LOGGER.info("正常--####--进入BusInfo-》dealInfo--####,传入参数busInfo：" + busInfo.toString());
    
    Map<String, String> resMap = new HashMap();
    try
    {
      if (busInfo == null)
      {
        resMap.put("code", "0001");
        resMap.put("msg", "busInfo 传入空值");
        LOGGER.error("！！错误--####--新昌公交,BusInfo-》dealInfo####busInfo 传入空值");
        
        return resMap;
      }
      String live = (String)busInfo.get("live");
      String stas = (String)busInfo.get("stas");
      

      stas = stas.replace("[", "");
      stas = stas.replace("]", "");
      
      String[] stasArr = stas.split(",");
      String desc = stasArr[0] + " 开往 " + stasArr[(stasArr.length - 1)];
      if (!"[]".equals(live))
      {
        live = live.replace("],[", "#");
        live = live.replace("]", "");
        live = live.replace("[", "");
        
        String[] liveArr = live.split("#");
        for (int i = 0; i < liveArr.length; i++)
        {
          String[] liveinfo = liveArr[i].split(",");
          int n = Integer.parseInt(liveinfo[0]);
          int m = Integer.parseInt(liveinfo[1]);
          if (m == 1) {
            stasArr[(n - 1)] = ("=" + stasArr[(n - 1)]);
          } else if (n + 1 > stasArr.length) {
            stasArr[(n - 1)] = (">" + stasArr[(n - 1)]);
          } else {
            stasArr[n] = (">" + stasArr[n]);
          }
        }
      }
      String resutlStas = "";
      for (int i = 0; i < stasArr.length; i++)
      {
        stasArr[i] = stasArr[i].replaceAll("\"", "");
        if ((!stasArr[i].contains(">")) && (!stasArr[i].contains("="))) {
          resutlStas = resutlStas + "  " + stasArr[i] + "\n";
        } else {
          resutlStas = resutlStas + stasArr[i] + "\n";
        }
      }
      resMap.put("errcode", "0000");
      resMap.put("stas", resutlStas);
      resMap.put("desc", desc);
      LOGGER.info("正常--####--出来BusInfo-》dealInfo--####,返回resMap\n");
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--新昌公交,busInfo->dealInfo有问题", e);
      return resMap;
    }
    return resMap;
  }
  
  public static String dealBusContent(String Content)
  {
    LOGGER.info("正常--####--进入BusInfo-》dealBusContent--####,传入参数Content：" + Content.toString());
    
    String[] con = Content.split(" ");
    String desc = "0";
    String resString = "";
    if (con.length == 2)
    {
      desc = "1";
      Content = con[0];
    }
    Map<String, Object> lineMap = getBusLine(Content);
    if ("0000".equals(lineMap.get("code"))) {
      if ("2".equals((String)lineMap.get("size")))
      {
        Map<String, Object> infoMap = getBusLineInfo((String)lineMap.get("msg"), desc);
        if ("0000".equals(infoMap.get("code")))
        {
          resString = (String)infoMap.get("msg");
          String wether = (String)Wether.getWetherInfo("新昌").get("wether_info");
          
          resString = wether + "路线:" + (String)infoMap.get("routename") + "\n方向：" + 
            (String)infoMap.get("desc") + "\n时间：" + (String)infoMap.get("time") + 
            "\n“>”表示快到，“=”表示停靠\n" + resString + "\n\n发送路线+空格+2 查询反向，如3路  2\n\n-->查询地址发送准确名称即可，包括城乡线";
        }
      }
      else
      {
        resString = (String)lineMap.get("msg");
      }
    }
    LOGGER.info("正常--####--出来BusInfo-》dealBusContent--####,返回resString" + resString + "\n");
    
    return resString;
  }
  
  public static String getBusTime(String Num)
  {
    try
    {
      InputStream inStream = DBUtilMysql.class.getResourceAsStream("/bus_time.properties");
      Properties prop = new Properties();
      
      prop.load(inStream);
      if ((prop.getProperty("bus." + Num) == null) || ("".equals(prop.getProperty("bus." + Num)))) {
        return "";
      }
      return "\n首末班时间：" + prop.getProperty(new StringBuilder("bus.").append(Num).toString());
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--新昌公交,busInfo->getBusTime问题", e);
    }
    return "";
  }
  
  public static void main(String[] args)
  {
    BusInfo busInfo = new BusInfo();
    String resMap = dealBusContent("2 2");
    System.out.println(resMap.toString());
  }
}
