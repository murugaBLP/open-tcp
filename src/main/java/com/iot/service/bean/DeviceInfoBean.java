package com.iot.service.bean;

public class DeviceInfoBean {

	private String imeiNo;
	private String deviceName;
	private String deviceId;
	private String location;
	
	
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}


	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}


	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}


	public String toString(){
		return "{imeiNo:"+imeiNo+",deviceName:"+deviceName+",deviceId:"+deviceId+",location:"+location+"}";
	}
	
	
}
