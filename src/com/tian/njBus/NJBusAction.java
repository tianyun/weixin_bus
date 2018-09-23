package com.tian.njBus;

import com.tian.web.MakeFriend.MakeFriend_To;

/**
 * 南京公交action类，处理方法
 * 
 * @author tianyun
 *
 */
public class NJBusAction {

	/**
	 * 传入公交路线，获取并处理信息
	 * @param busline
	 * @return
	 */
	public static String njBusAction(String busline) {
		String businfo = NJBus.getBusInfo(busline);
		MakeFriend_To.pushToCustom("o1IU4xDQxlIMeEaWAmDpqdv2-uts", businfo);
		return businfo+"\n\n发送数字+空格+2 查询反向，如3 2";
	}
	public static void main(String[] args) {
		System.out.println(njBusAction("1"));
	}
}
