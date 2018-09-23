package com.tian.entity;

public class AccessToken {
	private String token;
	private int expiresIn;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int string) {
		this.expiresIn = string;
	}
	@Override
	public String toString() {
		return "AccessToken [token=" + token + ", expiresIn=" + expiresIn + "]";
	}
	
	
}
