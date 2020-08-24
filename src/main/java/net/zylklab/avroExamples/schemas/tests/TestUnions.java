package net.zylklab.avroExamples.schemas.tests;

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

import net.zylklab.avroExamples.schemas.Main;
import net.zylklab.avroExamples.schemas.utils.ReportIO;

public class TestUnions extends Tests{

	private static final Logger LOGGER = LoggerFactory.getLogger(TestUnions.class);

	public void testCompatibility() {

		// Read all schemas
		Schema schema1, schema2, schema3, schema4, schema5, schema6, schema7, schema8, schemaPrim1, schemaPrim2, schemaPrim3;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions3.avsc"));
			schema4 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions4.avsc"));
			schema5 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions5.avsc"));
			schema6 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions6.avsc"));
			schema7 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions7.avsc"));
			schema8 = new Schema.Parser().parse(new File("src/main/avro/schemas/unions/unions8.avsc"));

			schemaPrim1 = new Schema.Parser().parse(new File("src/main/avro/schemas/primitives/primitives1.avsc"));
			schemaPrim2 = new Schema.Parser().parse(new File("src/main/avro/schemas/primitives/primitives2.avsc"));
			schemaPrim3 = new Schema.Parser().parse(new File("src/main/avro/schemas/primitives/primitives3.avsc"));
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

		LOGGER.info("Test 1: Union types are the same");
		// Schema 1
		results.add(test1(schema1, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 2: Union types are not the same but can be promoted");
		// Schema 1 and schema 2
		results.add(test2(schema1, schema2)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// TODO Missing a test where union types are not the same and cannot be promoted?

		LOGGER.info(
				"Test 3: Union types of both schemas intersect, but one does not contain the other. There is no default values and object type is present in both unions.");
		// Schema 1 and schema 3
		results.add(test3(schema1, schema3)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info(
				"Test 4: Union types of both schemas intersect, but one does not contain the other. There is no default values and the object type is not present in the reader's union schema.");
		// Schema 1 and schema 3
		results.add(test4(schema1, schema3)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));

		LOGGER.info(
				"Test 5: Union types of both schemas intersect, but one does not contain the other. There are default values and the object type is not present in the reader's union schema.");
		// Schema 4 and schema 5
		results.add(test51(schema4, schema5)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		results.add(test52(schema5, schema4)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info(
				"Test 6: One schema has an union while the other has a primitive type that matches the fist element of the union. We test this both ways.");
		// Union schema 1 and primitive schema 1
		results.add(test61(schema1, schemaPrim1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		results.add(test62(schemaPrim1, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info(
				"Test 7.1: One schema has an union while the other has a primitive type does not match the fist element of the union (But it is still part of the union). Test from union to primitive.");
		// Union schema 1 and primitive schema 2
		results.add(test71(schema1, schemaPrim2)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		
		
		LOGGER.info(
				"Test 7.2: One schema has an union while the other has a primitive type does not match the fist element of the union (But it is still part of the union). Test from primitive to union.");
		// Union schema 1 and primitive schema 2
		
		results.add(test72(schemaPrim2, schema1)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info(
				"Test 8: One schema has an union while the other has a primitive type that matches a promotion of the fist element of the union. Test from union to primitive.");
		// Union schema 6 and primitive schema 3
		results.add(test81(schema6, schemaPrim3)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		
		LOGGER.info(
				"Test 8: One schema has an union while the other has a primitive type that matches a promotion of the fist element of the union. Test from primitive to union.");
		// Union schema 6 and primitive schema 3
		results.add(test82(schemaPrim3, schema6)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// LOGGER.info(
		//		"Test 9: One schema has an union while the other has a primitive type that does not match the fist element of the union (or a promotion).");
		// Union schema 6 and primitive schema 3
		// TODO Completely unnecessary, right?
		
		//
		// COMPLEX TYPES
		//
		
		LOGGER.info("Test 9: Union types are the same and at least one of them is a complex type");
		// Schema 7
		results.add(test9 (schema7, schema7)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));		

		LOGGER.info("Test 10: Union types are not the same but can be promoted. At least one of them is a complex type");
		// Schema 7 and 8
		results.add(test10(schema7, schema8)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// TODO Keep doing tests with complex types? Feels a bit unnecessary.
		
		// Return info
		reportSuccesses(results, 10);

	}

	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData1 = new GenericData.Record(WriterSchema);
		avroData1.put("myData", 1);
		
		// Expected object
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
		GenericRecord avroData3 = new GenericData.Record(WriterSchema);
		avroData3.put("myData", 1);
		
		// Expected output
		GenericRecord expAvroData3 = new GenericData.Record(ReaderSchema);
		expAvroData3.put("myData", 1);
		
		return myTester.testCompatibility(avroData3, WriterSchema, ReaderSchema, expAvroData3);

	}
	
	private int test4(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		String testValue4 = "Hello";
		GenericRecord avroData4 = new GenericData.Record(WriterSchema);
		avroData4.put("myData", testValue4);
		
		// Expected output
		GenericRecord expAvroData4 = null;
		
		return myTester.testCompatibility(avroData4, WriterSchema, ReaderSchema, expAvroData4);

	}
	
	private int test51(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData51 = new GenericData.Record(WriterSchema);
		avroData51.put("myData", Main.DEFAULT_STRING);
		
		// Output object
		GenericRecord expAvroData51 = null;
		
		return myTester.testCompatibility(avroData51, WriterSchema, ReaderSchema, expAvroData51);

	}
	
	private int test52(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData52 = new GenericData.Record(WriterSchema);
		avroData52.put("myData", null);
		
		// Output object
		GenericRecord expAvroData52 = null;
		
		return myTester.testCompatibility(avroData52, WriterSchema, ReaderSchema, expAvroData52);

	}
	
	private int test61(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData61 = new GenericData.Record(WriterSchema);
		avroData61.put("myData", 1);
		
		// Expected object
		GenericRecord expAvroData61 = new GenericData.Record(ReaderSchema);
		expAvroData61.put("myData", 1);
		
		return myTester.testCompatibility(avroData61, WriterSchema, ReaderSchema, expAvroData61);

	}
	
	private int test62(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData62 = new GenericData.Record(WriterSchema);
		avroData62.put("myData", 1);
		
		// Expected object
		GenericRecord expAvroData62 = new GenericData.Record(ReaderSchema);
		expAvroData62.put("myData", 1);
		
		return myTester.testCompatibility(avroData62, WriterSchema, ReaderSchema, expAvroData62);

	}
	
	private int test71(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData7 = new GenericData.Record(WriterSchema);
		avroData7.put("myData", 1);
		
		// Expected output
		GenericRecord expAvroData7 = null;
		
		return myTester.testCompatibility(avroData7, WriterSchema, ReaderSchema, expAvroData7);

	}
	
	private int test72(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData72 = new GenericData.Record(WriterSchema);
		avroData72.put("myData", Main.DEFAULT_STRING);

		// Expected output
		GenericRecord expAvroData72 = new GenericData.Record(ReaderSchema);
		expAvroData72.put("myData", new Utf8(Main.DEFAULT_STRING));
		
		return myTester.testCompatibility(avroData72, WriterSchema, ReaderSchema, expAvroData72);

	}
	
	
	private int test81(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData81 = new GenericData.Record(WriterSchema);
		avroData81.put("myData", Main.DEFAULT_STRING);
		
		// Expected output
		GenericRecord expAvroData81 = new GenericData.Record(ReaderSchema);
		expAvroData81.put("myData", ByteBuffer.wrap(Main.DEFAULT_STRING.getBytes()));
		
		return myTester.testCompatibility(avroData81, WriterSchema, ReaderSchema, expAvroData81);

	}
	
	private int test82(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		GenericRecord avroData82 = new GenericData.Record(WriterSchema);
		avroData82.put("myData", ByteBuffer.wrap(Main.DEFAULT_STRING.getBytes()));
		
		// Expected output
		GenericRecord expAvroData82 = new GenericData.Record(ReaderSchema);
		expAvroData82.put("myData", new Utf8(Main.DEFAULT_STRING));
		
		return myTester.testCompatibility(avroData82, WriterSchema, ReaderSchema, expAvroData82);

	}
	
	private int test9(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		List<String> testArray1 = new ArrayList<String>();
		testArray1.add(Main.DEFAULT_STRING);
		GenericRecord avroData9 = new GenericData.Record(WriterSchema);
		avroData9.put("myData", testArray1);
		
		
		// Expected output
		List<Utf8> expTestArray1 = new ArrayList<Utf8>();
		expTestArray1.add(new Utf8(Main.DEFAULT_STRING));
		GenericRecord expAvroData9 = new GenericData.Record(ReaderSchema);
		expAvroData9.put("myData", new GenericData.Array<Utf8>(ReaderSchema.getField("myData").schema().getTypes().get(1), expTestArray1));
		
		return myTester.testCompatibility(avroData9, WriterSchema, ReaderSchema, expAvroData9);

	}
	
	private int test10(Schema WriterSchema, Schema ReaderSchema){
		// Input object 
		List<String> testArray21 = new ArrayList<String>();
		testArray21.add("Hello");
		GenericRecord avroData10 = new GenericData.Record(WriterSchema);
		avroData10.put("myData", testArray21);
		
		// Expected output
		List<ByteBuffer> expTestArray21 = new ArrayList<ByteBuffer>();
		expTestArray21.add(ByteBuffer.wrap(Main.DEFAULT_STRING.getBytes()));
		GenericRecord expAvroData10 = new GenericData.Record(ReaderSchema);
		expAvroData10.put("myData", expTestArray21);
		
		return myTester.testCompatibility(avroData10, WriterSchema, ReaderSchema, expAvroData10);

	}
	
	
}
