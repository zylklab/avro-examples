package net.zylklab.avroExamples.protocols.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.protocols.utils.ClientAvroRPC;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroRPC;

/**
 * Launches different tests related to the Netty server.
 *
 */
public class TestNetty{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestNetty.class);
	
	/**
	 * Creates a Netty server.
	 * 
	 * @param host Server's address
	 * @param port Server's communication port
	 * @return New server
	 */
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
	
	/**
	 * Creates a Netty server and a Netty client and runs all communication related tests.
	 * 
	 * @param host Server's address
	 * @param port Server's communication port
	 */
	public void runCommunicationTests(String host, int port) {
		
		// Create server (in a separate thread)
		ProxyServerAvroRPC server = createServer(host, port);
		
		// Start communication tests
		ClientAvroRPC client = new ClientAvroRPC(host, port);
		CommunicationTests ct = new CommunicationTests();
		ct.testCommunications(server, client, 8);
		client.closeClient();
		
		// Close server and clients
		server.setClose(true);
		
		return;
		
	}
	
	/**
	 * Creates a Netty server runs a stress test.
	 * 
	 * @param host Server's address
	 * @param port Server's communication port
	 */
	public void runStressTests(String host, int port) {
		
		// Create server (in a separate thread)
		ProxyServerAvroRPC server = createServer(host, port);
		
		// Start stress tests
		StressTests st = new StressTests();
		st.testStress(server, host, port);

		// Close server and clients
		server.setClose(true);
		
		return;
		
	}
	
}
