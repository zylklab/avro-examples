package net.zylklab.avroExamples.protocols;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.protocols.tests.TestJetty;
import net.zylklab.avroExamples.protocols.tests.TestNetty;


public class TestCommunications {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestCommunications.class);
	
	/**
	 * Default string used during testing.
	 */
	public static final String DEFAULT_STRING = "Hello";
	/**
	 * Default integer used during testing.
	 */
	public static final int DEFAULT_INT = 1;
	/**
	 * Server's address
	 */
	private static String HOST = "localhost";
	/**
	 * Server port to use for communication.
	 */
	private static int PORT = 10000;
	
	
	/**
	 * Runs all the tests on Avro's communication protocols.
	 */
	public static void run() {
		
		// Test an HTTP server
        LOGGER.info("Testing communication with an HTML server (Jetty)...");
        TestJetty testJetty = new TestJetty();
        testJetty.runCommunicationTests(HOST, PORT);
        
        LOGGER.info("===========================");
        LOGGER.info("Waiting for all elements to close...");
        // TODO This is horrible coding
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        LOGGER.info("===========================");
        
     // Test a Netty server (synchronously)
        LOGGER.info("Testing communication with a Netty server...");
        TestNetty testNetty = new TestNetty();
        testNetty.runCommunicationTests(HOST, PORT);		
		
        LOGGER.info("Done");
		
	}
	
}
