package net.zylklab.avroExamples.protocols.utils;

import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.jetty.HttpServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.zylklab.avroExamples.generated.MyTests;
import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;
import net.zylklab.avroExamples.protocols.Main;


public abstract class ProxyServerAvro extends Thread{
	
	protected Server server;

	protected int port;
	protected String host;
	
	protected boolean close = false;
	
	public abstract void setClose(boolean b);
}