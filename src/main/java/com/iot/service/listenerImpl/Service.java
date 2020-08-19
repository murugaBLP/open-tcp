package com.iot.service.listenerImpl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import com.iot.service.listener.Listener;
import com.iot.service.parser.PacketParser;

/**
 * @author Muruganandam
 *
 */
public class Service implements Listener{
	
	private static final Logger LOGGER = Logger.getLogger(Service.class.getName());
	BlockingQueue<String> queue = null;
	PacketParser parser = null;
	
	public Service() {
		queue = new LinkedBlockingQueue<String>();
		parser = new PacketParser(queue);
		LOGGER.info("que : "+queue.hashCode());
		LOGGER.info("Service going to start consumer thread.");
		new Thread(parser).start();
	}
	
	@Override
	public void onMessage(String message) {
		LOGGER.info("Received message . "+message+", "+Thread.currentThread().getName());
		try {
			if(message == null)
				return;
			
			queue.put(message);
			LOGGER.info("Msg sent.."+queue.size());
		} catch (InterruptedException e) {
			LOGGER.severe("Exception while send message to queue."+e);
		}
	}
}
