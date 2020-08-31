package net.zylklab.avroExamples.schemas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.schemas.tests.TestArrays;
import net.zylklab.avroExamples.schemas.tests.TestEnums;
import net.zylklab.avroExamples.schemas.tests.TestFixed;
import net.zylklab.avroExamples.schemas.tests.TestMaps;
import net.zylklab.avroExamples.schemas.tests.TestPrimitives;
import net.zylklab.avroExamples.schemas.tests.TestRecords;
import net.zylklab.avroExamples.schemas.tests.TestUnions;

public class TestSchemas {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestSchemas.class);
	
	/**
	 * Default string used during testing.
	 */
	public static final String DEFAULT_STRING = "Hello";
	/**
	 * Default integer used during testing.
	 */
	public static final int DEFAULT_INT = 1;
	
	
	/**
	 * Runs all the tests on Avro schemas.
	 */
    public static void run() {

    	   	
        LOGGER.info("Testing arrays...");
        TestArrays testArrays = new TestArrays();
        testArrays.testCompatibility();
        
        LOGGER.info("Testing enums...");
        TestEnums testEnums = new TestEnums();
        testEnums.testCompatibility();
        
        LOGGER.info("Testing fixed...");
        TestFixed testFixed = new TestFixed();
        testFixed.testCompatibility();
        
        LOGGER.info("Testing maps...");
        TestMaps testMaps = new TestMaps();
        testMaps.testCompatibility();
      
        LOGGER.info("Testing primitives...");
        TestPrimitives testPrimitives = new TestPrimitives();
        testPrimitives.testCompatibility();
        
        LOGGER.info("Testing records...");
        TestRecords testRecords = new TestRecords();
        testRecords.testCompatibility();
        
        LOGGER.info("Testing unions...");
        TestUnions testUnions = new TestUnions();
        testUnions.testCompatibility();
	
    	
        LOGGER.info("Done");
        
    }
    
    
}
