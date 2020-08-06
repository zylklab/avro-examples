package main.java.net.zylklab.avroExamples.tests;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.Fixed;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericFixed;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.utils.TestCompatibility;

public class TestFixed {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);

	public void testArrayCompatibility(){
		
		// Read all schemas
		Schema schema1, schema2, schema3;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/fixed/fixed1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/fixed/fixed2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/fixed/fixed3.avsc"));
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
		
		LOGGER.info("Test 1: Fixed types have the same size and unqualified names.");
		// Schema 1
		GenericFixed testValue1 = new Fixed(schema1, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord avroData1 = new GenericData.Record(schema1);
		avroData1.put("myData", testValue1);
		results.add(myTester.testCompatibility(avroData1, schema1, schema1)); // Pass

		LOGGER.info("Test 2: Fixed types have the same size but different unqualified names.");
		// Schema 1 and 2
		GenericFixed testValue2 = new Fixed(schema1, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord avroData2 = new GenericData.Record(schema1);
		avroData2.put("myData", testValue2);
		results.add(myTester.testCompatibility(avroData2, schema1, schema2)); // Fail
		
		LOGGER.info("Test 3: Fixed types have different size but same unqualified names.");
		// Schema 1 and 3
		GenericFixed testValue31 = new Fixed(schema1, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord avroData31 = new GenericData.Record(schema1);
		avroData31.put("myData", testValue31);
		results.add(myTester.testCompatibility(avroData31, schema1, schema3)); // Fail
		
		GenericFixed testValue32 = new Fixed(schema3, ByteBuffer.allocate(8).putInt(1).array());
		GenericRecord avroData32 = new GenericData.Record(schema3);
		avroData32.put("myData", testValue32);
		results.add(myTester.testCompatibility(avroData32, schema3, schema1)); // Fail
		
		
		// Return info
		// // Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + 1 + " tests to run successfully. Got " + count);		// TODO Hardcoded number here
		
	}
}
