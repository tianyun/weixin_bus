package com.tian.util;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import java.io.PrintStream;
import java.util.Date;
import org.apache.log4j.Logger;

public class MemCachedUtil
{
  protected static final Logger LOGGER = Logger.getLogger(MemCachedUtil.class);
  private static MemCachedClient client = null;
  
  public static MemCachedClient getConnectMem()
  {
    client = new MemCachedClient();
    return client;
  }
  
  static
  {
    String[] addr = { "127.0.0.1:11211" };
    Integer[] weights = { Integer.valueOf(10) };
    SockIOPool pool = SockIOPool.getInstance();
    pool.setServers(addr);
    pool.setWeights(weights);
    


    pool.setInitConn(10);
    pool.setMinConn(5);
    pool.setMaxConn(200);
    pool.setMaxIdle(900000L);
    
    pool.setMaintSleep(30L);
    pool.setNagle(false);
    pool.setSocketTO(30);
    pool.setSocketConnectTO(0);
    pool.initialize();
  }
  
  public String setMemCached(String key, Object value, Date date)
  {
    LOGGER.info("正常--####--进入MemCachedUtil-》setMemCached--####,传入参数：key" + key + ";value" + value);
    client = getConnectMem();
    try
    {
      client.set(key, value, date);
      LOGGER.info("正常--####--出来MemCachedUtil-》setMemCached--####,返回0000\n");
      
      return "0000";
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--MemCachedUtil->setMemCached--####，设置memcache数值有问题", e);
    }
    return "0001";
  }
  
  public String getMemCached(String key)
  {
    LOGGER.info("正常--####--进入MemCachedUtil-》getMemCached--####,传入参数：key" + key);
    client = getConnectMem();
    try
    {
      String resStr = (String)client.get(key);
      LOGGER.info("正常--####--出来MemCachedUtil-》getMemCached--####,返回resStr" + resStr + "\n");
      return resStr;
    }
    catch (Exception e)
    {
      LOGGER.error("！！错误--####--MemCachedUtil->getMemCached--####，获取memcache数值有问题", e);
    }
    return "0001";
  }
  
  public static void main(String[] args)
  {
    client = getConnectMem();
    

    String str = (String)client.get("test2");
    System.out.println(str);
  }
}
