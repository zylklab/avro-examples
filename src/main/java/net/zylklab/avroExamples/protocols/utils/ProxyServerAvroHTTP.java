package net.zylklab.avroExamples.protocols.utils;

import java.net.InetSocketAddress;

import org.apache.avro.ipc.jetty.HttpServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.zylklab.avroExamples.generated.MyTests;

/**
 * HTTP server using Jetty
 */
public class ProxyServerAvroHTTP extends ProxyServerAvro{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyServerAvroHTTP.class);
	
	public ProxyServerAvroHTTP(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Run a new server on a separate thread.
	 */
	public void run(){
		try {
			server = new HttpServer(new SpecificResponder(MyTests.class, new MyTestsImpl()), new InetSocketAddress(host, port));
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