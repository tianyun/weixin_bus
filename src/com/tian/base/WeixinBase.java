package com.tian.base;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * 
 * 所有微信基类
 * 
 * @author zty
 * 
 */
public abstract class WeixinBase {
	protected static final Logger LOGGER = Logger.getLogger(WeixinBase.class);
	/**
	 * 用户发送信息
	 */
	private Map<String, String> params;

	/**
	 * 公众号信息
	 */
	private WeixinContent weixinContent;
	
	/**
	 * 微信数据库相关操作
	 */
	private WeixinMessage weixinMessage;
	private String ToUserName;
	private String FromUserName;
	private String CreateTime;

	public WeixinBase(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public WeixinContent getWeixinContent() {
		return weixinContent;
	}

	public void setWeixinContent(WeixinContent weixinContent) {
		this.weixinContent = weixinContent;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public WeixinMessage getWeixinMessage() {
		return weixinMessage;
	}

	public void setWeixinMessage(WeixinMessage weixinMessage) {
		this.weixinMessage = weixinMessage;
	}

	// 反射方法
	public String exec() throws Exception {
		Class cee = this.getClass();
		String methodType = params.get("MsgType");
		LOGGER.info("正常--####--传入的消息类型：" + methodType);

		if ("event".equals(methodType)) {
			String methodName = params.get("Event");
			LOGGER.info("正常--####--执行的事件名：" + methodName);

			Method method = cee.getDeclaredMethod(methodName + "Event");
			LOGGER.info("正常--####--执行方法名：" + method.toString());

			return (String) method.invoke(this);
		} else {
			Method method = cee.getDeclaredMethod(methodType);
			return (String) method.invoke(this);
		}

	}

	// 文本消息
	public abstract String text();

	// 图片消息
	public abstract String image();

	// 语音消息
	public abstract String voice();

	// 视频消息
	public abstract String video();

	// 地理位置消息（用户发送）
	public abstract String location();

	// 链接消息
	public abstract String link();

	// 关注时消息
	public abstract String subscribeEvent();

	// 获取用户地理位置消息
	public abstract String LOCATIONEvent();

	// 自定义菜单点击消息
	public abstract String CLICKEvent();

	// url跳转消息
	public abstract String VIEWEvent();


	

}
