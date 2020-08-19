package com.iot.service.utils;

import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Muruganandam
 *
 */

public class Utils {
	
	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
	
	public Properties readConfigFile(String fileName) {
		if(fileName == null || fileName.isEmpty())
			return null;
		LOGGER.info("Service going to load config file."+fileName);
		Properties properties = null;
		 try {
			File jarPath=new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
	        LOGGER.info("File path."+propertiesPath);
	        FileReader reader=new FileReader(propertiesPath+fileName);
			properties = new Properties();
			properties.load(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return properties;
	}
	
	public static int hexToDecimal(String hexVal) {
		try {
			if(hexVal == null || hexVal.trim().isEmpty())
				return 0;
			return Integer.parseInt(hexVal,16);
		} catch (Exception e) {
			LOGGER.severe("Exception while convert hex to decimal."+hexVal);
		}
		return 0;
	}
	
	public static String epochDate(int date, String dateFormat) {
		try {
			Date expiry = new Date(Long.parseLong(String.valueOf(date))*1000);
			System.out.println(expiry);
			DateFormat format = new SimpleDateFormat(dateFormat);
			String outDate = format.format(expiry);
			LOGGER.info("Date converted :"+outDate);
			return outDate;
		} catch (Exception e) {
			LOGGER.severe("Exception while convert date."+e);
		}
		return null;
	}
	
	public static String hexToBin(String s) {
		  return new BigInteger(s, 16).toString(2);
	}
	
	public static String decToBinary(int n) {
		String bin = Integer.toBinaryString(n);
		System.out.println("bin :"+bin);
		return bin;
	}
	
	public static void main(String[] args) {
		System.out.println(hexToDecimal("00e2"));
		decToBinary(123);
	}
}
