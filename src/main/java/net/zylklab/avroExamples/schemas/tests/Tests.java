package net.zylklab.avroExamples.schemas.tests;


import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.schemas.utils.TestCompatibility;

public abstract class Tests {

	private static final Logger LOGGER = LoggerFactory.getLogger(Tests.class);
	
	/**
	 * Object that runs the tests.
	 */
	protected TestCompatibility myTester = new TestCompatibility();
	
	/**
	 * Runs all tests.
	 */
	public abstract void testCompatibility();		
	
	/**
	 * Report the number of successes. 
	 * 
	 * @param results List with the results of all tests.
	 * @param expectedHits	Expected number of tests with a positive output
	 */
	protected void reportSuccesses(List<Integer> results, int expectedHits) {
		// Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		
		LOGGER.info("============================================");
		LOGGER.info("Expected " + expectedHits + " tests to run successfully. Got " + count + ".");
		LOGGER.info("============================================");
	}
	
}
