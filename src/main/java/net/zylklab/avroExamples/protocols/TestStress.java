package net.zylklab.avroExamples.protocols;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.protocols.tests.TestJetty;
import net.zylklab.avroExamples.protocols.tests.TestNetty;


public class TestStress {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestStress.class);
	
	/**
	 * Default string used during testing.
	 */
	public static final String DEFAULT_STRING = "Hello";
	/**
	 * Default integer used during testing.
	 */
	public static final int DEFAULT_INT = 1;
	
	private static String host = "localhost";	// Communication host
	private static int port = 10000;	// Communication port 
	
	
	public static void run() {
		
		// Test an HTTP server
        LOGGER.info("Testing communication with an HTML server (Jetty)...");
        TestJetty testJetty = new TestJetty();
        testJetty.runStressTests(host, port);
        
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
        
		// Test an asynchronous server
        LOGGER.info("Testing communication with a Netty server...");
        TestNetty testNetty = new TestNetty();
        testNetty.runStressTests(host, port);		
		
        LOGGER.info("Done");
		
	}
	
}
