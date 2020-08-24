package net.zylklab.avroExamples.schemas.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.schemas.utils.ReportIO;

public class TestMaps extends Tests{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestMaps.class);

	public void testCompatibility(){
		
		// Read all schemas
		Schema schema1, schema2;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/schemas/maps/maps1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/schemas/maps/maps2.avsc"));
		} catch (IOException ex) {
			LOGGER.error("An error occurred while creating the array schemas. We won't perform any of these tests.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			return;			
		}
		

		// Keep results stored somewhere
		List<Integer> results = new ArrayList<Integer>();
		
		// Start results reporter
		ReportIO reporter = new ReportIO();
		
		LOGGER.info("Test 1: Maps have the same item type");
		// Schema 1
		results.add(test1(schema1, schema1)); // pass
		reporter.reportResults(results.get(results.size() - 1 ));

		LOGGER.info("Test 2: Maps have different item types, but you can promote from one to the other");
		// Schema 1 and 2
		results.add(test2(schema1, schema2)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 3: Maps have different item types, and you cannot promote from one to the other");
		// Schema 1 and 2
		results.add(test3(schema2, schema1)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// Return info
		reportSuccesses(results, 2);
		
		
	}

	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		Map<String, Integer> myMap1 = new HashMap<String, Integer>();
		myMap1.put("key_1", 1);
		GenericRecord avroData1 = new GenericData.Record(WriterSchema);
		avroData1.put("myData", myMap1);
		
		// Expected object
		Map<Utf8, Integer> expMyMap1 = new HashMap<Utf8, Integer>();
		expMyMap1.put(new Utf8("key_1"), 1);
		GenericRecord expAvroData1 = new GenericData.Record(ReaderSchema);
		expAvroData1.put("myData", expMyMap1);
		
		return myTester.testCompatibility(avroData1, WriterSchema, ReaderSchema, expAvroData1);
	}
	
	private int test2(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		Map<String, Integer> myMap2 = new HashMap<String, Integer>();
		myMap2.put("key_1", 1);
		GenericRecord avroData2 = new GenericData.Record(WriterSchema);
		avroData2.put("myData", myMap2);
		
		// Expected object
		Map<Utf8, Long> expMyMap2 = new HashMap<Utf8, Long>();
		expMyMap2.put(new Utf8("key_1"), (long) 1);
		GenericRecord expAvroData2 = new GenericData.Record(ReaderSchema);
		expAvroData2.put("myData", expMyMap2);
		
		return myTester.testCompatibility(avroData2, WriterSchema, ReaderSchema, expAvroData2);
	}
	
	private int test3(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		Map<String, Long> myMap3 = new HashMap<String, Long>();
		myMap3.put("key_1", (long) 1);
		GenericRecord avroData3 = new GenericData.Record(WriterSchema);
		avroData3.put("myData", myMap3);
		
		// Expected output
		GenericRecord expAvroData3 = null;
		
		return myTester.testCompatibility(avroData3, WriterSchema, ReaderSchema, expAvroData3);
	}
	
}
