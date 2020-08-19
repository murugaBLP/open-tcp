package com.iot.service.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iot.service.bean.LiveDataBean;
import com.iot.service.bean.ModbusData;
import com.iot.service.bean.ParserConfig;
import com.iot.service.utils.Constants;

/**
 * @author Muruganandam
 *
 */

public class HttpService implements Constants{

	private static final Logger LOGGER = Logger.getLogger(HttpService.class.getName());
	
	
	
	/**
	 * test function
	 * @return
	 */
	private String processData() {
		String data = "<0,867322034846857,P03,08,03/V2.3,5d5d2510,0098,48,0,FF,airtel,8991000902123575366F,,,,,,,,,,,,,,001033cb9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4f478,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d5d2512,0099,48,0,FF,airtel,8991000902123575366F,,,,,,,,,,,,,,1010318b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4773b,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d5d2514,009a,48,0,FF,airtel,8991000902123575366F,,,,,,,,,,,,,,001033cc3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3be36ec,,,,,,,,,,;<0,867322034846857,P03,08,03/V2.3,5d5d2515,009b,48,0,FF,airtel,8991000902123575366F,,,,,,,,,,,,,,1010318c3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3bec3be5b0c,,,,,,,,,,;";
		
		String[] array = data.split("<"); 
		Arrays.stream(array).forEachOrdered(item->{
			if(!item.startsWith(",") && item.endsWith(";")) {
				String[] msg = item.split(",");
				
				/*Arrays.stream(msg).forEachOrdered(i ->{
					System.out.println(i);
				});*/
			}
		});
		return null;
	}
	public void doPost(LiveDataBean bean, String url) {
		try {
			Gson gson = new Gson(); 
			HttpClient  httpClient = HttpClientBuilder.create().build();
			HttpPost  post  = new HttpPost(url);
			StringEntity postData = new StringEntity(gson.toJson(bean));//gson.tojson() converts your pojo to json
			//LOGGER.info("Json :"+gson.toJson(bean));
			post.setEntity(postData);
			post.setHeader(CONTENT_TYPE, APP_JSON);
			HttpResponse  response = httpClient.execute(post);
			LOGGER.info("Post live message response : "+response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			LOGGER.severe("Exception while send HTTP request."+e);
		}
	}
	
	public String doGet(String url) {
		try {
			HttpClient  client = HttpClientBuilder.create().build();
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			String body = handler.handleResponse(response);
			int code = response.getStatusLine().getStatusCode();
			LOGGER.info("Response code :"+code);
			return body;
		} catch (Exception e) {
			LOGGER.severe("Exception in get request.."+e);
			e.printStackTrace();
			return null;
		}
	}
	
	public void getDeviceInfo() {
		try {
			HttpClient  client = HttpClientBuilder.create().build();
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpGet get = new HttpGet(DEVICE_INFO);
			HttpResponse response = client.execute(get);
			String body = handler.handleResponse(response);
			int code = response.getStatusLine().getStatusCode();
			
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(body);
			Map<String, String> deviceInfo = new HashMap<String, String>();
			JsonArray arr = json.get(HITS).getAsJsonObject().get(HITS).getAsJsonArray();
			for(JsonElement array : arr) {
				System.out.println(array.getAsJsonObject().get(SOURCE).toString());
				//DeviceInfoBean info = new Gson().fromJson(array.getAsJsonObject().get("_source"), DeviceInfoBean.class);
				deviceInfo.put(array.getAsJsonObject().get(SOURCE).getAsJsonObject().get(IMEI_NO).toString(), array.getAsJsonObject().get(SOURCE).toString());
			}
			System.out.println("info : "+deviceInfo.toString());
			
			System.out.println("code :"+code);
		} catch (Exception e) {
			LOGGER.severe("Exception in get request.."+e);
			e.printStackTrace();
		}
	}
	
	public void getParserInfo() {
		try {
			HttpClient  client = HttpClientBuilder.create().build();
			ResponseHandler<String> handler = new BasicResponseHandler();
			HttpGet get = new HttpGet(PARSER_INFO);
			HttpResponse response = client.execute(get);
			String body = handler.handleResponse(response);
			int code = response.getStatusLine().getStatusCode();
			
			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(body);
			Map<String, ParserConfig> deviceInfo = new HashMap<String, ParserConfig>();
			JsonArray arr = json.get(HITS).getAsJsonObject().get(HITS).getAsJsonArray();
			for(JsonElement array : arr) {
				ModbusData infos = new Gson().fromJson(array.getAsJsonObject().get(SOURCE), ModbusData.class);
				ParserConfig info = infos.getModbusData().get(0);
				deviceInfo.put(info.getFirmwareVersion()+info.getDataType(), info);
			}
		} catch (Exception e) {
			LOGGER.severe("Exception in get request.."+e);
			e.printStackTrace();
		}
	}
	
	public static String convertHexToString(String hex){

		  StringBuilder sb = new StringBuilder();
		  StringBuilder temp = new StringBuilder();
		  
		  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
		  for( int i=0; i<hex.length()-1; i+=2 ){
			  
		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      //convert the decimal to character
		      sb.append((char)decimal);
			  
		      temp.append(decimal);
		  }
		  //System.out.println("Decimal : " + temp.toString());
		  
		  return sb.toString();
	  }
	
	public static void main(String[] args) {
		//new HttpService().processData();
		/*String data = "001033cb9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4b9b4f478";
		System.out.println("String :"+convertHexToString("10")); 
		String hex="5d5d2510";  
		int decimal=Integer.parseInt(hex,16);  
		System.out.println(decimal); 
		Date expiry = new Date(Long.parseLong(String.valueOf(decimal))*1000);
		System.out.println(expiry);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("Date :"+format.format(expiry));*/
		HttpService httpService = new HttpService();
		//httpService.getParserInfo();
		httpService.getDeviceInfo();
	}
}
