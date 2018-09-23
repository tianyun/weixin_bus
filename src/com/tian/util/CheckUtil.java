package com.tian.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CheckUtil {
	private static final String token = "weixin";

	/**
	 * 检验signature 方法：1. 将token、timestamp、nonce三个参数进行字典序排序 2.
	 * 将三个参数字符串拼接成一个字符串进行sha1加密 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 排序
		Arrays.sort(arr);

		// 生成字符串
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}

		// sha1 加密
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());//getBytes()方法是将字符串解码为字节数组
			tmpStr = byteToStr(digest);//将字节转换为十六进制的字符串
			System.out.println(tmpStr);
			System.out.println(signature.toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;//toUpperCase将字符串变成大写

	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);//byteToHexStr字节拼接函数
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}
}
