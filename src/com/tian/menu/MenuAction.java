package com.tian.menu;

import java.io.IOException;

import com.tian.util.MessageUtil;
import com.tian.util.WeixinUtil;

import net.sf.json.JSONObject;

public class MenuAction {
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * 菜单组装
	 * 
	 * @return
	 */
	public static Menu initMenu() {
		//组装二级菜单
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("刷新重发");
		button11.setType("click");
		button11.setKey("flush");

		ClickButton button12 = new ClickButton();
		button12.setName("反向");
		button12.setType("click");
		button12.setKey("desc_to");
		
		ClickButton button21 = new ClickButton();
		button21.setName("其他查询");
		button21.setType("click");
		button21.setKey("serach_more");
		ClickButton button22 = new ClickButton();
		button22.setName("交友");
		button22.setType("click");
		button22.setKey("makefriend_to");

		ClickButton button31 = new ClickButton();
		button31.setName("问题反馈");
		button31.setType("click");
		button31.setKey("feedback");

		ViewButton button32 = new ViewButton();
		button32.setName("联系我们");
		button32.setType("view");
		button32.setUrl("http://mp.weixin.qq.com/s?__biz=MzI5MjAzODYxMg==&mid=214211086&idx=1&sn=cc77b96769aa7ff9dbf9c3883982c2c2#rd");

		//组装一级菜单
		Button button3 = new Button();
		button3.setName("联系我们");
		button3.setSub_button(new Button[] { button31, button32 });
		
		Button button2 = new Button();
		button2.setName("新功能");
		button2.setSub_button(new Button[] { button22, button21 });
		
		Button button1 = new Button();
		button1.setName("反向");
		button1.setSub_button(new Button[] { button11, button12 });
		menu.setButton(new Button[] { button1, button2, button3 });

		return menu;
	}

	/**
	 * 创建菜单
	 * 
	 * @param token
	 * @param menu
	 * @return
	 */
	public static int createMenu(String token, String menu) {
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = WeixinUtil.doPostStr(url, menu);
		if (jsonObject != null) {
			result = jsonObject.getInt("errcode");
		}
		return result;
	}

	
	/**
	 * 查询菜单
	 * 
	 * @param token
	 * @return
	 */
	public static JSONObject queryMenu(String token) {
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = WeixinUtil.doGetStr(url);
		return jsonObject;
	}

	/**
	 * 删除菜单
	 * 
	 * @param token
	 * @return
	 * @throws IOException
	 */
	public static int deleteMenu(String token) throws IOException {
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = WeixinUtil.doGetStr(url);
		int result = 0;
		if (jsonObject != null) {
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
}
