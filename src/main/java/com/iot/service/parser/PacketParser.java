package com.iot.service.parser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import org.joda.time.DateTime;

import com.iot.service.bean.DataParseFormat;
import com.iot.service.bean.LiveDataBean;
import com.iot.service.bean.LiveModbusData;
import com.iot.service.bean.LiveStatusData;
import com.iot.service.bean.MailBean;
import com.iot.service.bean.ParserConfig;
import com.iot.service.bean.StatusParseFormat;
import com.iot.service.http.HttpService;
import com.iot.service.mail.MailService;
import com.iot.service.main.IoTService;
import com.iot.service.utils.Constants;
import com.iot.service.utils.ParserConfigService;
import com.iot.service.utils.RefreshDao;
import com.iot.service.utils.Utils;

/**
 * @author Muruganandam
 *
 */

public class PacketParser implements Runnable,Constants{

	private static final Logger LOGGER = Logger.getLogger(PacketParser.class.getName());
	BlockingQueue<String> queue = null;
	HttpService httpService = null;
	ParserConfigService configService = null;
	
	/**
	 * @param queue
	 */
	public PacketParser(BlockingQueue<String> queue) {
		try {
			this.queue = queue;
			httpService = new HttpService();
			configService = new ParserConfigService();
			new Thread(new RefreshDao(configService)).start();
			new Thread(new MailService(configService)).start();
			LOGGER.info("Initialized consumer thread!");
		} catch (Exception e) {
			LOGGER.severe("Exception in PacketParser constructor.."+e);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOGGER.info("Consumer thread started!");
		while(true) {
			try {
				String data = queue.take();
				processMessage(data);
				//LOGGER.info("processed message...."+queue.size());
			} catch (Exception e) {
				LOGGER.severe("Exception while taking message from the queue."+e);
			}
		}
	}
	
	/**
	 * @param data
	 */
	private void processMessage(String data) {
		
		if(data == null || data.isEmpty()) {
			LOGGER.warning("Received null message.\n");
			return;
		}
		
		try {
			String[] array = data.split(SPLIT_STR); 
			Arrays.stream(array).forEachOrdered(item->{
				try {
					if(!item.startsWith(STR_STARTS_WITH) && item.endsWith(STR_ENDS_WITH)) {
						LOGGER.info("Items :"+item);
						String[] msg = item.split(SPLIT_COM);
						LiveDataBean bean = new LiveDataBean();
						bean.setType(msg[0]);
						bean.setImeiNo(msg[1]);
						bean.setFirmwareVersion(msg[2]);
						bean.setDeviceModel(msg[3]);
						bean.setSdkFirmware(msg[4]);
						int dateDecimal = Utils.hexToDecimal(msg[5]);
						bean.setTimestamp(Utils.epochDate(dateDecimal, DATE_FORMAT));
						bean.setPacketId(msg[6]);
						bean.setSignalStrength(msg[7]);
						bean.setStatusBit(Utils.hexToBin(msg[8]));
						bean.setHardwarePeripherals(msg[9]);
						bean.setOperatorName(msg[10]);
						bean.setSimIdNo(msg[11]);
						bean.setLatitute(msg[12]);
						bean.setLongitude(msg[13]);
						bean.setDegree(msg[14]);
						bean.setSatelliteView(msg[15]);
						bean.setAltitude(msg[16]);
						bean.setSpeed(msg[17]);
						bean.setPdop(msg[18]);
						bean.setHdop(msg[19]);
						bean.setVdop(msg[20]);
						bean.setInternalBatteryVoltage(msg[21]);
						bean.setInternalBatteryTemperature(msg[22]);
						bean.setMemoryCapacityPrecentage(msg[23]);
						bean.setModbusFTPFileName(msg[24]);
						bean.setModbusRequestNo(msg[25]);
						//parse modbus data
						String mdData = msg[26];
						String msgType = UN_ZERO;
						if(mdData != null && !mdData.isEmpty()) {
							if(msg[25].startsWith(STARTS_ZERO)) {
								mdData = mdData.substring(6, mdData.length());
								LOGGER.info("modbus data length :"+(mdData.length()));
								bean.setTagMap(parseModbusdata(mdData));
								bean.setDescMap(getDescriptionMap());
								bean.setStatusMap(getStatusBit(mdData.substring(116,124), bean.getImeiNo()));
							}else {
								msgType = UN_ONE;
								mdData = mdData.substring(6, mdData.length());
								LOGGER.info("modbus data length :"+(mdData.length()));;
								bean.setTagMap(parseModbusdataTwo(mdData));
								bean.setDescMap(getDescriptionMapTwo());
							}
						}
						processLivedat(bean, mdData, msg[25]);
						
						bean.setTotalNoOfRequest(msg[27]);
						bean.setRs485LookupName(msg[28]);
						bean.setAdc2_adc1_adc0(msg[29]);
						bean.setiKey(msg[30]);
						bean.setAccelarationGyro(msg[31]);
						bean.setSerialRequestNo(msg[32]);
						bean.setRs232TransparentSerialData(msg[33]);
						bean.setRs232RequestNo(msg[34]);
						bean.setRs232Lookup(msg[35]);
						bean.setRawData(item);
						bean.setType(DATA_TYPR);
						bean.setRecTimestamp(DateTime.now().toString());
						
						if(!msgType.equals(UN_ONE))
							postData(bean, msg[1]+msgType);
						
						//System.out.println(bean.toString());
					}
				} catch (Exception e) {
					LOGGER.severe("Exception while split the data."+e);
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			LOGGER.severe("Exception while parse message."+e);
		}
	}
	
	/**
	 * @param bean
	 * @param msgType
	 */
	private void postData(LiveDataBean bean, String msgType) {
		try {
			String liveUrl = IoTService.properties.getProperty(LIVE_URL);
			String historyUrl = IoTService.properties.getProperty(HISTORY_URL);
			httpService.doPost(bean, liveUrl+msgType);
			httpService.doPost(bean, historyUrl);
		} catch (Exception e) {
			LOGGER.severe("Exception in while post data."+e);
		}
	}
	
	/**
	 * @param bean
	 * @param data
	 * @param dataType
	 */
	private void processLivedat(LiveDataBean bean,String data, String dataType) {
		/*List<ParserConfig> list = configService.getParserData().getModbusData();
		list.stream().forEachOrdered(parConf -> {
			if(parConf.getDataType().equalsIgnoreCase(dataType)) {
				parseLiveData(bean, parConf, data, dataType);
				parseStatusData(bean, parConf, data, dataType);
				setDeviceInfo(bean);
			}
		});*/
		try {
			ParserConfig parConf = configService.getParserConfig().get(bean.getFirmwareVersion()+"_"+dataType); //FV_TYPE
			//ParserConfig parConf = configService.getParserConfig().get("0");
			
			if(parConf == null) {
				LOGGER.severe("Unable to find parser config details..Type : "+bean.getFirmwareVersion()+dataType);
				return;
			}
			
			parseLiveData(bean, parConf, data, dataType);
			parseStatusData(bean, parConf, data, dataType);
			setDeviceInfo(bean);
		} catch (Exception e) {
			LOGGER.severe("Exception in processLivedat.."+e);
		}
	}
	
	private void parseLiveData(LiveDataBean bean,ParserConfig parConf, String data, String dataType) {
		try {
			LOGGER.info("Parsing live data.");
			List<DataParseFormat> dataParseFormats = parConf.getDataParseFormat();
			List<LiveModbusData> liveDataList = new ArrayList<>();
			
			if(dataParseFormats == null || dataParseFormats.isEmpty() || dataParseFormats.size()==0)
				return;
						
			dataParseFormats.stream().forEachOrdered(ld->{
				try {
					LiveModbusData liveData = new LiveModbusData();
					liveData.setTag(ld.getTag());
					liveData.setDisName(ld.getDisName());
					liveData.setUnit(ld.getUnit());
					liveData.setImageUrl(ld.getImageUrl());
					
					if(ld.getConvertionType().equalsIgnoreCase("hexToDecimal")) {
						String subStr[] = ld.getParseLength().split(",");
						liveData.setValue(Double.valueOf(Utils.hexToDecimal(data.substring(Integer.valueOf(subStr[0]),Integer.valueOf(subStr[1])))));	
					}else if(ld.getConvertionType().equalsIgnoreCase("decToBinary")) {
						//liveData.setValue(Utils.decToBinary(Utils.hexToDecimal(data.substring(0,4))));	
					}else {
						LOGGER.severe("Data convertion type is not matching. please configure properly. Tag : "+ld.getTag());
					}
					liveDataList.add(liveData);
				} catch (Exception e) {
					LOGGER.severe("Exception while parse live data.."+e);
				}
			});
			bean.setLiveDats(liveDataList);
		} catch (Exception e) {
			LOGGER.severe("Exception in parseLiveData :" + e);
			e.printStackTrace();
		}
	}
	
	private void parseStatusData(LiveDataBean bean,ParserConfig parConf, String data, String dataType) {
		try {
			LOGGER.info("Parsing status data.");
			List<StatusParseFormat> statusParseFormats = parConf.getStatusParseFormat();
			if(statusParseFormats == null || statusParseFormats.isEmpty() || statusParseFormats.size()==0)
				return;
			
			List<LiveStatusData> statusList = new ArrayList<>();
			List<String> statusVal = new ArrayList<>();
			
			if(parConf.getStatusConversionType().equalsIgnoreCase("hexToDecimal")) {
				String subStr[] = parConf.getStatusParseLength().split(",");
				String statusbit = String.valueOf(Utils.hexToDecimal(data.substring(Integer.valueOf(subStr[0]),Integer.valueOf(subStr[1]))));	
				statusVal.addAll(Arrays.asList(Utils.decToBinary(Utils.hexToDecimal(statusbit)).split("")));
				
			}else if(parConf.getStatusConversionType().equalsIgnoreCase("decToBinary")) {
				String subStr[] = parConf.getStatusParseLength().split(",");
				statusVal.addAll(Arrays.asList(Utils.decToBinary(Utils.hexToDecimal(data.substring(Integer.valueOf(subStr[0]),Integer.valueOf(subStr[1])))).split("")));	
			}else {
				LOGGER.severe("Data convertion type is not matching. please configure properly. Status type : "+parConf.getStatusConversionType());
			}
			
			statusParseFormats.stream().forEachOrdered(sd->{
				try {
					LiveStatusData liveStatusData = new LiveStatusData();
					liveStatusData.setTag(sd.getTag());
					liveStatusData.setDisName(sd.getDisName());
					liveStatusData.setValue(statusVal.get(Integer.valueOf(sd.getParseLength())));	
					liveStatusData.setUnit(sd.getUnit());
					liveStatusData.setImageUrl(sd.getImageUrl());
					statusList.add(liveStatusData);
				} catch (Exception e2) {
					LOGGER.severe("Exception while parse status data."+e2);
				}
			});
			bean.setStatusData(statusList);
		} catch (Exception e) {
			LOGGER.severe("Exception in parseStatusData :" + e);
			e.printStackTrace();
		}
	}
	
	private void setDeviceInfo(LiveDataBean bean) {
		try {
			LOGGER.info("info : "+configService.getDeviceInfo().get(bean.getImeiNo()));
			bean.setDeviceInfo(configService.getDeviceInfo().get(bean.getImeiNo()));
		} catch (Exception e) {
			LOGGER.severe("Exception in setDeviceInfo.."+e);
			e.printStackTrace();
		}
	}
	
	private HashMap<String, Double> parseModbusdata(String data) {
		HashMap<String, Double> map = new LinkedHashMap<>();
		
		try {
			map.put("tag1", Double.valueOf(Utils.hexToDecimal(data.substring(0,4))));
			map.put("tag2", Double.valueOf(Utils.hexToDecimal(data.substring(4,8))));
			map.put("tag3", Double.valueOf(Utils.hexToDecimal(data.substring(8,12))));
			map.put("tag4", Double.valueOf(Utils.hexToDecimal(data.substring(12,16))));
			map.put("tag5", Double.valueOf(Utils.hexToDecimal(data.substring(16,20))));
			map.put("tag6", Double.valueOf(Utils.hexToDecimal(data.substring(20,24))));
			map.put("tag7", Double.valueOf(Utils.hexToDecimal(data.substring(24,28))));
			map.put("tag8", Double.valueOf(Utils.hexToDecimal(data.substring(28,32))));
			map.put("tag9", Double.valueOf(Utils.hexToDecimal(data.substring(32,36))));
			map.put("tag10", Double.valueOf(Utils.hexToDecimal(data.substring(36,40))));
			map.put("tag11", Double.valueOf(Utils.hexToDecimal(data.substring(40,44))));
			map.put("tag12", Double.valueOf(Utils.hexToDecimal(data.substring(44,48))));
			map.put("tag13", Double.valueOf(Utils.hexToDecimal(data.substring(48,52))));
			map.put("tag14", Double.valueOf(Utils.hexToDecimal(data.substring(52,56))));
			map.put("tag15", Double.valueOf(Utils.hexToDecimal(data.substring(56,60))));
			map.put("tag16", Double.valueOf(Utils.hexToDecimal(data.substring(60,64))));
			map.put("tag17", Double.valueOf(Utils.hexToDecimal(data.substring(64,68))));
			map.put("tag18", Double.valueOf(Utils.hexToDecimal(data.substring(68,72))));
			map.put("tag19", Double.valueOf(Utils.hexToDecimal(data.substring(72,76))));
			map.put("tag20", Double.valueOf(Utils.hexToDecimal(data.substring(76,80))));
			map.put("tag21", Double.valueOf(Utils.hexToDecimal(data.substring(80,84))));
			map.put("tag22", Double.valueOf(Utils.hexToDecimal(data.substring(84,88))));
			map.put("tag23", Double.valueOf(Utils.hexToDecimal(data.substring(88,92))));
			map.put("tag24", Double.valueOf(Utils.hexToDecimal(data.substring(92,96))));
			map.put("tag25", Double.valueOf(Utils.hexToDecimal(data.substring(96,100))));
			map.put("tag26", Double.valueOf(Utils.hexToDecimal(data.substring(100,104))));
			map.put("tag27", Double.valueOf(Utils.hexToDecimal(data.substring(104,108))));
			map.put("tag28", Double.valueOf(Utils.hexToDecimal(data.substring(108,112))));
			map.put("tag29", Double.valueOf(Utils.hexToDecimal(data.substring(112,116))));
			
		} catch (Exception e) {
			LOGGER.severe("Exception while parse modbus data."+e);
		}
		return map;
	}
	

	private Map<String, String> getStatusBit(String statusbit, String imeiNo){
		Map<String, String> map = new LinkedHashMap<>();
		String statusBin[] = Utils.decToBinary(Utils.hexToDecimal(statusbit)).split("");
		try {
			if(statusBin.length <= 1) {
				setEmailStatus(statusBin, imeiNo, map);
				return map;
			}
			
			map.put("R i/p lo", statusBin[0]);
			map.put("Y i/p lo", statusBin[1]);
			map.put("B i/p lo", statusBin[2]);
			map.put("R i/p hi", statusBin[3]);
			map.put("Y i/p hi", statusBin[4]);
			map.put("B i/p hi", statusBin[5]);
			map.put("R o/p lo", statusBin[6]);
			map.put("Y o/p lo", statusBin[7]);
			map.put("B o/p lo", statusBin[8]);
			map.put("R o/p hi", statusBin[9]);
			map.put("Y o/p hi", statusBin[10]);
			map.put("B o/p hi", statusBin[11]);
			map.put("phr", statusBin[12]);
			map.put("env", statusBin[13]);	
			map.put("mech flt", statusBin[14]);
			map.put("on/off", statusBin[15]);
			map.put("auto/manual", statusBin[16]);
			map.put("phr e/d", statusBin[17]);
			map.put("env e/d", statusBin[18]);
			
			
		} catch (Exception e) {
			LOGGER.info("status map."+e);
		}
		setEmailStatus(statusBin, imeiNo, map);
		return map;
	}
	
	private void setEmailStatus(String statusBin[], String imeiNo, Map<String, String> map) {
		try {
			if(statusBin == null || statusBin.length <= 0) {
				//reset all the status values as 0
				for(Map.Entry<String, List<MailBean>> statusMap : MailService.statusMap.entrySet()) {
					statusMap.getValue().forEach(val->{
						val.setStatus(0);
					});
				}
				return;
			}
			
			List<MailBean> statusList = MailService.statusMap.get(imeiNo);
			if(statusList == null || statusList.size()==0) {
				Properties properties = IoTService.properties;
				Long emailInterval = Long.valueOf(properties.getProperty(EMAIL_INTERVAL));
				List<MailBean> list = new ArrayList<>();
				map.forEach((key, val)->{
					Long occuredTime = System.currentTimeMillis();
					list.add(new MailBean(Integer.valueOf(val), key, occuredTime, 0L, emailInterval, imeiNo));
				});
				MailService.statusMap.put(imeiNo, list);
				return;
			}
			
			map.forEach((key, val)->{
				for (MailBean bean :MailService.statusMap.get(imeiNo)) {
					if(bean.getTagName().equalsIgnoreCase(key)) {
						bean.setStatus(Integer.valueOf(val));
						bean.setOccurredTime(System.currentTimeMillis());
					}
				}
			});
			
		} catch (Exception e) {
			LOGGER.severe("Exception while set an email status.."+e);
		}
	}
	
	private HashMap<String, Double> parseModbusdataTwo(String data) {
		HashMap<String, Double> map = new LinkedHashMap<>();
		
		try {
			map.put("tag30", Double.valueOf(Utils.hexToDecimal(data.substring(0,4))));
			map.put("tag31", Double.valueOf(Utils.hexToDecimal(data.substring(4,8))));
			map.put("tag32", Double.valueOf(Utils.hexToDecimal(data.substring(8,12))));
			map.put("tag33", Double.valueOf(Utils.hexToDecimal(data.substring(12,16))));
			map.put("tag34", Double.valueOf(Utils.hexToDecimal(data.substring(16,20))));
			map.put("tag35", Double.valueOf(Utils.hexToDecimal(data.substring(20,24))));
			map.put("tag36", Double.valueOf(Utils.hexToDecimal(data.substring(24,28))));
			map.put("tag37", Double.valueOf(Utils.hexToDecimal(data.substring(28,32))));
			map.put("tag38", Double.valueOf(Utils.hexToDecimal(data.substring(32,36))));
			map.put("tag39", Double.valueOf(Utils.hexToDecimal(data.substring(36,40))));
			map.put("tag40", Double.valueOf(Utils.hexToDecimal(data.substring(40,44))));
			map.put("tag41", Double.valueOf(Utils.hexToDecimal(data.substring(44,48))));
			map.put("tag42", Double.valueOf(Utils.hexToDecimal(data.substring(48,52))));
			//map.put("tag43", Double.valueOf(Utils.hexToDecimal(data.substring(52,56))));
			//map.put("tag44", Double.valueOf(Utils.hexToDecimal(data.substring(56,60))));
		} catch (Exception e) {
			LOGGER.severe("Exception while parse modbus data."+e);
		}
		return map;
	}
	
	private Map<String, String> getDescriptionMap(){
		HashMap<String, String> descMap = new LinkedHashMap<>();
		descMap.put("tag1", "R phase-neutral i/p voltage");
		descMap.put("tag2", "Y phase-neutral i/p voltage");
		descMap.put("tag3", "B phase-neutral i/p voltage");
		descMap.put("tag4", "RY phase-phase i/p voltage");
		descMap.put("tag5", "YB phase-phase i/p voltage");
		descMap.put("tag6", "BR phase-phase i/p voltage");
		descMap.put("tag7", "R phase-neutral o/p voltage");
		descMap.put("tag8", "Y phase-neutral o/p voltage");
		descMap.put("tag9", "B phase-neutral o/p voltage");
		descMap.put("tag10", "RY phase-phase o/p voltage");
		descMap.put("tag11", "YB phase-phase o/p voltage");
		descMap.put("tag12", "BR phase-phase o/p voltage");
		descMap.put("tag13", "R phase load");
		descMap.put("tag14", "Y phase load");
		descMap.put("tag15", "B phase load");
		descMap.put("tag16", "frequency");
		descMap.put("tag17", "R phase angle");
		descMap.put("tag18", "Y phase angle");
		descMap.put("tag19", "B phase angle");
		descMap.put("tag20", "R phase KVA");
		descMap.put("tag21", "Y phase KVA");
		descMap.put("tag22", "B phase KVA");
		descMap.put("tag23", "R phase KW");
		descMap.put("tag24", "Y phase KW");
		descMap.put("tag25", "B phase KW");
		descMap.put("tag26", "R phase PF");
		descMap.put("tag27", "Y phase PF");
		descMap.put("tag28", "B phase PF");
		descMap.put("tag29", "KWH");
		descMap.put("tag30", "STATUS/ FAULTS");	
		return descMap;
	}
	
	private Map<String, String> getDescriptionMapTwo(){
		HashMap<String, String> descMap = new LinkedHashMap<>();
		descMap.put("tag32", "set volt");
		descMap.put("tag33", "sensitivity");
		descMap.put("tag34", "IHV");
		descMap.put("tag35", "ILV");
		descMap.put("tag36", "OHV");
		descMap.put("tag37", "OLV");
		descMap.put("tag38", "CTFS");
		descMap.put("tag39", "OVL");
		descMap.put("tag40", "ONTIME");
		descMap.put("tag41", "CUTTIME");
		descMap.put("tag42", "RSTTIME");
		descMap.put("tag43", "mech HI");
		descMap.put("tag44", "mech LO");
		return descMap;
	}
	
	public static void main(String[] args) {
		PacketParser pars = new PacketParser(null);
		//String data = "<0,867322034846857,P03,08,03/V2.3,5d669a7b,010f,64,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00dd00e200e40189018d00000000f4402c082c0806040404040414c45e405e404040404040406020202020200000000000c8791dafbc8900000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d15,02b5,38,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00c9000000000000cb00cc00cf00000000000000000000000000000000000000000000000000000000000000ff65002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d17,02b6,38,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,010318000000000000000000000000000000000000006cf40000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d1d,02b7,38,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00ca00000000000000000000000000000000006cf40000000000000000000000000000000000000000000000ff65002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d24,02b8,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00000000000000cc00cd00d0000000000000006cf40000000000000000000000000000000000000000000000ff65002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d26,02b9,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,0103180000000000000000000000000000000000006cf4f40000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d31,02ba,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d33,02bb,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,010318000000000000000000000000000000fa6cf4f400000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d34,02bc,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00ca00cc00cd000000000000d000000000000000000000000000000000000000000000000000000000000000ff65002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d36,02bd,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,0103180000000000000000000000000000000000000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d3c,02be,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00c600c700ca00000000c900cc00000000000000000000000000000000000000000000000000000000000000870e002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d3e,02bf,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,01000000000000000000000000000000006cf400000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d40,02c0,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00c600c300000000c800c900cc0000000000000000000000000000000000000000000000000000000000000086ce002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d4c,02c1,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00c800cb000000ca00cb00ce00000000000000000000000000000000000000000000000000000000000000009839002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d4e,02c2,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,0103180000000000000000000000000000000000000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d50,02c3,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00c8000000000000ca00cb00ce000000000000000000000000000000000000000000000000000000000000009839002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d51,02c4,45,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,01000000000000000000000000000000006cf400000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d60,02c5,42,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d62,02c6,42,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,010318000000000000000000000000000000000000006cf40000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d63,02c7,42,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c0000000000000000ca00cb00ce000000000000000000000000000000000000000000000000000000000000000039002295393c9900000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d65,02c8,42,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,1,0103180000000000000000000000000000000000000000000000000000,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d666d70,02c9,38,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,,,,,,,,,,,;";
		//String data ="<0,867322034536003,P03,08,03/V2.3,5d67f145,004b,70,0,FF,airtel,8991000902112317408F,,,,,,,,,,,,,,0,01033c00c000c200c501560000000000ee00f000f001ab01a001a400820084008901f40079007600780136013e014800000000014400000000006300000105b9b1,,,,,,,,,,;";
		String data = "<0,867322034536003,P03,08,03/V2.3,5d960836,11ad,32,0,FF,airtel,89914009129060936584,,,,,,,,,,,,,,0,01033c00e600e600e601900190019000c800c800c8015e015e015e00640064006401f4007800780078001400140014001200120012006300630063000000011d44,,,,,,,,,,;";
		pars.processMessage(data);
		//System.out.println(pars.configService.getParserData());
		
		//System.out.println(new BigInteger("0", 16).toString(2));
		/*String pk = "001033cb9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4f478";
		pk = pk.substring(6, pk.length()-4);
		System.out.println(pk);
		System.out.println(pk.substring(0,2));
		System.out.println(pk.substring(2, 4));*/
		//new PacketParser(null).parseModbusdata(pk);
		
	}
}
