package com.tian.menu;

import net.sf.json.JSONObject;

import com.tian.util.AccessTokenUtil;
import com.tian.util.HttpUtil;

public class MenuTest {
	public static void main(String[] args) {
		String token= AccessTokenUtil.getAccessToken("gh_1579fe4d87d1");
	String menu=
			 JSONObject.fromObject(MenuAction.initMenu()).toString();
			  System.out.println(menu);
			  int
			 i=MenuAction.createMenu(token,
			 menu);
			  System.out.println(i);
			 String tt=
			 HttpUtil.httpGet("https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+token);
			 System.out.println(tt);
}
}
