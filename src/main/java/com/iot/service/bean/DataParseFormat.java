package com.iot.service.bean;

/**
 * @author Muruganandam
 *
 */

public class DataParseFormat {

	private String tag = "";
	private String disName = "";
	private String parseLength = "";
	private String convertionType = "";
	private String unit = "";
	private String imageUrl = "";
	private String multipilicationFactor = "";
	
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
	public String getMultipilicationFactor() {
		return multipilicationFactor;
	}
	public void setMultipilicationFactor(String multipilicationFactor) {
		this.multipilicationFactor = multipilicationFactor;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDisName() {
		return disName;
	}
	public void setDisName(String disName) {
		this.disName = disName;
	}
	public String getParseLength() {
		return parseLength;
	}
	public void setParseLength(String parseLength) {
		this.parseLength = parseLength;
	}
	public String getConvertionType() {
		return convertionType;
	}
	public void setConvertionType(String convertionType) {
		this.convertionType = convertionType;
	}
	
	public String toString() {
		return "{ tag : "+tag+", disName : "+disName +", parseLength : "+parseLength
				+", convertionType : "+convertionType+ ", unit : "+unit+", imageUrl :"
				+imageUrl+", multipilicationFactor : "+multipilicationFactor+"}";
	}
}
