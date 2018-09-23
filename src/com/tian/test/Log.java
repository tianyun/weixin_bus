package com.tian.test;

import net.sf.json.JSONObject;

import com.tian.entity.PushType;
import com.tian.menu.MenuAction;
import com.tian.util.HttpUtil;
import com.tian.util.WeixinUtil;

public class Log {

	public static void main(String[] args) {
//		  String menu=
//		 JSONObject.fromObject(MenuAction.initMenu()).toString();
//		  System.out.println(menu);
//		  int
//		 i=MenuAction.createMenu("iEl3KXeucagsnxObH_qwTfGYivAQK-6rWtTlWsc_CZlVvdq5gbehAT94-8giBPJ4VBI1a9xcV8cL5ZCN-im0N11P4djEqM5JiBJ9DgcfVzo",
//		 menu);
//		  System.out.println(i);
		 String tt=
		 HttpUtil.httpGet("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=iEl3KXeucagsnxObH_qwTfGYivAQK-6rWtTlWsc_CZlVvdq5gbehAT94-8giBPJ4VBI1a9xcV8cL5ZCN-im0N11P4djEqM5JiBJ9DgcfVzo");
		 System.out.println(tt);
//		String param = PushType.getType("text", "nihp",
//				"o8c52uH_HTlls0Lhi8WILUmNcZ14");
//		System.out.println(param);
//		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=qCsxf8Nm-viWoyAY35_LEtwq__BeiSfn_Qf6DIw8v7ZbjHIPRAVpDvUg9tVbVii050tHWzEkXPL6qT62evm8nW_6ra3ypnp5SoTbjuHeZzg";
//		String sr = HttpUtil.httpPOST(postUrl, param);
//		System.out.println(sr);
	}

	public static String getNam() {
		String tt = HttpUtil
				.httpGet("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=gI2k4WxYY5VddOaoexOUdyd2Gx0no3bbYcLRQoG61NODvmlqMBQaicEXN0v-hQHS1Rmh9ONJif2snYv2Nn9It9QRmyoLZr9oizstp5QTgr4");
		return tt;
	}
}