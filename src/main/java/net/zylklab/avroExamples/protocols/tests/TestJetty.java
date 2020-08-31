package net.zylklab.avroExamples.protocols.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.protocols.utils.ClientAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroHTTP;


/**
 * Launches different tests related to the Jetty server.
 *
 */
public class TestJetty{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestJetty.class);
	
	/**
	 * Creates a Jetty server.
	 * 
	 * @param host Server's address
	 * @param port Server's communication port
	 * @return New server
	 */
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
	
	/**
	 * Creates a Jetty server and a Jetty client and runs all communication related tests.
	 * 
	 * @param host Server's address
	 * @param port Server's communication port
	 */
	public void runCommunicationTests(String host, int port) {
		
		// Create server (in a separate thread)
		ProxyServerAvroHTTP server = createServer(host, port);
				
		// Start communication tests
		ClientAvroHTTP client = new ClientAvroHTTP(host, port);
		CommunicationTests ct = new CommunicationTests();
		ct.testCommunications(server, client, 7);
		client.closeClient();

		// Close server and clients
		server.setClose(true);
		
		return;
	}
	
	/**
	 * Creates a Jetty server runs a stress test.
	 * 
	 * @param host Server's address
	 * @param port Server's communication port
	 */
	public void runStressTests(String host, int port) {
		
		// Create server (in a separate thread)
		ProxyServerAvroHTTP server = createServer(host, port);

		// Start stress tests
		StressTests st = new StressTests();
		st.testStress(server, host, port);

		// Close server and clients
		server.setClose(true);
		
		return;
	}

}
