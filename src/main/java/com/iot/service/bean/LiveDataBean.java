package com.iot.service.bean;

import java.util.List;
import java.util.Map;

/**
 * @author Muruganandam
 *
 */

public class LiveDataBean {

	private String imeiNo;
	private String dataType;
	private String firmwareVersion;
	private String deviceModel;
	private String sdkFirmware;
	private String data;
	private String packetId;
	private String signalStrength;
	private String statusBit;
	private String hardwarePeripherals;
	private String operatorName;
	private String simIdNo;
	private String latitute;
	private String longitude;
	private String degree;
	private String satelliteView;
	private String altitude;
	private String speed;
	private String pdop;
	private String hdop;
	private String vdop;
	private String internalBatteryVoltage;
	private String internalBatteryTemperature;
	private String memoryCapacityPrecentage;
	private String modbusFTPFileName;
	private String modbusRequestNo;
	private String modbusData;
	private String totalNoOfRequest;
	private String rs485LookupName;
	private String adc2_adc1_adc0;
	private String iKey;
	private String accelarationGyro;
	private String serialRequestNo;
	private String rs232TransparentSerialData;
	private String rs232RequestNo;
	private String rs232Lookup;
	private String terminationString;
	private String type;
	private String rawData;
	private String recTimestamp;
	private String timestamp;
	private Map<String, Double> tagMap;
	private Map<String, String> descMap;
	private Map<String, String> statusMap;
	
	private List<LiveModbusData> liveData;
	private List<LiveStatusData> statusData;
	private Map<String, String> deviceInfo = null;
	
