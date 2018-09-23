package com.tian.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.tian.entity.Image;
import com.tian.entity.ImageMessage;
import com.tian.entity.Music;
import com.tian.entity.MusicMessage;
import com.tian.entity.News;
import com.tian.entity.NewsMessage;
import com.tian.entity.TextMessage;
import com.tian.entity.Voice;
import com.tian.entity.VoiceMessage;

/**
 * 消息的格式转换，xml->map
 * 
 * @author tiantian
 * 
 */
public class MessageUtil {

	protected static final Logger logger = Logger.getLogger(MessageUtil.class);

	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBCRIBE = "unsubcribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "view";
	public static final String MESSAGE_SCANCODE = "scancode_push";

	/**
	 * 将xml转换成java textMessage对象
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request)
			throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		// dom4j解析传入xml
		SAXReader reader = new SAXReader();
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		Element root = doc.getRootElement();
		List<Element> list = root.elements();// 获取所有节点
		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;

	}

	/**
	 * 将java textMessage对象转换成xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", textMessage.getClass());// 根节点改成xml

		return xStream.toXML(textMessage);// 把java对象转换成xml
	}

	/**
	 * 文字回复的xml封装
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String ToUserName,  String FromUserName ,
			String content) {
		String message = null;
		
		TextMessage text = new TextMessage();
		text.setFromUserName(ToUserName);
		text.setToUserName(FromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);

		message = textMessageToXml(text);
		return message;

	}

	/**
	 * 将java newMessage对象转换成xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", newsMessage.getClass());// 根节点改成xml
		xStream.alias("item", new News().getClass());
		return xStream.toXML(newsMessage);// 把java对象转换成xml
	}

	/**
	 * 图文消息的封装
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String ToUserName,  String FromUserName , List<News> newMenu) {
		String message = null;
		
		List<News> newsList = newMenu;
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setToUserName(FromUserName);
		newsMessage.setFromUserName(ToUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());

		message = newsMessageToXml(newsMessage);
		return message;
	}

	/**
	 * 将java imageMessage对象转换成xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", imageMessage.getClass());// 根节点改成xml

		return xStream.toXML(imageMessage);// 把java对象转换成xml
	}

	/**
	 * 图片消息封装
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initImageMessage(String ToUserName,  String FromUserName,Image image) {
		String message = null;
		
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(ToUserName);
		imageMessage.setToUserName(FromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);

		message = imageMessageToXml(imageMessage);
		return message;

	}

	/**
	 * 将java voiceMessage对象转换成xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String voiceMessageToXml(VoiceMessage voiceMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", voiceMessage.getClass());// 根节点改成xml
		return xStream.toXML(voiceMessage);// 把java对象转换成xml
	}

	/**
	 * 组装声音消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initVoiceMessage(String ToUserName,  String FromUserName, Voice voice) {
		String message = null;
		
		VoiceMessage voiceMessage = new VoiceMessage();
		voiceMessage.setFromUserName(ToUserName);
		voiceMessage.setToUserName(FromUserName);
		voiceMessage.setMsgType(MESSAGE_VOICE);
		voiceMessage.setCreateTime(new Date().getTime());
		voiceMessage.setVoice(voice);

		message = voiceMessageToXml(voiceMessage);
		return message;

	}

	/**
	 * 将java voiceMessage对象转换成xml
	 * 
	 * @param textMessage
	 * @return
	 */
	public static String MusicMessageToXml(MusicMessage musicMessage) {
		XStream xStream = new XStream();
		xStream.alias("xml", musicMessage.getClass());// 根节点改成xml
		return xStream.toXML(musicMessage);// 把java对象转换成xml
	}

	/**
	 * 组装音乐消息
	 * 
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initMusicMessage(String ToUserName,  String FromUserName, Music music) {
		String message = null;
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(ToUserName);
		musicMessage.setToUserName(FromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);

		message = MusicMessageToXml(musicMessage);
		return message;

	}

	

	
}
