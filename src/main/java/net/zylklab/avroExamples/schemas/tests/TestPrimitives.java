package net.zylklab.avroExamples.schemas.tests;

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

import net.zylklab.avroExamples.schemas.Main;
import net.zylklab.avroExamples.schemas.utils.ReportIO;

public class TestPrimitives extends Tests{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestPrimitives.class);

	public void testCompatibility(){
		
		// Read all schemas
		Schema schema1, schema2, schema3;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/schemas/primitives/primitives1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/schemas/primitives/primitives2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/schemas/primitives/primitives3.avsc"));

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
		
		LOGGER.info("Test 1: Primitive values are the same");
		// Schema 1
		results.add(test1(schema1, schema1)); // Pass
		
		
		LOGGER.info("Test 2: Primitive values are not the same but can be promoted");
		// Schema 2 and 3
		results.add(test2(schema2, schema3)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 3: Primitive values are not the same and cannot be promoted");
		// Schema 1 and 2
		results.add(test3(schema1, schema2)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		
		// Return info
		reportSuccesses(results, 2);
		
		
	}
	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroData1 = new GenericData.Record(WriterSchema);
		avroData1.put("myData", 1);
		
		//Output object
		GenericRecord expAvroData1 = new GenericData.Record(ReaderSchema);
		expAvroData1.put("myData", 1);
		
		return myTester.testCompatibility(avroData1, WriterSchema, ReaderSchema, expAvroData1); 

	}
	
	private int test2(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroData2 = new GenericData.Record(WriterSchema);
		avroData2.put("myData", Main.DEFAULT_STRING);
		
		// Expected object
		GenericRecord expAvroData2 = new GenericData.Record(ReaderSchema);
		expAvroData2.put("myData", ByteBuffer.wrap(Main.DEFAULT_STRING.getBytes()));
		
		return myTester.testCompatibility(avroData2, WriterSchema, ReaderSchema, expAvroData2); 

	}
	
	
	private int test3(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		int testValue3 = 1;
		GenericRecord avroData3 = new GenericData.Record(WriterSchema);
		avroData3.put("myData", testValue3);
		
		// Expected object
		GenericRecord expAvroData3 = null;
		
		return myTester.testCompatibility(avroData3, WriterSchema, ReaderSchema, expAvroData3); 

	}
	
}
