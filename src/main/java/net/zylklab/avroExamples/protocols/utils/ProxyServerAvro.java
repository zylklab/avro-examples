package net.zylklab.avroExamples.protocols.utils;

import org.apache.avro.ipc.Server;

public abstract class ProxyServerAvro extends Thread{
	
	protected Server server;

	protected int port;
	protected String host;
	
	protected boolean close = false;
	
	public abstract void setClose(boolean b);
}