package com.iot.service.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iot.service.bean.ModbusData;
import com.iot.service.bean.ParserConfig;
import com.iot.service.http.HttpService;
import com.iot.service.main.IoTService;


/**
 * @author Muruganandam
 *
 */

public class ParserConfigService implements Constants,Refresh{

	private static final Logger LOGGER = Logger.getLogger(ParserConfigService.class.getName());
	private ModbusData modbusData = null;
	ParserConfigService configService = null;
	
	Map<String, ParserConfig> parserConfig = null;
	Map<String, Map<String, String>> deviceInfo = null;
	
	public ParserConfigService() {
		//this.modbusData = loadJson();
		this.deviceInfo = getDeviceInfos();
		this.parserConfig = getParserInfos();
	}
	
	@Override
	public void load() {
		configService = new ParserConfigService();
		//configService.modbusData = configService.loadJson();
		configService.deviceInfo = getDeviceInfos();
		configService.parserConfig = getParserInfos();
	}
	
	@Override
	public void switchObj() {
		//this.modbusData = configService.modbusData;
		this.deviceInfo = configService.deviceInfo;
		this.parserConfig = configService.parserConfig;
		this.configService = null;
	}
	
	public ModbusData getParserData() {
		return this.modbusData;
	}
	
	public Map<String, ParserConfig> getParserConfig(){
		return this.parserConfig;
	}
	
	public Map<String, Map<String, String>> getDeviceInfo(){
		return this.deviceInfo;
	}
	
	/**
	 * @param fileName
	 * @return
	 */
	public ModbusData loadJson() {
		try {
			File jarPath=new File(LoggerService.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
	        LOGGER.info("Loading json file :"+propertiesPath+PARSER_CONF_FILE);
	        BufferedReader br = new BufferedReader(new FileReader(propertiesPath+PARSER_CONF_FILE));
	        return new Gson().fromJson(br, ModbusData.class);
		} catch (Exception e) {
			LOGGER.severe("Exception while load json file parse."+e);
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, Map<String, String>> getDeviceInfos() {
		try {
			String deviceInfoUrl = IoTService.properties.getProperty(DEVICE_INFO);
			String data = new HttpService().doGet(deviceInfoUrl);
			if(data == null) {
				LOGGER.warning("Unable to fetch device info."+deviceInfoUrl);
				return null;
			}
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(data);
			Map<String, Map<String, String>> deviceInfo = new HashMap<String, Map<String, String>>();
			JsonArray arr = json.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
			for(JsonElement array : arr) {
				//DeviceInfoBean info = new Gson().fromJson(array.getAsJsonObject().get("_source"), DeviceInfoBean.class);
				//deviceInfo.put(info.getImeiNo(), info);
				//deviceInfo.put(array.getAsJsonObject().get("_source").getAsJsonObject().get("imeiNo").getAsString(), array.getAsJsonObject().get("_source").toString());
				JsonObject object = array.getAsJsonObject().get("_source").getAsJsonObject();
				Set<Map.Entry<String, JsonElement>> set = object.entrySet();
				Map<String, String> infoMap = new HashMap<>();
				for(Map.Entry<String, JsonElement> entry : set) {
					infoMap.put(entry.getKey().toString(), entry.getValue().toString().replaceAll("\"", ""));
				}
				deviceInfo.put(array.getAsJsonObject().get("_source").getAsJsonObject().get("imeiNo").getAsString(), infoMap);
			}
			return deviceInfo;
		} catch (Exception e) {
			LOGGER.severe("Exception in get request.."+e);
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, ParserConfig> getParserInfos() {
		try {
			String parserInfoUrl = IoTService.properties.getProperty(PARSER_INFO);
			String data = new HttpService().doGet(parserInfoUrl);
			if(data == null) {
				LOGGER.warning("Unable to fetch parser info."+parserInfoUrl);
				return null;
			}
			
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(data);
			Map<String, ParserConfig> deviceInfo = new HashMap<String, ParserConfig>();
			
			JsonArray arr = json.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
			for(JsonElement array : arr) {
				ModbusData infos = new Gson().fromJson(array.getAsJsonObject().get("_source"), ModbusData.class);
				ParserConfig info = infos.getModbusData().get(0);
				deviceInfo.put(info.getFirmwareVersion()+"_"+info.getDataType(), info);
			}
			return deviceInfo;
		} catch (Exception e) {
			LOGGER.severe("Exception in get request.."+e);
			e.printStackTrace();
			return null;
		}
	}
	/*
	public void processData() {
		RefreshDao dao = new RefreshDao(this);
		new Thread(dao).start();
		
		try {
			Thread.sleep(4000);
			while(true) {
				System.out.println("hash Code :"+this.hashCode());
				System.out.println(modbusData.getModbusData().get(0));
				System.out.println(modbusData.getModbusData().get(1));
				Thread.sleep(4000);
			}
		} catch (Exception e) {
			System.out.println("Exception in ...."+e);
		}
	}
	
	public static void main(String[] args) {
		new ParserConfigService().processData();
	}*/
}
