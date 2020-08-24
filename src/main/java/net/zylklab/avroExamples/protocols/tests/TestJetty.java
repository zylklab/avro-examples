package net.zylklab.avroExamples.protocols.tests;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;
import net.zylklab.avroExamples.protocols.Main;
import net.zylklab.avroExamples.protocols.utils.ClientAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ReportIO;

public class TestJetty extends CommunicationTests{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestJetty.class);
	
	protected ProxyServerAvroHTTP createServer(String host, int port) {
		
		// Start server
		LOGGER.info("Starting ServerAvroHTTP...");
		ProxyServerAvroHTTP server = new ProxyServerAvroHTTP(host, port);
		server.start();
		synchronized(server) {
		    try {
		        // Calling wait() until server is up
		    	server.wait();
		    } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                LOGGER.error("Thread interrupted", e); 
		    }
		}
		
		return server;
	}
	
	public void testCommunication(String host, int port) {
		
		// Create server (in a separate thread)
		ProxyServerAvroHTTP server = createServer(host, port);
		
		// Create client launcher
		ClientAvroHTTP client = new ClientAvroHTTP(host, port);
		
		// Start tests
		CommunicationTests ct = new CommunicationTests();
		ct.testCommunications(server, client);

		// Close server and clients
		client.closeClient();
		server.setClose(true);
		
		return;
	}

}
