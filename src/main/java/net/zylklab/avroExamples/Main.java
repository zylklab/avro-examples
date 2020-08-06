package main.java.net.zylklab.avroExamples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.tests.TestArrays;
import main.java.net.zylklab.avroExamples.tests.TestEnums;
import main.java.net.zylklab.avroExamples.tests.TestFixed;
import main.java.net.zylklab.avroExamples.tests.TestMaps;
import main.java.net.zylklab.avroExamples.tests.TestPrimitives;
import main.java.net.zylklab.avroExamples.tests.TestRecords;
import main.java.net.zylklab.avroExamples.tests.TestUnions;

public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
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
	 * 
	 * @param args
	 */
    public static void main(String[] args) {

    	   	
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
