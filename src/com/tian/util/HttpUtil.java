package com.tian.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * http相关请求的封装
 * 
 * @param url
 * @return
 */
public class HttpUtil {

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的UR 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String httpGet(String URL) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(URL);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),"utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line+"";
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String httpPOST(String url, String param) {
		BufferedReader in = null;
		OutputStreamWriter osw= null;
		String result = "";
		try {
			URL realUrl = new URL(url);
//			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Server", "nginx/1.8.0");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type",
					"application/json; encoding=utf-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
//			conn.connect();
//			// 获取URLConnection对象对应的输出流
			 osw = new OutputStreamWriter(
					conn.getOutputStream(), "utf-8");
//			PrintWriter out = new PrintWriter(conn.getOutputStream());
			 // 发送请求参数
//			 out.print(param.getBytes("utf-8"));
//			 flush输出流的缓冲
//			 out.flush();
//			 out.close();
			osw.write(param);
			osw.flush();
			osw.close();
			//获取输入流
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				
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
		// 发送 GET 请求
		String s = HttpUtil
				.httpGet("https://api.weixin.qq.com/cgi-bin/user/get?access_token=TBu1i5a6dwrPTiDdyw0J9sfOvk_YmKdX5MGUBwJTEZ281cSZiLUfkBEDKO420E7lIb99MP8F1ggjApmfwxBIB9lelzzxau0bUCmnYg0WhVM&next_openid");
		System.out.println(s);

		// 发送 POST 请求
		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+AccessTokenUtil.getAccessToken("gh_e8a2c02918d5");
		 String param = "{" +
		 "\"touser\":\"o8c52uH_HTlls0Lhi8WILUmNcZ14\"," +
		 "\"msgtype\":\"text\"," +
		 "\"text\":" +
		 "{" +
		 "\"content\":\"Hello World\"" +
		 "}" +
		 "}";
		String tt = "你好";

//		String param = PushType.getType("text", tt,
//				"o8c52uH_HTlls0Lhi8WILUmNcZ14");
		String sr = HttpUtil.httpPOST(postUrl, param);
		System.out.println(sr);
	}
}
