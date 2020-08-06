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

public class TestRecords extends Tests{

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestRecords.class);

	public void testCompatibility(){
		
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
		
		// Keep results stored somewhere
		List<Integer> results = new ArrayList<Integer>();
		
		// Start results reporter
		ReportIO reporter = new ReportIO();
		
		LOGGER.info("Test 1: Both record schemas are the same");
		// Schema 1
		results.add(test1(schema1, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 2: Both record schemas have the same unqualified names and types, but in a different order");
		// Schema 1 and 2
		results.add(test2(schema1, schema2)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 3: Both record schemas have the same field types but different unqualified names");
		// Schema 1 and 3
		results.add(test3(schema1, schema3)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));

		
		LOGGER.info("Test 4: Both record schemas have the same unqualified names but the types are different (yet, you can promote from one to the other)");
		// Schema 1 and 4
		results.add(test4(schema1, schema4)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 5: Both record schemas have the same unqualified names but the types are different (you cannot promote from one to the other)");
		// Schema 4 and 1
		results.add(test5(schema4, schema1)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 6: Writter has a field that's not present in reader");
		// Schema 5 and 1
		results.add(test6(schema5, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 7: Reader has a field that's not present in writer with no default value");
		// Schema 1 and 6
		results.add(test7(schema1, schema6)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 8: Reader has a field that's not present in writer with default value");
		// Schema 1 and 7
		results.add(test8(schema1, schema7)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// Return info
		reportSuccesses(results, 5);
		
		
	}
	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord = new GenericData.Record(WriterSchema);
		avroRecord.put("data_1", 1);
		avroRecord.put("data_2", "Hello");
		
		// Expected output
		GenericRecord expAvroRecord = new GenericData.Record(ReaderSchema);
		expAvroRecord.put("data_1", 1);
		expAvroRecord.put("data_2", new Utf8("Hello"));
		
		return myTester.testCompatibility(avroRecord, WriterSchema, ReaderSchema, expAvroRecord);
		
	}
	
	
	private int test2(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord2 = new GenericData.Record(WriterSchema);
		avroRecord2.put("data_1", 1);
		avroRecord2.put("data_2", "Hello");
		
		// Expected object
		GenericRecord expAvroRecord2 = new GenericData.Record(ReaderSchema);
		expAvroRecord2.put("data_2", new Utf8("Hello"));
		expAvroRecord2.put("data_1", 1);

		
		return myTester.testCompatibility(avroRecord2, WriterSchema, ReaderSchema, expAvroRecord2);
		
	}
	
	
	private int test3(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord3 = new GenericData.Record(WriterSchema);
		avroRecord3.put("data_1", 1);
		avroRecord3.put("data_2", "Hello");
		
		// Expected object
		GenericRecord expAvroRecord3 = null;

		return myTester.testCompatibility(avroRecord3, WriterSchema, ReaderSchema, expAvroRecord3);
		
	}
	
	
	private int test4(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord4 = new GenericData.Record(WriterSchema);
		avroRecord4.put("data_1", 1);
		avroRecord4.put("data_2", "Hello");
		
		// Expected object
		GenericRecord expAvroRecord4 = new GenericData.Record(ReaderSchema);
		expAvroRecord4.put("data_1", (long) 1);
		expAvroRecord4.put("data_2", ByteBuffer.wrap(Main.DEFAULT_STRING.getBytes()));
		
		return myTester.testCompatibility(avroRecord4, WriterSchema, ReaderSchema, expAvroRecord4);
		
	}
	
	
	private int test5(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord5 = new GenericData.Record( WriterSchema);
		avroRecord5.put("data_1", (long) 1);
		avroRecord5.put("data_2", ByteBuffer.wrap("Hello".getBytes()));
		
		// Expected object
		GenericRecord expAvroRecord5 = null;
		
		return myTester.testCompatibility(avroRecord5, WriterSchema, ReaderSchema, expAvroRecord5);
		
	}
	
	
	private int test6(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord6 = new GenericData.Record(WriterSchema);
		avroRecord6.put("data_1", 1);
		avroRecord6.put("data_2", "Hello");
		avroRecord6.put("data_3", "Hello");
		
		// Expected object
		GenericRecord expAvroRecord6 = new GenericData.Record(ReaderSchema);
		expAvroRecord6.put("data_1", 1);
		expAvroRecord6.put("data_2", new Utf8("Hello"));
		
		return myTester.testCompatibility(avroRecord6, WriterSchema, ReaderSchema, expAvroRecord6);
		
	}
	
	
	private int test7(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord7 = new GenericData.Record(WriterSchema);
		avroRecord7.put("data_1", 1);
		avroRecord7.put("data_2", "Hello");
		
		// Expected object
		GenericRecord expAvroRecord7 = null;
		
		
		return myTester.testCompatibility(avroRecord7, WriterSchema, ReaderSchema, expAvroRecord7);
		
	}
	
	
	private int test8(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericRecord avroRecord8 = new GenericData.Record(WriterSchema);
		avroRecord8.put("data_1", 1);
		avroRecord8.put("data_2", "Hello");
		
		// Expected object
		GenericRecord expAvroRecord8 = new GenericData.Record(ReaderSchema);
		expAvroRecord8.put("data_1", 1);
		expAvroRecord8.put("data_2", new Utf8("Hello"));
		expAvroRecord8.put("data_3", new Utf8("string_value"));
		
		return myTester.testCompatibility(avroRecord8, WriterSchema, ReaderSchema, expAvroRecord8);
		
	}
	
	
}
