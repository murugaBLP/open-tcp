package com.iot.service.bean;

import java.util.List;

/**
 * @author Muruganandam
 *
 */

public class ParserConfig {

	private String firmwareVersion = "";
	private String dataType = "";
	private String removeFirst = "";
	private String removeLast = "";
	private List<DataParseFormat> dataParseFormat = null;
	private String statusParseLength = "";
	private String statusConversionType = "";
	private List<StatusParseFormat> statusParseFormat = null;
	
	
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getRemoveFirst() {
		return removeFirst;
	}
	public void setRemoveFirst(String removeFirst) {
		this.removeFirst = removeFirst;
	}
	public String getRemoveLast() {
		return removeLast;
	}
	public void setRemoveLast(String removeLast) {
		this.removeLast = removeLast;
	}
	public List<DataParseFormat> getDataParseFormat() {
		return dataParseFormat;
	}
	public void setDataParseFormat(List<DataParseFormat> dataParseFormat) {
		this.dataParseFormat = dataParseFormat;
	}
	public String getStatusParseLength() {
		return statusParseLength;
	}
	public void setStatusParseLength(String statusParseLength) {
		this.statusParseLength = statusParseLength;
	}
	public String getStatusConversionType() {
		return statusConversionType;
	}
	public void setStatusConversionType(String statusConversionType) {
		this.statusConversionType = statusConversionType;
	}
	public List<StatusParseFormat> getStatusParseFormat() {
		return statusParseFormat;
	}
	public void setStatusParseFormat(List<StatusParseFormat> statusParseFormat) {
		this.statusParseFormat = statusParseFormat;
	}
	
	public String toString() {
		return "{firmwareVersion :"+firmwareVersion+", dataType : "+dataType+", removeFirst : "+removeFirst+", removeLast :"+removeLast
				+", dataParseFormat : "+dataParseFormat.toString()+", statusParseLength : "+statusParseLength+", statusConversionType :"+
				statusConversionType+", statusParseFormat : "+statusParseFormat.toString()+"}";
	}
}
