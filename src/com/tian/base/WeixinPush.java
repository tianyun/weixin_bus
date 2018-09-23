package com.tian.base;

import org.apache.log4j.Logger;

import com.tian.entity.PushType;
import com.tian.util.AccessTokenUtil;
import com.tian.util.WeixinUtil;
import com.tian.web.MakeFriend.MakeFriend_To;

/**
 * 平台消息推送基类
 * 
 * @author Dell
 * 
 */
public class WeixinPush {
	protected static final Logger LOGGER = Logger.getLogger(WeixinPush.class);
	/**
	 * 微信openid
	 */
	private String OPENID_ = "";

	public WeixinPush() {
		// TODO Auto-generated constructor stub
	}

	public WeixinPush(String openid) {
		this.OPENID_ = openid;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 消息组装， 传入类型和内容
	 * 
	 * @param content
	 * @param type
	 * @return
	 */
	public String pushTextMsg(String content, String ToOpenid) {
		String postUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
				+ AccessTokenUtil.getAccessToken(OPENID_);
		String param = PushType.getType("text", content, ToOpenid);

		LOGGER.info("正常--####--WeixinPush ->pushTypeMsg，推送内容：" + param);

		net.sf.json.JSONObject jsonData = WeixinUtil.doPostStr(postUrl, param);

		try {
			// JSONObject jsonData = new JSONObject(resulStr);
			String res = jsonData.getString("errcode");
			LOGGER.info("正常--####--WeixinPush ->pushTypeMsg，推送内容返回报文：" + res.toString());

			if ("0".equals(res)) {

				LOGGER.info("正常--####--WeixinPush ->pushTypeMsg，消息推送成功：");
				return "0000";
			} else {
				LOGGER.error("错误--####--WeixinPush ->pushTypeMsg，消息推送失败");
				return "0001";
			}

		} catch (Exception e) {
			LOGGER.error("！！错误--####--WeixinPush ->pushTypeMsg，json格式转换错误", e);
			return "0001";
		}

	}
	
	public static void main(String[] args) {
		WeixinPush wx = new WeixinPush("gh_1579fe4d87d1");
		wx.pushTextMsg("你好", "o1IU4xDQxlIMeEaWAmDpqdv2-uts");
//		String customUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1")+"&openid="+"o1IU4xDQxlIMeEaWAmDpqdv2-uts"+"&lang=zh_CN";
//		net.sf.json.JSONObject jsonData= WeixinUtil.doGetStr(customUrl);
//		System.out.println(jsonData);
	}
}
