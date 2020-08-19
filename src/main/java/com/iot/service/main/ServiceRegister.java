package com.iot.service.main;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.iot.service.ServiceManager;
import com.iot.service.listener.Listener;

/**
 * @author Muruganandam
 *
 */

public class ServiceRegister {

	private static final Logger LOGGER = Logger.getLogger(ServiceRegister.class.getName());
	
	public void registerService(HashMap<Listener, Integer> map) {
		createThreadPool(map);
	}
	
	private void createThreadPool(HashMap<Listener, Integer> map) {
		ExecutorService executor = Executors.newFixedThreadPool(map.size());
		map.forEach((listener, port)->{
			ServiceManager task = new ServiceManager();
			task.addListener(listener, port);
			LOGGER.info("Registring port :"+port);
			executor.submit(task);
		});
		executor.shutdown();
		while(!executor.isTerminated()) {}
		LOGGER.info("Completed all the threads.");
	}
}
