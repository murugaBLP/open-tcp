package com.iot.service;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.iot.service.listener.Listener;

/**
 * @author Muruganandam
 *
 */
public class ServiceManager implements Runnable{

	private static final Logger LOGGER = Logger.getLogger(ServiceManager.class.getName());
	private Listener  listener;
	private int port;
	private final Map<String, ProcessThread> clientMap = new HashMap<>();
	
	public void addListener(Listener listener, int port) {
		this.listener = listener;
		this.port = port;
	}
	
	public Listener getListener() {
		return this.listener;
	}
	public Map<String, ProcessThread> getClientMap(){
		return this.clientMap;
	}
	
	@Override
	public void run() {
		LOGGER.info("Service listening port ... "+this.port);
		try {
			connectClient();
		} catch (Exception e) {
			LOGGER.severe("Exception in run function."+e);
		}
	}
	
	private void connectClient() {
		ServerSocket server = null;
		Socket clientSocket = null;
		try {
			server = new ServerSocket(this.port);
			while(true) {
				try {
					clientSocket = server.accept();
					new ProcessThread(clientSocket, this).start();
				} catch (Exception e) {
					LOGGER.severe("Exception while opening TCP socket."+this.port);
					LOGGER.severe(e.toString());
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Exception while listening TCP socket."+this.port + ", Ex:"+e);
			e.printStackTrace();
		}finally {
			try {
				if(server != null)
					server.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
