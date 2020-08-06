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

public class TestPrimitives {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);

	public void testArrayCompatibility(){
		
		// Read all schemas
		Schema schema1, schema2, schema3, schema4;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives3.avsc"));
			schema4 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives4.avsc"));

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
		
		
		LOGGER.info("Test 1: Primitive values are the same");
		// Schema 1
		int testValue1 = 1;
		GenericRecord avroData1 = new GenericData.Record(schema1);
		avroData1.put("myData", testValue1);
		results.add(myTester.testCompatibility(avroData1, schema1, schema1)); // Pass
		
		
		LOGGER.info("Test 2: Primitive values are not the same but can be promoted");
		// Schema 2 and 3
		String testValue2 = "Hello";
		GenericRecord avroData2 = new GenericData.Record(schema2);
		avroData2.put("myData", testValue2);
		results.add(myTester.testCompatibility(avroData2, schema2, schema3)); // Pass
		
		LOGGER.info("Test 3: Primitive values are not the same and cannot be promoted");
		// Schema 1 and 2
		int testValue3 = 1;
		GenericRecord avroData3 = new GenericData.Record(schema1);
		avroData3.put("myData", testValue3);
		results.add(myTester.testCompatibility(avroData3, schema1, schema2)); // Fail

		LOGGER.info("Test 4: Primitive value is null");
		// Schema 4
		GenericRecord avroData4 = new GenericData.Record(schema4);
		avroData4.put("myData", null);
		results.add(myTester.testCompatibility(avroData4, schema4, schema4)); // Pass
		
		// Return info
		// // Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + 3 + " tests to run successfully. Got " + count);		// TODO Hardcoded number here
		
		
	}
	
}
