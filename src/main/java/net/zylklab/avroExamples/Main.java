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
	
	
    public static void main(String[] args) {
        
        LOGGER.info("Testing arrays...");
        TestArrays testArrays = new TestArrays();
        testArrays.testArrayCompatibility();
        
        LOGGER.info("Testing enums...");
        TestEnums testEnums = new TestEnums();
        testEnums.testArrayCompatibility();
        
        LOGGER.info("Testing fixed...");
        TestFixed testFixed = new TestFixed();
        testFixed.testArrayCompatibility();
        
        LOGGER.info("Testing maps...");
        TestMaps testMaps = new TestMaps();
        testMaps.testArrayCompatibility();
      
        LOGGER.info("Testing primitives...");
        TestPrimitives testPrimitives = new TestPrimitives();
        testPrimitives.testArrayCompatibility();
        
        LOGGER.info("Testing unions...");
        TestUnions testUnions = new TestUnions();
        testUnions.testArrayCompatibility();

        LOGGER.info("Testing records...");
        TestRecords testRecords = new TestRecords();
        testRecords.testArrayCompatibility();

    	
/*      LOGGER.info("My tests...");
        MyTests myTests = new MyTests();
        myTests.testArrayCompatibility();
*/    	
        LOGGER.info("Done");
        
    }
}
