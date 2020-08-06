package main.java.net.zylklab.avroExamples.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.utils.TestCompatibility;


public class TestArrays {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);
	
	public void testArrayCompatibility(){
				
		// Read all schemas
		Schema schema1, schema2, schema3;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/arrays/arrays1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/arrays/arrays2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/arrays/arrays3.avsc"));
		} catch (IOException ex) {
			LOGGER.error("An error occurred while creating the array schemas. We won't perform any of these tests.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			return;			
		}
		
		// Instantiate the class that checks for compatibility
		TestCompatibility myTester = new TestCompatibility();
		
		// Keep results stored somewhere
		List<Integer> results = new ArrayList<Integer>();
		
		LOGGER.info("Test 1: Arrays have the same item type");
		// Schema 1
		List<String> testArray1 = new ArrayList<String>();
		testArray1.add("Hello");
		GenericRecord avroData1 = new GenericData.Record(schema1);
		avroData1.put("myData", testArray1);
		results.add(myTester.testCompatibility(avroData1, schema1, schema1)); // Pass
		
		LOGGER.info("Test 2: Arrays have different item types, but you can promote from one to the other");
		// Schema 1 and 2
		List<String> testArray2 = new ArrayList<String>();
		testArray2.add("Hello");
		GenericRecord avroData2 = new GenericData.Record(schema1);
		avroData2.put("myData", testArray2);
		results.add(myTester.testCompatibility(avroData2, schema1, schema2)); // Pass

		
		LOGGER.info("Test 3: Arrays have different item types, and you cannot promote from one to the other");
		// Schema 1 and 3
		List<String> testArray3 = new ArrayList<String>();
		testArray3.add("Hello");
		GenericRecord avroData3 = new GenericData.Record(schema1);
		avroData3.put("myData", testArray3);
		results.add(myTester.testCompatibility(avroData3, schema1, schema3)); // Fail

		
		
		// Return info
		// // Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + 2 + " tests to run successfully. Got " + count);		// TODO Hardcoded number here
		
	}
	
}
