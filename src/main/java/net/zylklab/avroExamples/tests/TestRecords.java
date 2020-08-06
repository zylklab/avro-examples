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

public class TestRecords {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);

	public void testArrayCompatibility(){
		
		// Read all schemas
		Schema schema1, schema2, schema3, schema4, schema5, schema6, schema7;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/records/records1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/records/records2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/records/records3.avsc"));
			schema4 = new Schema.Parser().parse(new File("src/main/avro/records/records4.avsc"));
			schema5 = new Schema.Parser().parse(new File("src/main/avro/records/records5.avsc"));
			schema6 = new Schema.Parser().parse(new File("src/main/avro/records/records6.avsc"));
			schema7 = new Schema.Parser().parse(new File("src/main/avro/records/records7.avsc"));

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
		
		
		LOGGER.info("Test 1: Both record schemas are the same");
		// Schema 1
		GenericRecord avroRecord = new GenericData.Record(schema1);
		avroRecord.put("data_1", 1);
		avroRecord.put("data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord, schema1, schema1)); // Pass
		
		LOGGER.info("Test 2: Both record schemas have the same unqualified names and types, but in a different order");
		// Schema 1 and 2
		GenericRecord avroRecord21 = new GenericData.Record(schema1);
		avroRecord21.put("data_1", 1);
		avroRecord21.put("data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord21, schema1, schema2)); // Pass
		
		GenericRecord avroRecord22 = new GenericData.Record(schema2);
		avroRecord22.put("data_2", "Hello");
		avroRecord22.put("data_1", 1);
		results.add(myTester.testCompatibility(avroRecord22, schema2, schema1)); // Pass
		
		LOGGER.info("Test 3: Both record schemas have the same field types but different unqualified names");
		// Schema 1 and 3
		GenericRecord avroRecord31 = new GenericData.Record(schema1);
		avroRecord31.put("data_1", 1);
		avroRecord31.put("data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord31, schema1, schema3)); // Fail
		
		GenericRecord avroRecord32 = new GenericData.Record(schema3);
		avroRecord32.put("new_data_1", 1);
		avroRecord32.put("new_data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord32, schema3, schema1)); // Fail
		
		LOGGER.info("Test 4: Both record schemas have the same unqualified names but the types are different (yet, you can promote from one to the other)");
		// Schema 1 and 4
		GenericRecord avroRecord41 = new GenericData.Record(schema1);
		avroRecord41.put("data_1", 1);
		avroRecord41.put("data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord41, schema1, schema4)); // Pass
		
		LOGGER.info("Test 5: Both record schemas have the same unqualified names but the types are different (you cannot promote from one to the other)");
		// Schema 4 and 1
		GenericRecord avroRecord51 = new GenericData.Record(schema4);
		avroRecord51.put("data_1", (long) 1);
		avroRecord51.put("data_2", ByteBuffer.wrap("Hello".getBytes()));
		results.add(myTester.testCompatibility(avroRecord51, schema4, schema1)); // Fail
		
		
		LOGGER.info("Test 6: Writter has a field that's not present in reader");
		// Schema 5 and 1
		GenericRecord avroRecord61 = new GenericData.Record(schema5);
		avroRecord61.put("data_1", (long) 1);
		avroRecord61.put("data_2", "Hello");
		avroRecord61.put("data_3", "Hello");
		results.add(myTester.testCompatibility(avroRecord61, schema5, schema1)); // Pass
		
		LOGGER.info("Test 7: Reader has a field that's not present in writer with no default value");
		// Schema 1 and 6
		GenericRecord avroRecord71 = new GenericData.Record(schema1);
		avroRecord71.put("data_1", 1);
		avroRecord71.put("data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord71, schema1, schema6)); // Fail
		
		LOGGER.info("Test 8: Reader has a field that's not present in writer with default value");
		// Schema 1 and 7
		GenericRecord avroRecord81 = new GenericData.Record(schema1);
		avroRecord81.put("data_1", 1);
		avroRecord81.put("data_2", "Hello");
		results.add(myTester.testCompatibility(avroRecord81, schema1, schema7)); // Pass
		
		
		// Return info
		// // Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + 6 + " tests to run successfully. Got " + count);		// TODO Hardcoded number here
		
		
	}
	
}
