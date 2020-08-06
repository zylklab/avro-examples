package main.java.net.zylklab.avroExamples.tests;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.Main;
import main.java.net.zylklab.avroExamples.utils.ReportIO;


public class TestArrays extends Tests{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyTests.class);

	public void testCompatibility(){
				
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
		
		// Keep results stored somewhere
		List<Integer> results = new ArrayList<Integer>();
		
		// Start results reporter
		ReportIO reporter = new ReportIO();
		
		LOGGER.info("Test 1: Arrays have the same item type");			
		// Schema 1
		results.add(test1(schema1, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 2: Arrays have different item types, but you can promote from one to the other"); 
		// Schema 1 and 2
		results.add(test2(schema1, schema2)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 3: Arrays have different item types, and you cannot promote from one to the other"); 
		// Schema 1 and 3
		results.add(test3(schema1, schema3)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		reportSuccesses(results, 2);
		
	}
	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		
		// Input object
		List<String> testArray1 = new ArrayList<String>();
		testArray1.add(Main.DEFAULT_STRING);
		GenericRecord avroData1 = new GenericData.Record(WriterSchema);
		avroData1.put("myData", testArray1);
		// Expected object
		List<Utf8> expTestArray1 = new ArrayList<Utf8>();
		expTestArray1.add(new Utf8(Main.DEFAULT_STRING));
		GenericRecord expAvroData1 = new GenericData.Record(ReaderSchema);
		expAvroData1.put("myData", new GenericData.Array<Utf8>(ReaderSchema.getField("myData").schema(), expTestArray1));
		
		return myTester.testCompatibility(avroData1, WriterSchema, ReaderSchema, expAvroData1);
		
	}
	
	private int test2(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		List<String> testArray2 = new ArrayList<String>();
		testArray2.add(Main.DEFAULT_STRING);
		GenericRecord avroData2 = new GenericData.Record(WriterSchema);
		avroData2.put("myData", testArray2);
		// Expected object
		List<ByteBuffer> expTestArray2 = new ArrayList<ByteBuffer>();
		expTestArray2.add(ByteBuffer.wrap(Main.DEFAULT_STRING.getBytes()));
		GenericRecord expAvroData2 = new GenericData.Record(ReaderSchema);
		expAvroData2.put("myData", expTestArray2);
		
		return myTester.testCompatibility(avroData2, WriterSchema, ReaderSchema, expAvroData2);
	}

	private int test3(Schema WriterSchema, Schema ReaderSchema){
		
		// Input object
		List<String> testArray3 = new ArrayList<String>();
		testArray3.add(Main.DEFAULT_STRING);
		GenericRecord avroData3 = new GenericData.Record(WriterSchema);
		avroData3.put("myData", testArray3);
		// Expected object
		GenericRecord expAvroData3 = null;
		
		return myTester.testCompatibility(avroData3, WriterSchema, ReaderSchema, expAvroData3);
	}
	
}
