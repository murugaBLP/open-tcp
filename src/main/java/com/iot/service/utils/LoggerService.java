package com.iot.service.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;

/**
 * @author Muruganandam
 *
 */

public class LoggerService {

	private static final Logger LOGGER = Logger.getLogger(LoggerService.class.getName());
	
	public boolean initLogger(String fileName) {
		try {
			File jarPath=new File(LoggerService.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	        String propertiesPath=jarPath.getParentFile().getAbsolutePath();
	        LogManager logManager = LogManager.getLogManager();
			logManager.readConfiguration(new FileInputStream(propertiesPath+fileName));
			return true;
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Error in loading configuration",exception);
			return false;
		}	
	}
	
	
	public static void main(String[] args) {
		new LoggerService().initLogger("/../conf/logger_service.properties");
		IntStream.range(1, 1000).forEachOrdered(action->{
			LOGGER.fine("Fine message logged");
			LOGGER.info("INFO");
			LOGGER.warning("WARNING");
			LOGGER.severe("sever");
		});
		
	}
}
