package net.zylklab.avroExamples.protocols.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.protocols.utils.ClientAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ClientAvroRPC;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroRPC;

public class TestNetty extends CommunicationTests{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestNetty.class);
	
	protected ProxyServerAvroRPC createServer(String host, int port) {
		// Start server
		LOGGER.info("Starting ServerAvroRPC...");
		ProxyServerAvroRPC server = new ProxyServerAvroRPC(host, port);
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
		ProxyServerAvroRPC server = createServer(host, port);
		
		// Create client launcher
		ClientAvroRPC client = new ClientAvroRPC(host, port);
		
		// Start tests
		CommunicationTests ct = new CommunicationTests();
		ct.testCommunications(server, client);

		// Close server and clients
		client.closeClient();
		server.setClose(true);
		
		return;
		
	}
}
