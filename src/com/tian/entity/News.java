package com.tian.entity;

public class News {
	private String Title;
	private String Description;
	private String PicUrl;
	private String Url;

	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		this.Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String urls) {
		Url = urls;
	}

	@Override
	public String toString() {
		return "NewsMessage [title=" + Title + ", Description=" + Description
				+ ", PicUrl=" + PicUrl + ", Urls=" + Url + "]";
	}

	
}
