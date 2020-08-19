package com.iot.service.listenerImpl;

import com.iot.service.listener.Listener;

/**
 * @author Muruganandam
 *
 */
public class ModbusListener implements Listener{

	@Override
	public void onMessage(String message) {
		System.out.println("Received message ."+message+", "+Thread.currentThread().getName());
	}
}
