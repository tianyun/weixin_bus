package com.tian.util;

public enum ErrorInfo {
	INTERFACEERROR("0001"), DATABASEERROR("0002"), REQUESTERROR("0003");

	private final String name;

	private ErrorInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
