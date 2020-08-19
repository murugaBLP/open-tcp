package com.iot.service.bean;

import java.util.List;

/**
 * @author Muruganandam
 *
 */

public class ModbusData {
	List<ParserConfig> modbusData = null;

	public List<ParserConfig> getModbusData() {
		return modbusData;
	}

	public void setModbusData(List<ParserConfig> modbusData) {
		this.modbusData = modbusData;
	}
	
	public String toString() {
		return "modbusData: {"+modbusData.toString()+"}";
	}
}
