package com.iot.service.utils;

import java.util.logging.Logger;

/**
 * @author Muruganandam
 *
 */
public class RefreshDao implements Runnable, Constants{

	private static final Logger LOGGER = Logger.getLogger(RefreshDao.class.getName());
	Refresh refresh = null;
	
	public RefreshDao(Refresh refresh) {
		this.refresh = refresh;
	}
	
	public void run() {
		while(true) {
			try {
				LOGGER.info("Service going to refresh object!");
				refresh.load();
				refresh.switchObj();
				LOGGER.info("Refresh object completed!");
				Thread.sleep(REFRESH_TIME*60);
			} catch (Exception e) {
				LOGGER.severe("Exception while refresh the service.."+e);
			}
		}
	}
}
