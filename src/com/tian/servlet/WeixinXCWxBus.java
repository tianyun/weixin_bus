package com.tian.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sina.sae.memcached.SaeMemcache;
import com.tian.base.WeixinBase;
import com.tian.entity.News;
import com.tian.util.AccessTokenUtil;
import com.tian.util.MessageUtil;
import com.tian.util.WeixinUtil;
import com.tian.web.MakeFriend.MakeFriend_To;
import com.tian.xcBus.BusAction;

/**
 * 新昌公交专用 纯java 接口
 * @author tianyun
 *
 */
public class WeixinXCWxBus extends WeixinBase {
	protected static final Logger LOGGER = Logger
			.getLogger(WeixinXCWxBus.class);

	Map<String, String> params;

	public WeixinXCWxBus(Map<String, String> params) {
		super(params);
		this.params = params;
	}

	@Override
	public String text() {
		String Content = this.params.get("Content");
		String ToUserName = this.params.get("ToUserName");
		String FromUserName = this.params.get("FromUserName");
		LOGGER.info("正常--####--进入weixin_test_text方法" + params.toString());
		String message = null;
		String resString = "";
		// 交友功能
		if (Content.startsWith("@")) {
			// 从缓存获取盆友openid
			SaeMemcache mc = null;
			String mcFromUserName = null;
			mc = new SaeMemcache();
			mc.init();
			mcFromUserName = mc.get(FromUserName);
			if (mcFromUserName==null) {
				message = MessageUtil.initText(ToUserName, FromUserName,
						"目前没有人发消息给你哦");
				return message;
			}
//			if (mcFromUserName.equals(FromUserName)) {
//				message = MessageUtil.initText(ToUserName, FromUserName,
//						"同一个人");
//				return message;
//			}
//			String fromUserInfo = WeixinCustoms.getCustomInfo(FromUserName);
//			JSONObject jsonData;
			String customUrl="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+AccessTokenUtil.getAccessToken("gh_1579fe4d87d1")+"&openid="+FromUserName+"&lang=zh_CN";
			net.sf.json.JSONObject jsonData= WeixinUtil.doGetStr(customUrl);
			try {
//				jsonData = new JSONObject(fromUserInfo);
				String Name = jsonData.getString("nickname");
				// 发送给对方
				String tt="";
				if ( mc.get(mcFromUserName)==null) {
					tt="快速回复可回复@+内容\n\n";
				}
				Content=Content.replace("@", "");
				String errcode = MakeFriend_To.pushToCustom(mcFromUserName,
						Name + " 发来消息：\n" + Content + "\n\n"+tt+"或者点击<a href='http://1.wxfinal.sinaapp.com/MakeFriend/MakeFriend_view.html?FromUserName="+mcFromUserName+"&tocustom="+FromUserName+"'>此处回复</a>");
				//													点击<a href='http://1.nbut.sinaapp.com/jk/making_friend/customer_net.php?fromusername=".$fromUsername."&tousername=".$val."'>这里回复</a>
				if ("0000".equals(errcode)) {
					// 发送给自己
					message = MessageUtil.initText(ToUserName, FromUserName,
							"对方已收到");
					mc.set(mcFromUserName, FromUserName, 1800);
					return message;
				} else {
					message = MessageUtil.initText(ToUserName, FromUserName,
							"消息：" + Content + "\n没有发送成功，可能是对方不在线哦");
					return message;
				}
			} catch (Exception e) {
				LOGGER.error("！！错误--####--新昌无线公交，json解析错误");
				message = MessageUtil.initText(ToUserName, FromUserName, "消息："
						+ Content + "\n没有发送成功，json解析错误，请联系小编");
				return message;
			}

		} else {
			Content = Content.replace("路", "");
			Map<String, String> busMap = new HashMap<String, String>();
			busMap = BusAction.getBusWx(Content);
			if ("0000".equals(busMap.get("errcode"))) {
				resString = busMap.get("info");
				SaeMemcache mc = new SaeMemcache();
				mc.init();
				mc.set(FromUserName, Content, 1800);
			} else {
				resString = busMap.get("info");

			}

		}

		message = MessageUtil.initText(ToUserName, FromUserName, resString);
		LOGGER.info("正常--####--新昌公交返回message 数据： " + message);
		return message;
	}

