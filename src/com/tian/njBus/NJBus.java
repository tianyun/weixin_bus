package com.tian.njBus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.tian.util.DBUtilMysql;
import com.tian.util.HttpUtil;

/**
 * 南京公交基本类，1.数据库码表方法，2.获取实时信息方法，3.获取cookie
 * 
 * @author tianyun
 * 
 */
public class NJBus {
	protected static final Logger logger = Logger.getLogger(NJBus.class);

	/**
	 * 通过传入路线获取实时消息 <a style="font-size: 16px;" href=
	 * 
	 * @param busline
	 * @return
	 */
	public static String getBusInfo(String busline) {
		// 获取网页信息
		String Url = "http://njbus.zhihui.cc/njgjc/webapp.do?method=chooseStation&updownId="+busline;
		String resStr = HttpUtil.httpGet(Url);
		// 获取路线
		String lineStas = "";
		Pattern pattern1 = Pattern.compile(">&nbsp;&nbsp;(.*?)</a>");
		Matcher matcher1 = pattern1.matcher(resStr);
		int i = 0;
//		List<String> statesList = new ArrayList<String>();
		String resultString="";
		while (matcher1.find()) {
			i = i + 1;
			// System.out.println(matcher.group(0));
			// System.out.println(matcher.start());
			// System.out.println(matcher.end());
			lineStas = i + lineStas + matcher1.group(0).replace(">&nbsp;&nbsp;", "").replace("</a>", "") + "\n";
		}
		System.out.println(lineStas);
		// 查找各站点，开始遍历
		Pattern pattern2 = Pattern.compile("href=\"(.*?)\"");
		Matcher matcher2 = pattern2.matcher(resStr);
		int j = 0;
		
		while (matcher2.find()) {
			String host = "http://njbus.zhihui.cc";
			try {
				URL url = new URL(host + matcher2.group(0).replace("href=\"", "").replace("\"", ""));
				HttpURLConnection resumeConnection = (HttpURLConnection) url.openConnection();
				// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限

				String JSESSIONID = getCookie(Url);

				String cookie = "JSESSIONID=0ECD7AFAACF28A3D49B4A3E1BA1BBB2F";
				resumeConnection.setRequestProperty("Cookie", cookie);
				// System.out.println(cookie);
				resumeConnection.connect();
				InputStream urlStream = resumeConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "UTF-8"));

				String result = null;
				String resString = "";
				while ((result = bufferedReader.readLine()) != null) {
					// System.out.println(result);
					resString = resString + result + "\n";
				}
				bufferedReader.close();

				// 获取当前站点
				Pattern patterninfo = Pattern.compile("当前站点:<span class=\"red\">(.*?)</span>");
				Matcher matcher3 = patterninfo.matcher(resString);
				j = j + 1;
				// System.out.println(matcher1.find());
				String nowSates = "";
				if (matcher3.find()) {
					nowSates = matcher3.group(0).replace("当前站点:<span class=\"red\">", "").replace("</span>", "");
					System.out.println(
							j + matcher3.group(0).replace("当前站点:<span class=\"red\">", "").replace("</span>", ""));
				}
				// 获取当前信息
				Pattern patterninfonow = Pattern.compile("<span class=\"red\">0</span>站地");
				Matcher matcher4 = patterninfonow.matcher(resString);
				if (matcher4.find()) {
					System.out.println("=" + matcher4.group(0));

					nowSates = "=" + nowSates;
					System.out.println(nowSates);

				}
				// 获取当前信息
				Pattern patterninfonow1 = Pattern.compile("<span class=\"red\">1</span>站地");
				Matcher matcher5 = patterninfonow1.matcher(resString);
				if (matcher5.find()) {
					System.out.println(">" + matcher5.group(0));
					nowSates = ">" + nowSates;
					System.out.println(nowSates);

				}
				System.out.println("add:" + nowSates);
//				statesList.add(nowSates);
				resultString = resultString + nowSates+"\n";
			} catch (Exception e) {
				logger.error("！！错误--####--获取实时消息有错误", e);
			}

		}
		System.out.println("last:" + resultString);
		
		return resultString;
	}

	public static String getCookie(String urlStr) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(urlStr);
		try {
			HttpResponse response = httpclient.execute(httpget);
			CookieStore cookieStore = httpclient.getCookieStore();
			// System.out.println(cookieStore.getCookies());
			String[] arrs = cookieStore.getCookies().toString().split("]");
			System.out.println(arrs[14].replace("[value: ", ""));
			return arrs[14].replace("[value: ", "");
		} catch (Exception e) {
			logger.error("！！错误--####--南京公交，获取cookie失败", e);
			e.printStackTrace();
			return "0003";
		}

	}

	/**
	 * 制作公交车对应码表
	 */
	public void get_busline_comp() {
		for (int i = 0; i < 1100; i++) {
			System.out.println("第" + i + "次: " + get_busline_info(i));
		}
	}

	/**
	 * 传入数字，获取公交路线 <span class="red">14路</span>
	 * 
	 * @param num
	 * @return
	 */
	public static String get_busline_info(int num) {
		String Url = "http://njbus.zhihui.cc/njgjc/webapp.do?method=chooseStation&updownId=" + num;
		String resStr = HttpUtil.httpGet(Url);
		String res = "0";
		Pattern pattern = Pattern.compile("<span class=\"red\">(.*)</span>");
		Matcher matcher = pattern.matcher(resStr);
		if (matcher.find()) {
			if ("".equals(matcher.group(0)) || matcher.group(0) == null) {
				System.out.println("没有对应数据");
			} else {

				// System.out.println(matcher.group(0));
				String insert_netline = matcher.group(0).replace("<span class=\"red\">", "").replace("</span>", "");
				if (num % 2 == 0) {
					insert_NJGJ("" + num, insert_netline + "_2");
					System.out.println("获取路线：" + insert_netline + "_2");

				} else {
					insert_NJGJ("" + num, insert_netline);
					System.out.println("获取路线：" + insert_netline);

				}
				res = "1";
			}
		} else {
			System.out.println("找不到对应的数据");
			res = "找不到对应的数据";
		}
		return res;
	}

	/**
	 * 把公交路线和对应的网页数字插入数据库
	 * 
	 * @param busline
	 * @param netline
	 */
	public static void insert_NJGJ(String busline, String netline) {
		try {
			Connection conn = DBUtilMysql.getConnection();
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO wx_njgj_busline( WX_NJ_BUSLINE , WX_NJ_NETLINE ) VALUE('" + busline + "' , '"
					+ netline + "' )";
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		// System.out.println((int)((new Date().getTime())/1000));
		getBusInfo("2");
		// getCookie();
		// String str ="[[version: 0][name:
		// Hm_lpvt_d2feb2a25015d9085e35393fc9143538][value: 1446128370][domain:
		// njbus.zhihui.cc][path: /][expiry: null], [version: 0][name:
		// Hm_lvt_d2feb2a25015d9085e35393fc9143538][value: 1446128370][domain:
		// njbus.zhihui.cc][path: /][expiry: Fri Oct 28 22:19:30 CST 2016],
		// [version: 0][name: JSESSIONID][value:
		// 7AC0110D2D2CABE837ED60588124D366][domain: njbus.zhihui.cc][path:
		// /njgjc/][expiry: null]]";
		// String [] arrs=str.split("]");
		// System.out.println(arrs[14].replace("[value: ", ""));
		// for (int i = 0; i < arrs.length; i++) {
		// String string = arrs[i];
		// System.out.println(string);
		// }
	}

}
