package com.tian.cqbus;

/**
 * 车站类
 * 
 * @author zxh
 *
 */
public class Station {
	String StationName;
	String FBackSign;
	String Minlatitude;
	String LNodeId;
	String Minlongitude;
	public String getStationName() {
		return StationName;
	}
	public void setStationName(String stationName) {
		StationName = stationName;
	}
	public String getFBackSign() {
		return FBackSign;
	}
	public void setFBackSign(String fBackSign) {
		FBackSign = fBackSign;
	}
	public String getMinlatitude() {
		return Minlatitude;
	}
	public void setMinlatitude(String minlatitude) {
		Minlatitude = minlatitude;
	}
	public String getLNodeId() {
		return LNodeId;
	}
	public void setLNodeId(String lNodeId) {
		LNodeId = lNodeId;
	}
	public String getMinlongitude() {
		return Minlongitude;
	}
	public void setMinlongitude(String minlongitude) {
		Minlongitude = minlongitude;
	}
	@Override
	public String toString() {
		return "Station [StationName=" + StationName + ", FBackSign="
				+ FBackSign + ", Minlatitude=" + Minlatitude + ", LNodeId="
				+ LNodeId + ", Minlongitude=" + Minlongitude + "]";
	}
	
	
}
