package net.zylklab.avroExamples.protocols.tests;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.protocols.TestCommunications;
import net.zylklab.avroExamples.protocols.utils.ClientAvro;
import net.zylklab.avroExamples.protocols.utils.ClientAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ClientAvroRPC;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvro;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroHTTP;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvroRPC;

/**
 * Implements and launches a stress test for a given server.
 */
public class StressTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationTests.class);

	/**
	 * Launches a stress test for a given server.
	 * 
	 * It creates a separate client for each request.
	 * 
	 * @param server
	 * @param host	Server's address
	 * @param port	Server's communication port
	 */
	public void testStress(ProxyServerAvro server, String host, int port) {

		// Number of requests we'll submit to the server. We create a separate client for each one of them.
		// TODO Should this be a customizable parameter? Maybe an incoming parameter for this method?
		int number_requests = 200;

		try {
			
			LOGGER.info("Starting stress test. This could take a while...");
			
			// Launch all clients in a separate thread.
	        ExecutorService executor = Executors.newFixedThreadPool(number_requests);
	        // Create a list to hold the Future object
	        List<Future<Integer>> pcs = new ArrayList<Future<Integer>>();
	        for(int i=0; i< number_requests; i++){
		        Callable<Integer> callable = new PrivateClient(server, host, port);
	            Future<Integer> future = executor.submit(callable);
	            pcs.add(future);
	        }
	        
	        // Wait for all threads to finish
	        for(Future<Integer> fut : pcs){
            		fut.get();
	        }
			
			// Print results
	        LOGGER.info("============================================");
			LOGGER.info("Stress test finished successfully");
			LOGGER.info("============================================");
			
		} catch (InterruptedException ex) {
			LOGGER.error("We caught an unhandled exception during the stress test.");
			LOGGER.error(ex.getMessage());
			ex.printStackTrace();
		} catch (ExecutionException ex) {
			LOGGER.error("We caught an unhandled exception during the stress test.");
			LOGGER.error(ex.getMessage());
			ex.printStackTrace();
		}
		

		
	}
	
	/**
	 * Stores information about a given server and launches requests to it when asked.
	 * 
	 * It creates clients on the fly.
	 * 
	 */
	private class PrivateClient implements Callable<Integer> {
		
		private ProxyServerAvro server;
		private String host;
		private int port;
		private Record_1 msg;
		
		/**
		 * Store information about the server. 
		 * 
		 * Does not create a client yet.
		 * 
		 * @param server
	 	 * @param host	Server's address
	 	 * @param port	Server's communication port
		 */
		public PrivateClient(ProxyServerAvro server, String host, int port) {
			
			// Fill server's information
			this.host = host;
			this.port = port;
			this.server = server;
			
		}
		/**
		 * Creates a client and requests an expensive operation to a server.
		 * 
		 * After receiving a response, it closes said client and finishes execution.
		 * 
		 * Can raise a RunTimeException if the response does not match the expected object.
		 * 
		 */
		public Integer call() {
			
			// Create default input record
			msg = new Record_1();
			msg.setPrimitiveValue(TestCommunications.DEFAULT_INT);
			msg.setComplexValue(TestCommunications.DEFAULT_STRING);
			
			// Assign client
			ClientAvro client = null;
			if (server instanceof ProxyServerAvroRPC) {
				client = new ClientAvroRPC(host, port);
			} else if (server instanceof ProxyServerAvroHTTP) {
				client = new ClientAvroHTTP(host, port);				
			} else {
				throw new RuntimeException();
			}
			
			// Send request to server and close client
			Record_1 response = client.sendExpensiveOperation(msg);
			client.closeClient();
			
			// Check response
			if (!msg.equals(response)) {
				throw new RuntimeException();
			}
			return 1;
		}
	
	}
	

	
}
