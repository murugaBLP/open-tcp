package com.iot.service.bean;

import java.util.Map;

public class DeviceInfo {

	private Map<String, DeviceInfoBean> deviceInfo;

	public Map<String, DeviceInfoBean> getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(Map<String, DeviceInfoBean> deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
	public String toString() {
		return "deviceInfo : "+deviceInfo.toString();
	}
}