	@Override
	public String image() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String voice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String video() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String location() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String link() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String subscribeEvent() {
		LOGGER.info("正常--####--新昌公交，关注事件");
		String message = "";
		String ToUserName = this.params.get("ToUserName");
		String FromUserName = this.params.get("FromUserName");

		String resString = "欢迎关注新昌无线公交\n\n发送数字查询公交\n发送空格+2查询反向 \n\n如查询：3路\n发送“3”或者“3 2”查询反向";
		message = MessageUtil.initText(ToUserName, FromUserName, resString);
		LOGGER.info("正常--####--新昌公交，返回数据" + message);
		return message;
	}

	@Override
	public String LOCATIONEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String CLICKEvent() {
		LOGGER.info("正常--####--开始进入新昌无线公交点击事件");
		String message = null;
		SaeMemcache mc = null;
		String mcValue = "";
		try {
			mc = new SaeMemcache();
			mc.init();
			mcValue = mc.get(this.params.get("FromUserName"));
			LOGGER.info("正常--####--从缓存获取到数据：" + mcValue);
		} catch (Exception e) {
			LOGGER.error("！！错误，从缓存获取报异常：" + e);
			// TODO: handle exception
		}

		if ("makefriend_to".equals(this.params.get("EventKey"))) {
			message = MakeFriend_To.getMessage(this.params.get("ToUserName"),
					this.params.get("FromUserName"));
			return message;
		}

		else if ("desc_to".equals(this.params.get("EventKey"))) {
			if (mcValue == null) {
				message = MessageUtil.initText(this.params.get("ToUserName"),
						this.params.get("FromUserName"), "半小时内未输入，请手动输入公交车路线");
			} else {
				String[] con = mcValue.split(" ");
				String busline = "";
				if (con.length == 1) {
					// 如果一开始是 3
					busline = "" + con[0] + " 2";

				} else {
					// 如果一开始是 3 2
					busline = "" + con[0];

				}
				Map<String, String> busMap = new HashMap<String, String>();
				busMap = BusAction.getBusWx(busline);

				String BusInfo = busMap.get("info");
				message = MessageUtil.initText(this.params.get("ToUserName"),
						this.params.get("FromUserName"), BusInfo);
				mc.set(this.params.get("FromUserName"), busline, 1800);
			}

		} else if ("flush".equals(this.params.get("EventKey"))) {

			if (mcValue == null) {
				message = MessageUtil.initText(this.params.get("ToUserName"),
						this.params.get("FromUserName"), "半小时内未输入，请手动输入公交车路线");
			} else {
				Map<String, String> busMap = new HashMap<String, String>();
				busMap = BusAction.getBusWx(mcValue);

				String BusInfo = busMap.get("info");
				message = MessageUtil.initText(this.params.get("ToUserName"),
						this.params.get("FromUserName"), BusInfo);
			}
			mc.set(this.params.get("FromUserName"), mcValue, 1800);

		} else if ("serach_more".equals(this.params.get("EventKey"))) {
			message = MessageUtil.initText(this.params.get("ToUserName"),
					this.params.get("FromUserName"),
					"其他查询正在开发中，如果你有好的需求要开发，请在联系我们-问题反馈中留言，谢谢哈");
		} else if ("feedback".equals(this.params.get("EventKey"))) {

			News news = new News();
			news.setTitle("新昌公交问题（需求）反馈");
			news.setDescription("请填写您的需求，或者直接添加我们微信好友");
			news.setPicUrl("http://nbut-sucai.stor.sinaapp.com/qqq.png");
			news.setUrl("http://1.nbut.sinaapp.com/activities/nbut_vote/index.php?openid="
					+ this.params.get("FromUserName"));
			List<News> newsList = new ArrayList<News>();
			newsList.add(news);

			// message = MessageUtil.newsMessageToXml((NewsMessage) newsList);
			message = MessageUtil.initNewsMessage(
					this.params.get("ToUserName"),
					this.params.get("FromUserName"), newsList);
		}

		return message;
	}

	@Override
	public String VIEWEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
