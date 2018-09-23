package com.tian.base;

/**
 * 微信公共平台id  和 类名的关系类
 * @author Dell
 *
 */
public class WeixinContent {
	String id;
	String CONTENT_OPEN;
	String CONTENT_CLASS_NAME;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCONTENT_OPEN() {
		return CONTENT_OPEN;
	}

	public void setCONTENT_OPEN(String cONTENT_OPEN) {
		CONTENT_OPEN = cONTENT_OPEN;
	}

	public String getCONTENT_CLASS_NAME() {
		return CONTENT_CLASS_NAME;
	}

	public void setCONTENT_CLASS_NAME(String cONTENT_CLASS_NAME) {
		CONTENT_CLASS_NAME = cONTENT_CLASS_NAME;
	}

	@Override
	public String toString() {
		return "WeixinContent [id=" + id + ", CONTENT_OPEN=" + CONTENT_OPEN
				+ ", CONTENT_CLASS_NAME=" + CONTENT_CLASS_NAME + "]";
	}

}
