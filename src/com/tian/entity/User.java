package com.tian.entity;

/**
 * 用户user 
 * @author tianyun
 *
 */
public class User {

	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 微信昵称
	 */
	private String nickName;
	
	/**
	 * 用户id
	 */
	private String userID;

	/**
	 * 用户在微信里的 openId
	 */
	private String openID;

	/**
	 * 用户所在城市
	 */
	private String city;
	
	/**
	 * 用户性别
	 */
	private String sex;
	
	/**
	 * 用户年龄
	 */
	private int age;
	
	/**
	 * 用户电话
	 */
	private int phoneNum;
	
	/**
	 * 用户地址
	 */
	private String address;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getOpenID() {
		return openID;
	}

	public void setOpenID(String openID) {
		this.openID = openID;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(int phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", userID=" + userID + ", openID=" + openID + ", city=" + city + ", sex="
				+ sex + ", age=" + age + ", phoneNum=" + phoneNum + ", address=" + address + "]";
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
	
}
