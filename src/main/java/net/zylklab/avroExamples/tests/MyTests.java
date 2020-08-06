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

import main.java.net.zylklab.avroExamples.utils.ReportIO;


public class MyTests extends Tests{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyTests.class);
	
	public void testCompatibility(){
				
		// Read all schemas
		Schema schema1, schema2;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/myTests/myTests1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/myTests/myTests2.avsc"));
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
		
		LOGGER.info("Test 1: Arrays as posted in Slack");
		// Schema 1 and 2
		results.add(test1(schema1, schema2)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		
		// Return info
		reportSuccesses(results, 1);
		
	}
	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroData = new GenericData.Record(WriterSchema);
		avroData.put("myData", null);
		avroData.put("myData_1", null);
		avroData.put("myData_2", null);
		// Expected object
		GenericRecord expAvroData = new GenericData.Record(ReaderSchema);
		expAvroData.put("myData", null);
		expAvroData.put("myData_1", null);
		expAvroData.put("myData_2", null);
		expAvroData.put("extraData", null);
		
		return myTester.testCompatibility(avroData, WriterSchema, ReaderSchema, expAvroData);
	}
	
}
