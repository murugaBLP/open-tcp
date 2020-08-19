package com.iot.service.main;

import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import com.iot.service.listener.Listener;
import com.iot.service.listenerImpl.Service;
import com.iot.service.utils.Constants;
import com.iot.service.utils.LoggerService;
import com.iot.service.utils.Utils;

/**
 * @author Muruganandam
 *
 */

public class IoTService implements Constants{

	private static final Logger LOGGER = Logger.getLogger(IoTService.class.getName());
	public static Properties properties;
	private Utils utils;
	
	public Utils getUtils() {
		return utils;
	}
	public Properties getProperties() {
		return properties;
	}
	
	private void startService() {
		readConfigFile();
		registerService();
	}
	
	private void readConfigFile() {
		utils = new Utils();
		properties = utils.readConfigFile(configFileName);
	}
	
	private void registerService() {
		try {
			if(properties.get(PORTS) == null)
				return;
			
			String[] ports = properties.get(PORTS).toString().split(",");
			HashMap<Listener, Integer> map = new HashMap<>();
			map.put(new Service(), Integer.valueOf(ports[0]));
			//add multiple listener
			//map.put(new ModbusListener(), Integer.valueOf(properties.get(PORT_2).toString()));
			
			LOGGER.info("Service list :"+map.toString());
			new ServiceRegister().registerService(map);
		} catch (Exception e) {
			LOGGER.severe("Exception while registre service."+e);
		}
	}
	
	static {
		if(!new LoggerService().initLogger(loggerFileName))
			System.exit(1);
	}
	
	public static void main(String[] args) {
		LOGGER.info("****************************************");
		LOGGER.info("**         Starting IoT Service       **");
		LOGGER.info("****************************************");
		new IoTService().startService();
	}
}
