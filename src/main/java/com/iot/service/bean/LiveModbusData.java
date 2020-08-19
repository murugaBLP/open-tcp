package com.iot.service.bean;

/**
 * @author Muruganandam
 *
 */
public class LiveModbusData {

	private String tag = "";
	private Double value = null;
	private String disName = "";
	private String unit = "";
	private String imageUrl = "";
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getDisName() {
		return disName;
	}
	public void setDisName(String disName) {
		this.disName = disName;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String toString() {
		return "{tag : "+tag+", value : "+value+",disName : "+disName+", unit :"+unit+", imageUrl : "+imageUrl+"}";
	}
}
