package net.zylklab.avroExamples.protocols.utils;

import org.apache.avro.ipc.Server;

/**
 * Default server using the Avro IPC library.
 *
 */
public abstract class ProxyServerAvro extends Thread{
	
	/**
	 * Avro's IPC Server
	 */
	protected Server server;

	/** 
	 * Host's address
	 */
	protected String host;
	/**
	 * Host's port
	 */
	protected int port;
	
	/**
	 * Is the server still running.
	 */
	protected boolean close = false;
	
	/**
	 * Stops the server.
	 * 
	 * @param b True for closing the server, false otherwise.
	 */
	public abstract void setClose(boolean b);
}