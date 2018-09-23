package com.tian.servlet;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.ResultSet;
import com.sina.sae.memcached.SaeMemcache;
import com.tian.base.WeixinBase;
import com.tian.base.WeixinCustoms;
import com.tian.entity.Music;
import com.tian.entity.MusicMessage;
import com.tian.entity.NewsMessage;
import com.tian.util.DBUtilMysql;
import com.tian.util.MessageUtil;
import com.tian.web.MakeFriend.MakeFriend_To;
import com.tian.xcBus.Bus;

/**
 * 微信id：gh_e8a2c02918d5
 * 微信测试号专用 纯java 接口 
 * @author zty
 * 
 */
public class WeixinTest extends WeixinBase {
	protected static final Logger LOGGER = Logger.getLogger(WeixinTest.class);
	Map<String, String> map;

	public WeixinTest(Map<String, String> params) {
		super(params);
		this.map = params;

	}

	@Override
	public String text() {
		LOGGER.info("正常--####--进入weixin_test_text方法" + map.toString());
		String Content = this.map.get("Content");
		String ToUserName = this.map.get("ToUserName");
		String FromUserName = this.map.get("FromUserName");
		LOGGER.info("正常--####--微信测试号，参数分别：" + Content + ";" + ToUserName + ";"
				+ FromUserName);
		String message = null;
		//交友功能
		if (Content.startsWith("@")) {
			//从缓存获取盆友openid
			SaeMemcache mc = null;
			String mcFromUserName= null;
			mc = new SaeMemcache();
			mc.init();
			mcFromUserName= mc.get(FromUserName);
			String fromUserInfo= WeixinCustoms.getCustomInfo(FromUserName);
			JSONObject jsonData;
			
				try {
					jsonData = new JSONObject(fromUserInfo);
					String Name = jsonData.getString("nickname");
					//发送给对方
					String errcode =MakeFriend_To.pushToCustom(mcFromUserName, Name+" 发来消息："+Content+"\n快速回复可回复@+内容");
					if ("0000".equals(errcode)) {
						//发送给自己
						message = MessageUtil.initText(ToUserName, FromUserName, "消息："+Content+"\n已成功发给小伙伴，等候回音哦");
						mc.set(ToUserName, FromUserName, 1800);
						return message;
					}else {
						message = MessageUtil.initText(ToUserName, FromUserName, "消息："+Content+"\n没有发送成功，请联系小编");
						return message;
					}
				} catch (JSONException e) {
					LOGGER.error("！！错误--####--新昌无线公交，json解析错误");
					message = MessageUtil.initText(ToUserName, FromUserName, "消息："+Content+"\n没有发送成功，json解析错误，请联系小编");
					return message;
				}
				
			
			
		}
		 if ("kk".equals(Content)) {
			message=MessageUtil.initNewsMessage(ToUserName, FromUserName, NewsMessage.newsMenu());
//			message="<xml>" +
//					"<ToUserName>o8c52uH_HTlls0Lhi8WILUmNcZ14</ToUserName>" +
//					"<FromUserName>gh_e8a2c02918d5</FromUserName>" +
//					"<CreateTime>1440677587194</CreateTime>"+
//					"<MsgType><![CDATA[news]]></MsgType>" +
//					"<ArticleCount>1</ArticleCount>" +
//					"<Articles>" +
//					"<item>" +
//					"<Title><![CDATA[宁工]]></Title> " +
//					"<Description><![CDATA[将被]]></Description>" +
//					"<PicUrl><![CDATA[http://nbut-img.stor.sinaapp.com/oTDuCuKMvjnJ1hCHxa1hFtalochg.jpg]]></PicUrl>" +
//					"<Url><![CDATA[http://nbut-img.stor.sinaapp.com/oTDuCuKMvjnJ1hCHxa1hFtalochg.jpg]]></Url>" +
//					"</item>" +
//					"</Articles>" +
//					"</xml> ";
			LOGGER.info("正常--####--回复腾讯报文：" + Content + ",回复信息：" + message);

			return message;
		}
		// 查找关键字对应的消息类型　
		String conSql = " SELECT  ID ,  KEY_WORD ,  KEY_WORD_TYPE   from  wx_pub_key_conection ";
		Map<String, String> paramCon = new HashMap<String, String>();
		paramCon.put("KEY_WORD", Content);
		Map<String, String> resultMap = DBUtilMysql.getSubMap(conSql, paramCon);
		LOGGER.info("正常--####--微信测试号，从类型关系对应数据库获得消息" + resultMap.toString());

		if ("music".equals(resultMap.get("KEY_WORD_TYPE"))) {
			String sql = " SELECT  TITLE ,  THUMBMEDIAID ,  DESCRIPTION ,  MUSICURL, HQMUSICURL FROM  wx_pub_key_music ";
			Map<String, String> paramMuisc = new HashMap<String, String>();
			paramMuisc.put("KEY_WORD", Content);
			Music music = null;
			try {
				ResultSet rsResultSet = (ResultSet) DBUtilMysql
						.getSubResultSet(sql, paramMuisc);

				music = (Music) DBUtilMysql.rs2Bean(rsResultSet, Music.class);

			} catch (Exception e) {
				e.printStackTrace();
			}
			message = MessageUtil.initMusicMessage(ToUserName, FromUserName,
					music);

		} else if ("text".equals(resultMap.get("KEY_WORD_TYPE"))) {
			String textSql = " SELECT  ID ,  KEY_WORD ,  TEXT_REMSG ,  COUNT FROM  wx_pub_key_text ";
			Map<String, String> paramText = new HashMap<String, String>();
			paramText.put("KEY_WORD", Content);
			Map<String, String> resultTextMap = DBUtilMysql.getSubMap(textSql, paramCon);
			LOGGER.info("正常--####--Map 组装成功："+resultTextMap.toString());
			message = MessageUtil
					.initText(ToUserName, FromUserName, resultTextMap.get("TEXT_REMSG"));
			
			
		} else {
			String[] con = Content.split(" ");
			String busline="";
			if(con.length==1){
				 busline=""+con[0]+"11";

			}else{
				 busline=""+con[0]+"1"+con[1];

			}
			String resString ="";
			String result = Bus.getBusInfo(busline,"");
			
			// 处理返回数据
			Map<String, String> infoMap = Bus.dealInfo(result);
			if("0001".equals(infoMap.get("errcode"))){
				resString="路线："+con[0]+"\n实时公交暂不支持此路线";
			}else {
				if (infoMap!=null) {
					//路线
					String line="路线：" + infoMap.get("name") ;
					//判断是否运营
					if("no".equals(infoMap.get("isBusness"))){
						line=line+"--已下班";
					}
					 resString = line + "\n方向："
							+ infoMap.get("desc") + "\n“>”表示快到，“=”表示停靠\n\n" + infoMap.get("stas");

				}else{
					resString= "没有这个公交路线"; 
				}
			}
			message = MessageUtil.initText(ToUserName, FromUserName, resString);
			LOGGER.info("正常--####--回复腾讯报文：" + Content + ",回复信息：" + message);

			return message;
		}
		LOGGER.info("正常--####--最后回复腾讯报文：" + Content + ",回复信息：" + message);

		return message;
	}

	@Override
	public String image() {
		String message = null;
		message = MessageUtil.initText(this.map.get("ToUserName"),
				this.map.get("FromUserName"), "这是个照片文件");
		return message;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String LOCATIONEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String CLICKEvent() {
		String message = null;
		
		if ("makefriend_to".equals(this.map.get("EventKey"))) {
			
		}
		message=MakeFriend_To.getMessage(this.map.get("ToUserName"),
				this.map.get("FromUserName"));
//		message = MessageUtil.initText(this.map.get("ToUserName"),
//				this.map.get("FromUserName"), "这是个点击事件");
		return message;
	}

	@Override
	public String VIEWEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
