package com.tian.base;

/**
 * 微信公共平台消息发送 固定部分内容基类
 * @author Dell
 *
 */
public class WeixinMessage {
	private String MsgType;
	private String FromUserName;
	private String ToUserName;
	private String CreateTime;

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

}
