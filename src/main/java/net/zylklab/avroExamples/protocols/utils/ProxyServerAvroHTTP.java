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



public class ProxyServerAvroHTTP extends ProxyServerAvro{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServerAvroHTTP.class);
	
	public Server server;

	private int port;
	private String host;
	
	private boolean close = false;
	
	public ProxyServerAvroHTTP(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public void run(){
		try {
			server = new HttpServer(new SpecificResponder(MyTests.class, new MyTestsImpl()), port);
			server.start();
			LOGGER.info("Server is listening at port " + server.getPort());
			LOGGER.info("Server started.");
			synchronized(this) {
				notifyAll();
			}
			while(!close) {
				Thread.sleep(1000);
			}
		} catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			server.close();
			LOGGER.info("Server closed.");
		}
		return;
	}
	
	public void setClose(boolean b) {
		close = b;
		return;
	}
}