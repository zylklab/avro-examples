package net.zylklab.avroExamples.schemas.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportIO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportIO.class);

	/**
	 * Given the output of a test, prints the result in a more humanly readible format.
	 * 
	 * @param results Result of the test. Positive values are a pass. Negative ones are not.
	 */
	public void reportResults(int results) {
		
		if (results == 1) {
			LOGGER.info(">>>> Pass");
		} else {
			LOGGER.info(">>>> Fail");
		}
	}
	
}
