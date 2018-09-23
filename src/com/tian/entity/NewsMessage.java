package com.tian.entity;

import java.util.ArrayList;
import java.util.List;

public class NewsMessage extends BaseMessage {
	private int ArticleCount;
	private List<News> Articles;
	public int getArticleCount() {
		return ArticleCount;
	}
	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}
	public List<News> getArticles() {
		return Articles;
	}
	public void setArticles(List<News> articles) {
		this.Articles = articles;
	}
	
	/**
	 * 关键字是3的图文回复
	 * 
	 * @return
	 */
	public static List<News> newsMenu() {
		News news = new News();
		news.setTitle("新昌公交问题（需求）反馈");
		news.setDescription("请填写您的需求，或者直接添加我们微信好友");
		news.setPicUrl("http://nbut-sucai.stor.sinaapp.com/qqq.png");
		news.setUrl("http://tianyun.tunnel.mobi/WEIXIN/MakeFriend/MakeFriend_view.html");
		List<News> newsList = new ArrayList<News>();
		newsList.add(news);
//		 news = new News();
//		news.setTitle("宁工介绍");
//		news.setDescription("位于宁波市江北区");
//		news.setPicUrl("http://nbut-img.stor.sinaapp.com/oTDuCuKMvjnJ1hCHxa1hFtalochg.jpg");
//		news.setUrl("http://www.baidu.com");
//		newsList.add(news);

		return newsList;
	}
	
}