	public Map<String, String> getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(Map<String, String> deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public List<LiveModbusData> getLiveDats() {
		return liveData;
	}
	public void setLiveDats(List<LiveModbusData> liveDats) {
		this.liveData = liveDats;
	}
	public List<LiveStatusData> getStatusData() {
		return statusData;
	}
	public void setStatusData(List<LiveStatusData> statusData) {
		this.statusData = statusData;
	}
	public Map<String, String> getStatusMap() {
		return statusMap;
	}
	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}
	public Map<String, Double> getTagMap() {
		return tagMap;
	}
	public void setTagMap(Map<String, Double> tagMap) {
		this.tagMap = tagMap;
	}
	public Map<String, String> getDescMap() {
		return descMap;
	}
	public void setDescMap(Map<String, String> descMap) {
		this.descMap = descMap;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getSdkFirmware() {
		return sdkFirmware;
	}
	public void setSdkFirmware(String sdkFirmware) {
		this.sdkFirmware = sdkFirmware;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getPacketId() {
		return packetId;
	}
	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}
	public String getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(String signalStrength) {
		this.signalStrength = signalStrength;
	}
	public String getStatusBit() {
		return statusBit;
	}
	public void setStatusBit(String statusBit) {
		this.statusBit = statusBit;
	}
	public String getHardwarePeripherals() {
		return hardwarePeripherals;
	}
	public void setHardwarePeripherals(String hardwarePeripherals) {
		this.hardwarePeripherals = hardwarePeripherals;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getSimIdNo() {
		return simIdNo;
	}
	public void setSimIdNo(String simIdNo) {
		this.simIdNo = simIdNo;
	}
	public String getLatitute() {
		return latitute;
	}
	public void setLatitute(String latitute) {
		this.latitute = latitute;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getSatelliteView() {
		return satelliteView;
	}
	public void setSatelliteView(String satelliteView) {
		this.satelliteView = satelliteView;
	}
	public String getAltitude() {
		return altitude;
	}
	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getPdop() {
		return pdop;
	}
	public void setPdop(String pdop) {
		this.pdop = pdop;
	}
	public String getHdop() {
		return hdop;
	}
	public void setHdop(String hdop) {
		this.hdop = hdop;
	}
	public String getVdop() {
		return vdop;
	}
	public void setVdop(String vdop) {
		this.vdop = vdop;
	}
	public String getInternalBatteryVoltage() {
		return internalBatteryVoltage;
	}
	public void setInternalBatteryVoltage(String internalBatteryVoltage) {
		this.internalBatteryVoltage = internalBatteryVoltage;
	}
	public String getInternalBatteryTemperature() {
		return internalBatteryTemperature;
	}
	public void setInternalBatteryTemperature(String internalBatteryTemperature) {
		this.internalBatteryTemperature = internalBatteryTemperature;
	}
	public String getMemoryCapacityPrecentage() {
		return memoryCapacityPrecentage;
	}
	public void setMemoryCapacityPrecentage(String memoryCapacityPrecentage) {
		this.memoryCapacityPrecentage = memoryCapacityPrecentage;
	}
	public String getModbusFTPFileName() {
		return modbusFTPFileName;
	}
	public void setModbusFTPFileName(String modbusFTPFileName) {
		this.modbusFTPFileName = modbusFTPFileName;
	}
	public String getModbusRequestNo() {
		return modbusRequestNo;
	}
	public void setModbusRequestNo(String modbusRequestNo) {
		this.modbusRequestNo = modbusRequestNo;
	}
	public String getModbusData() {
		return modbusData;
	}
	public void setModbusData(String modbusData) {
		this.modbusData = modbusData;
	}
	public String getTotalNoOfRequest() {
		return totalNoOfRequest;
	}
	public void setTotalNoOfRequest(String totalNoOfRequest) {
		this.totalNoOfRequest = totalNoOfRequest;
	}
	public String getRs485LookupName() {
		return rs485LookupName;
	}
	public void setRs485LookupName(String rs485LookupName) {
		this.rs485LookupName = rs485LookupName;
	}
	public String getAdc2_adc1_adc0() {
		return adc2_adc1_adc0;
	}
	public void setAdc2_adc1_adc0(String adc2_adc1_adc0) {
		this.adc2_adc1_adc0 = adc2_adc1_adc0;
	}
	public String getiKey() {
		return iKey;
	}
	public void setiKey(String iKey) {
		this.iKey = iKey;
	}
	public String getAccelarationGyro() {
		return accelarationGyro;
	}
	public void setAccelarationGyro(String accelarationGyro) {
		this.accelarationGyro = accelarationGyro;
	}
	public String getSerialRequestNo() {
		return serialRequestNo;
	}
	public void setSerialRequestNo(String serialRequestNo) {
		this.serialRequestNo = serialRequestNo;
	}
	public String getRs232TransparentSerialData() {
		return rs232TransparentSerialData;
	}
	public void setRs232TransparentSerialData(String rs232TransparentSerialData) {
		this.rs232TransparentSerialData = rs232TransparentSerialData;
	}
	public String getRs232RequestNo() {
		return rs232RequestNo;
	}
	public void setRs232RequestNo(String rs232RequestNo) {
		this.rs232RequestNo = rs232RequestNo;
	}
	public String getRs232Lookup() {
		return rs232Lookup;
	}
	public void setRs232Lookup(String rs232Lookup) {
		this.rs232Lookup = rs232Lookup;
	}
	public String getTerminationString() {
		return terminationString;
	}
	public void setTerminationString(String terminationString) {
		this.terminationString = terminationString;
	}
	public String getRecTimestamp() {
		return recTimestamp;
	}
	public void setRecTimestamp(String recTimestamp) {
		this.recTimestamp = recTimestamp;
	}
	
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRawData() {
		return rawData;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	
	public String toString() {
		return "{imeiNo :"+imeiNo +", firmwareVersion : "+firmwareVersion+", deviceModel :"+deviceModel
				+", sdkFirmware :"+sdkFirmware+", data : "+data+", packetId :"+packetId+", signalStrength :"
				+signalStrength+", statusBit :"+statusBit+", hardwarePeripherals :"+hardwarePeripherals
				+", operatorName :"+operatorName+", simIdNo :"+simIdNo+", latitute :"+latitute+", longitude:"+longitude
				+", degree :"+degree+", satelliteView :"+satelliteView+", altitude :"+altitude+", speed :"+speed
				+", pdop :"+pdop+", hdop :"+hdop+", vdop :"+vdop+", internalBatteryVoltage :"+internalBatteryVoltage
				+", internalBatteryTemperature :"+internalBatteryTemperature+", memoryCapacityPrecentage :"+memoryCapacityPrecentage
				+", modbusFTPFileName :"+modbusFTPFileName+", modbusRequestNo :"+modbusRequestNo+", modbusData :"+modbusData
				+", totalNoOfRequest :"+totalNoOfRequest+", rs485LookupName :"+rs485LookupName+", adc2_adc1_adc0 :"+adc2_adc1_adc0
				+", iKey :"+iKey+", accelarationGyro :"+accelarationGyro+", serialRequestNo :"+serialRequestNo
				+", rs232TransparentSerialData :"+rs232TransparentSerialData+", rs232RequestNo :"+rs232RequestNo
				+", rs232Lookup : "+rs232Lookup+", terminationString :"+terminationString+", type"+type
				+", rawData : "+rawData+", recTimestamp :"+recTimestamp
				+", tagMap : "+tagMap+", descMap :"+descMap
				+", statusMap : "+statusMap+ ", liveData : "+liveData.toString()+ ", statusData : "+statusData.toString()
				+", deviceInfo : "+deviceInfo+"}";
	}
}
