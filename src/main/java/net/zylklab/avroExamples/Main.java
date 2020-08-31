package net.zylklab.avroExamples;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.protocols.TestCommunications;
import net.zylklab.avroExamples.protocols.TestStress;
import net.zylklab.avroExamples.schemas.TestSchemas;

public class Main {

	static Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Chooses which test to run.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		String mainOption = args[0];
		String divisor = "============================================";
		
		if(mainOption.equals("schemas")) {
			LOGGER.info(divisor);
			LOGGER.info("Testing the evolution rules of the Avro Schemas.");
			LOGGER.info(divisor);
			TestSchemas.run();
			
		} else if(mainOption.equals("communication")) {
			LOGGER.info(divisor);
			LOGGER.info("Testing the communication protocols between different clients/servers using Avro IPC.");
			LOGGER.info(divisor);
			TestCommunications.run();
			
		} else if(mainOption.equals("stress")) {
			LOGGER.info(divisor);
			LOGGER.info("Testing how different servers handle a high amount of petitions from multiple users.");
			LOGGER.info(divisor);
			TestStress.run();
			
		} else {
			LOGGER.info(divisor);
			LOGGER.info("The selected option is not a test. Please select one of the following: schemas, communication, stress.");
			LOGGER.info(divisor);
		
			
			
		}
		
	}
	
}
