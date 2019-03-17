package com.tian.csbus;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.tian.util.ErrorInfo;


public class GetBusInfo {
	static String publicKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQp/621mbNQKRjvNo821KTJPsUW3Qr2IPXj5YqzrNgtJBpg6prEnFreLF/Hz39rJ9FQScJ2FhE3f/0bK0Hjg1Ib12N490lKoZKPnhJ/mBZuJWI6PWFwEEOugvJ8PjYeFAeWbOmYUGWDkdM0qEwyKBx6Ao8yAYw0kCzy6/lsSaidQIDAQAB";
	protected static final Logger LOGGER = Logger.getLogger(GetBusInfo.class);

	/**
	 * 传入参数 和 不相同的加密内容
	 * 
	 * @param url
	 * @param encrypParam
	 * @return
	 */
	public String busUtil(String url, String encrypParam) {
	    LOGGER.info("正常--####--进入GetBusInfo-》busUtil--####,传入参数url：" + url+" encrypParam:"+encrypParam);

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
		int year = c.get(Calendar.YEAR);
		int month1 = c.get(Calendar.MONTH) + 1;
		int day1 = c.get(Calendar.DAY_OF_MONTH);
		int hour1 = c.get(Calendar.HOUR_OF_DAY);
		int Minute1 = c.get(Calendar.MINUTE);
		String month = month1 < 10 ? "0" + month1 : "" + month1;
		String day = day1 < 10 ? "0" + day1 : "" + day1;
		String hour = hour1 < 10 ? "0" + hour1 : "" + hour1;
		String Minute = Minute1 < 10 ? "0" + Minute1 : "" + Minute1;

		String newdate = "" + year + "-" + month + "-" + day + " " + hour + ":" + Minute;
		String ak = UUID.randomUUID().toString();
		// 加密内容
		String encryption = encrypParam+ "&ak=" + ak + "&sTime="
				+ newdate;
		// 加密后内容
		String messageEn = null;
		try {
			messageEn = RSAUtil.encrypt(encryption, publicKEY);
			messageEn = URLEncoder.encode(messageEn, "utf-8");

			//LOGGER.info("加密内容:   " + encryption + "\n加密后的字符串为:   " + messageEn);
			// 模拟请求
			String param = "TYPE=851202&" + "WH=" + messageEn;
			String result = sendPost(url, param);
			LOGGER.info("正常--####--出来GetBusInfo-》busUtil--####,返回resString：" + result+"\n");

			return result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ErrorInfo.INTERFACEERROR.getName();
		}
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url   发送请求的 URL
	 * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws UnsupportedEncodingException
	 */
	public String sendPost(String url, String param) throws UnsupportedEncodingException {
		//LOGGER.info("url: " + url + "\nparam: " + param);
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "application/json, text/javascript, */*; q=0.01");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.setRequestProperty("Connection", "Connection: keep-alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			LOGGER.error("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		GetBusInfo getBusInfo = new GetBusInfo();
		String url = "http://58.20.113.233:9000/csTravel/lineQuery/queryStationByLinename";
		String encrypParam = "linename="+ "湖南大学站";
		getBusInfo.busUtil(url, encrypParam);
	}
}
