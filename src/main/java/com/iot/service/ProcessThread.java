package com.iot.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Muruganandam
 *
 */
public class ProcessThread extends Thread{
	
	private static final Logger LOGGER = Logger.getLogger(ProcessThread.class.getName());
	private Socket clientSocket = null;
	DataInputStream inStream = null;
	PrintStream outStream = null;
	ServiceManager manager = null;
	String clientName = null;
	Timestamp timestamp = null;
	
	public ProcessThread(Socket clientSocket,ServiceManager manager) {
		this.clientSocket = clientSocket;
		this.manager = manager;
	}
	
	public void run() {
		try {
			inStream = new DataInputStream(clientSocket.getInputStream());
			outStream = new PrintStream(clientSocket.getOutputStream());
			while (true) {
				try {
					String message = inStream.readUTF();
					//System.out.println("Message :" + message);
					this.clientName = "deviceName"+this.hashCode()+this.clientSocket.hashCode();
					if(!this.manager.getClientMap().containsKey(clientName))
						this.manager.getClientMap().put(clientName,this); //add new client
					this.manager.getListener().onMessage(message);
					this.timestamp = new Timestamp(new Date().getTime());
				} catch (Exception e) {
					System.out.println(this.manager.getClientMap().toString());
					LOGGER.severe("Client connection closed."+clientName);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.severe(e.toString());
		}finally {		
				try {
					this.inStream.close();
					this.outStream.close();
					this.clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	
}
