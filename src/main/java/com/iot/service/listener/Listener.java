package com.iot.service.listener;

/**
 * @author Muruganandam
 *
 */

@FunctionalInterface
public interface Listener {
	void onMessage(String message);
}
