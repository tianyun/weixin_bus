package com.tian.entity;

public class PushType {
	
	/**
	 * 传入类型和内容
	 * @param type
	 * @param content
	 * @return
	 */
	public static String getType(String type, String content,String openid) {
		String resultStr="";
		if("text".equals(type)){
			 resultStr = "{" +
					"\"touser\":\""+openid+"\"," +
					"\"msgtype\":\"text\"," +
					"\"text\":" +
						"{" +
							"\"content\":\""+content+"\"" +
						"}" +
						"}";
		}else{
			
		}
		return resultStr;
	}
}
