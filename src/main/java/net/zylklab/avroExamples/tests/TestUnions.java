package main.java.net.zylklab.avroExamples.tests;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.utils.TestCompatibility;

public class TestUnions {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);

	public void testArrayCompatibility() {

		// Read all schemas
		Schema schema1, schema2, schema3, schema4, schema5, schema6, schema7, schema8, schemaPrim1, schemaPrim2, schemaPrim3;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/unions/unions1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/unions/unions2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/unions/unions3.avsc"));
			schema4 = new Schema.Parser().parse(new File("src/main/avro/unions/unions4.avsc"));
			schema5 = new Schema.Parser().parse(new File("src/main/avro/unions/unions5.avsc"));
			schema6 = new Schema.Parser().parse(new File("src/main/avro/unions/unions6.avsc"));
			schema7 = new Schema.Parser().parse(new File("src/main/avro/unions/unions7.avsc"));
			schema8 = new Schema.Parser().parse(new File("src/main/avro/unions/unions8.avsc"));

			schemaPrim1 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives1.avsc"));
			schemaPrim2 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives2.avsc"));
			schemaPrim3 = new Schema.Parser().parse(new File("src/main/avro/primitives/primitives3.avsc"));
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

		LOGGER.info("Test 1: Union types are the same");
		// Schema 1
		int testValue1 = 1;
		GenericRecord avroData1 = new GenericData.Record(schema1);
		avroData1.put("myData", testValue1);
		results.add(myTester.testCompatibility(avroData1, schema1, schema1)); // Pass

		LOGGER.info("Test 2: Union types are not the same but can be promoted");
		// Schema 1 and schema 2
		String testValue2 = "Hello";
		GenericRecord avroData2 = new GenericData.Record(schema1);
		avroData2.put("myData", testValue2);
		results.add(myTester.testCompatibility(avroData2, schema1, schema2)); // Pass


		// TODO Missing a test where union types are not the same and cannot be promoted?

		LOGGER.info(
				"Test 3: Union types of both schemas intersect, but one does not contain the other. There is no default values and object type is present in both unions.");
		// Schema 1 and schema 3
		int testValue3 = 1;
		GenericRecord avroData3 = new GenericData.Record(schema1);
		avroData3.put("myData", testValue3);
		results.add(myTester.testCompatibility(avroData3, schema1, schema3)); // Pass

		LOGGER.info(
				"Test 4: Union types of both schemas intersect, but one does not contain the other. There is no default values and object type is not present in the reader's union schema.");
		// Schema 1 and schema 3
		String testValue4 = "Hello";
		GenericRecord avroData4 = new GenericData.Record(schema1);
		avroData4.put("myData", testValue4);
		results.add(myTester.testCompatibility(avroData4, schema1, schema3)); // Fail


		LOGGER.info(
				"Test 5: Union types of both schemas intersect, but one does not contain the other. There are default values and object type is not present in the reader's union schema.");
		// Schema 4 and schema 5
		String testValue51 = "Hello";
		GenericRecord avroData51 = new GenericData.Record(schema4);
		avroData51.put("myData", testValue51);
		results.add(myTester.testCompatibility(avroData51, schema4, schema5)); // Fail
		
		GenericRecord avroData52 = new GenericData.Record(schema5);
		avroData52.put("myData", null);
		results.add(myTester.testCompatibility(avroData52, schema5, schema4)); // Fail

		LOGGER.info(
				"Test 6: One schema has an union while the other has a primitive type that matches the fist element of the union. We test this both ways.");
		// Union schema 1 and primitive schema 1
		int testValue61 = 1;
		GenericRecord avroData61 = new GenericData.Record(schema1);
		avroData61.put("myData", testValue61);
		results.add(myTester.testCompatibility(avroData61, schema1, schemaPrim1)); // Pass
		
		int testValue62 = 1;
		GenericRecord avroData62 = new GenericData.Record(schemaPrim1);
		avroData62.put("myData", testValue62);
		results.add(myTester.testCompatibility(avroData62, schemaPrim1, schema1)); // Pass

		LOGGER.info(
				"Test 7: One schema has an union while the other has a primitive type does not match the fist element of the union (But it is still part of the union).");
		// Union schema 1 and primitive schema 2
		int testValue71 = 1;
		GenericRecord avroData71 = new GenericData.Record(schema1);
		avroData71.put("myData", testValue71);
		results.add(myTester.testCompatibility(avroData71, schema1, schemaPrim2)); // Fail
		
		String testValue72 = "Hello";
		GenericRecord avroData72 = new GenericData.Record(schemaPrim2);
		avroData72.put("myData", testValue72);
		results.add(myTester.testCompatibility(avroData72, schemaPrim2, schema1)); // Pass
		
		LOGGER.info(
				"Test 8: One schema has an union while the other has a primitive type that matches a promotion of the fist element of the union.");
		// Union schema 6 and primitive schema 3
		String testValue81 = "Hello";
		GenericRecord avroData81 = new GenericData.Record(schema6);
		avroData81.put("myData", testValue81);
		results.add(myTester.testCompatibility(avroData81, schema6, schemaPrim3)); // Pass
		
		ByteBuffer testValue82 = ByteBuffer.wrap("Hello".getBytes());
		GenericRecord avroData82 = new GenericData.Record(schemaPrim3);
		avroData82.put("myData", testValue82);
		results.add(myTester.testCompatibility(avroData82, schemaPrim3, schema6)); // Pass
		
		
		// LOGGER.info(
		//		"Test 9: One schema has an union while the other has a primitive type that does not match the fist element of the union (or a promotion).");
		// Union schema 6 and primitive schema 3
		// TODO Completely unnecessary, right?
		
		//
		// COMPLEX TYPES
		//
		
		LOGGER.info("Test 9: Union types are the same and at least one of them is a complex type");
		// Schema 7
		List<String> testArray1 = new ArrayList<String>();
		testArray1.add("Hello");
		GenericRecord avroData9 = new GenericData.Record(schema7);
		avroData9.put("myData", testArray1);
		results.add(myTester.testCompatibility(avroData9, schema7, schema7)); // Pass
		

		LOGGER.info("Test 10: Union types are not the same but can be promoted. At least one of them is a complex type");
		// Schema 7 and 8
		List<String> testArray21 = new ArrayList<String>();
		testArray21.add("Hello");
		GenericRecord avroData101 = new GenericData.Record(schema7);
		avroData101.put("myData", testArray21);
		results.add(myTester.testCompatibility(avroData101, schema7, schema8)); // Pass
		
		List<ByteBuffer> testArray22 = new ArrayList<ByteBuffer>();
		testArray22.add(ByteBuffer.wrap("Hello".getBytes()));
		GenericRecord avroData102 = new GenericData.Record(schema8);
		avroData102.put("myData", testArray22);
		results.add(myTester.testCompatibility(avroData102, schema8, schema7)); // Pass
		
		// TODO Keep doing tests with complex types? Feels a bit unnecessary.
		
		// Return info
		// // Count number of successes
		int count = 0;
		for (Integer i : results) {
			if (i > 0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + 11 + " tests to run successfully. Got " + count); // TODO Hardcoded number here

	}

}
