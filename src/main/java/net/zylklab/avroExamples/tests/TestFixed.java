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

import main.java.net.zylklab.avroExamples.utils.ReportIO;

public class TestFixed extends Tests{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestFixed.class);

	public void testCompatibility(){
		
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
		
		// Keep results stored somewhere
		List<Integer> results = new ArrayList<Integer>();
		
		// Start results reporter
		ReportIO reporter = new ReportIO();
		
		LOGGER.info("Test 1: Fixed types have the same size and unqualified names.");
		// Schema 1
		results.add(test1(schema1, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 2: Fixed types have the same size but different unqualified names.");
		// Schema 1 and 2
		results.add(test2(schema1, schema2)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 31: Fixed types have different size but same unqualified names. The fixed value in the reader schema is longer than in the writer.");
		// Schema 1 and 3
		results.add(test31(schema1, schema3)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 31: Fixed types have different size but same unqualified names. The fixed value in the writer schema is longer than in the reader.");
		// Schema 3 and 1
		results.add(test32(schema3, schema1)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// Return info
		reportSuccesses(results, 1);
		
	}
	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericFixed testValue1 = new Fixed(WriterSchema, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord avroData1 = new GenericData.Record(WriterSchema);
		avroData1.put("myData", testValue1);
		
		// Output object
		GenericFixed expTestValue1 = new Fixed(ReaderSchema, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord expAvroData1 = new GenericData.Record(ReaderSchema);
		expAvroData1.put("myData", expTestValue1);
		
		return myTester.testCompatibility(avroData1, WriterSchema, ReaderSchema, expAvroData1); 
	}
	
	private int test2(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericFixed testValue2 = new Fixed(WriterSchema, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord avroData2 = new GenericData.Record(WriterSchema);
		avroData2.put("myData", testValue2);
		
		// Output object
		GenericRecord expAvroData2 = null;

		return myTester.testCompatibility(avroData2, WriterSchema, ReaderSchema, expAvroData2); 

	}
	
	
	private int test31(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericFixed testValue31 = new Fixed(WriterSchema, ByteBuffer.allocate(4).putInt(1).array());
		GenericRecord avroData31 = new GenericData.Record(WriterSchema);
		avroData31.put("myData", testValue31);
		
		// Output object
		GenericRecord expAvroData31 = null;
		
		return myTester.testCompatibility(avroData31, WriterSchema, ReaderSchema, expAvroData31); 

	}
	
	private int test32(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericFixed testValue32 = new Fixed(WriterSchema, ByteBuffer.allocate(8).putInt(1).array());
		GenericRecord avroData32 = new GenericData.Record(WriterSchema);
		avroData32.put("myData", testValue32);
		
		// Output object
		GenericRecord expAvroData32 = null;
		
		return myTester.testCompatibility(avroData32, WriterSchema, ReaderSchema, expAvroData32); 

	}
	
	
}
