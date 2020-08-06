package main.java.net.zylklab.avroExamples.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.tests.TestArrays;

public class ReportIO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);

	
	public void reportResults(int results) {
		
		if (results == 1) {
			LOGGER.info(">> Pass");
		} else {
			LOGGER.info(">> Fail");
		}
	}
	
}
