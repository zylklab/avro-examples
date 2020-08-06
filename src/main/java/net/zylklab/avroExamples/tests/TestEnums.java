package main.java.net.zylklab.avroExamples.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData.EnumSymbol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericEnumSymbol;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.utils.ReportIO;

public class TestEnums extends Tests{

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestEnums.class);

	public void testCompatibility(){
		
		// Read all schemas
		Schema schema1, schema2, schema3, schema4, schema5, schema6, schema7, schema8, schema9;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/enums/enums1.avsc"));
			schema2 = new Schema.Parser().parse(new File("src/main/avro/enums/enums2.avsc"));
			schema3 = new Schema.Parser().parse(new File("src/main/avro/enums/enums3.avsc"));
			schema4 = new Schema.Parser().parse(new File("src/main/avro/enums/enums4.avsc"));
			schema5 = new Schema.Parser().parse(new File("src/main/avro/enums/enums5.avsc"));
			schema6 = new Schema.Parser().parse(new File("src/main/avro/enums/enums6.avsc"));
			schema7 = new Schema.Parser().parse(new File("src/main/avro/enums/enums7.avsc"));
			schema8 = new Schema.Parser().parse(new File("src/main/avro/enums/enums8.avsc"));
			schema9 = new Schema.Parser().parse(new File("src/main/avro/enums/enums9.avsc"));

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
		
		LOGGER.info("Test 1: Enumerations have same size and unqualified names in the same order");
		// Schema 1
		results.add(test1(schema1, schema1));	// Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 2: Enumerations have same size and unqualified names in different order");
		// Schema 1 and 2
		results.add(test2(schema1, schema2)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 3: Enumerations have same size, different unqualified names (name sets intersect) but no default values. Object value is present in both name sets");
		// Schema 1 and 3
		results.add(test3(schema1, schema3)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 4: Enumerations have same size, different unqualified names (name sets intersect) but no default values. Object value is only present in one name set");
		// Schema 1 and 3
		results.add(test4(schema1, schema3)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 5: Enumerations have same size, different unqualified names (name sets intersect) and default values. Object value is only present in the reader's name set and the default value is present in both name sets");
		// schema 4 and schema 5
		results.add(test5(schema4, schema5)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 6: Enumerations have same size, different unqualified names (name sets intersect) and default values. Object value is only present in the reader's name set and default value is only present in the writer's name set");
		// schema 6 and schema 7
		results.add(test6(schema6, schema7)); // Pass	
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 7: Enumerations have different sizes, one name set is a subset of the other and there are default values. Object value is present in both name sets.");
		// schema 6 and schema 8
		results.add(test7(schema6, schema8)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test 8: Enumerations have different sizes, one name set is a subset of the other and there are default values. Object value is only present in the writer's name set.");
		// schema 6 and schema 8
		results.add(test8(schema8, schema6)); // Pass		
		reporter.reportResults(results.get(results.size() - 1 ));
		
		
		LOGGER.info("Test 9: Enumerations have different sizes, one name set is a subset of the other but there are no default values. Object value is only present in the writer's name set");
		// schema 1 and schema 9
		results.add(test9(schema9, schema1));	// Fail
		reporter.reportResults(results.get(results.size() - 1 ));
		
		// Return info
		reportSuccesses(results, 7);

		
	}
	
	private int test1(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue1 = new EnumSymbol(WriterSchema, "OPTION_1");
		GenericRecord avroData1 = new GenericData.Record(WriterSchema);
		avroData1.put("myData", testValue1);
		
		// Expected object
		GenericEnumSymbol<EnumSymbol> expTestValue1 = new EnumSymbol(ReaderSchema, "OPTION_1");
		GenericRecord expAvroData1 = new GenericData.Record(ReaderSchema);
		expAvroData1.put("myData", expTestValue1);
		
		return myTester.testCompatibility(avroData1, WriterSchema, ReaderSchema, expAvroData1);
	}
	
	
	private int test2(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue2 = new EnumSymbol(WriterSchema, "OPTION_2");
		GenericRecord avroData2 = new GenericData.Record(WriterSchema);
		avroData2.put("myData", testValue2);
		
		// Expected object
		GenericEnumSymbol<EnumSymbol> expTestValue2 = new EnumSymbol(ReaderSchema, "OPTION_2");
		GenericRecord expAvroData2 = new GenericData.Record(ReaderSchema);
		expAvroData2.put("myData", expTestValue2);
		
		return myTester.testCompatibility(avroData2, WriterSchema, ReaderSchema, expAvroData2); 
	}
	
	
	private int test3(Schema WriterSchema, Schema ReaderSchema){
		//Input objects
		GenericEnumSymbol<EnumSymbol> testValue3 = new EnumSymbol(WriterSchema, "OPTION_1");
		GenericRecord avroData3 = new GenericData.Record(WriterSchema);
		avroData3.put("myData", testValue3);
		
		// Expected object
		GenericEnumSymbol<EnumSymbol> expTestValue3 = new EnumSymbol(ReaderSchema, "OPTION_1");
		GenericRecord expAvroData3 = new GenericData.Record(ReaderSchema);
		expAvroData3.put("myData", expTestValue3);
		
		return myTester.testCompatibility(avroData3, WriterSchema, ReaderSchema, expAvroData3);
	}
	
	
	private int test4(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue4 = new EnumSymbol(WriterSchema, "OPTION_3");
		GenericRecord avroData4 = new GenericData.Record(WriterSchema);
		avroData4.put("myData", testValue4);
		
		// Expected object
		GenericRecord expAvroData4 = null;
		
		return myTester.testCompatibility(avroData4, WriterSchema, ReaderSchema, expAvroData4); // Fail	
	}
	
	
	private int test5(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue5 = new EnumSymbol(WriterSchema, "OPTION_3");
		GenericRecord avroData5 = new GenericData.Record(WriterSchema);
		avroData5.put("myData", testValue5);
		
		// Expected object
		GenericEnumSymbol<EnumSymbol> expTestValue5 = new EnumSymbol(ReaderSchema, "OPTION_1");
		GenericRecord expAvroData5 = new GenericData.Record(ReaderSchema);
		expAvroData5.put("myData", expTestValue5);
		
		return myTester.testCompatibility(avroData5, WriterSchema, ReaderSchema, expAvroData5);
	}
	
	
	private int test6(Schema WriterSchema, Schema ReaderSchema){
		//Input object
		GenericEnumSymbol<EnumSymbol> testValue6 = new EnumSymbol(WriterSchema, "OPTION_3");
		GenericRecord avroData6 = new GenericData.Record(WriterSchema);
		avroData6.put("myData", testValue6);
		
		// Expected object
		GenericEnumSymbol<EnumSymbol> expTestValue6 = new EnumSymbol(ReaderSchema, "OPTION_4");
		GenericRecord expAvroData6 = new GenericData.Record(ReaderSchema);
		expAvroData6.put("myData", expTestValue6);
		
		return myTester.testCompatibility(avroData6, WriterSchema, ReaderSchema, expAvroData6);
	}
	
	
	private int test7(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue7 = new EnumSymbol(WriterSchema, "OPTION_3");
		GenericRecord avroData7 = new GenericData.Record(WriterSchema);
		avroData7.put("myData", testValue7);
		
		// Output object
		GenericEnumSymbol<EnumSymbol> expTestValue7 = new EnumSymbol(ReaderSchema, "OPTION_3");
		GenericRecord expAvroData7 = new GenericData.Record(ReaderSchema);
		expAvroData7.put("myData", expTestValue7);
		
		return myTester.testCompatibility(avroData7, WriterSchema, ReaderSchema, expAvroData7);

	}
	
	private int test8(Schema WriterSchema, Schema ReaderSchema){
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue8 = new EnumSymbol(WriterSchema, "OPTION_4");
		GenericRecord avroData8 = new GenericData.Record(WriterSchema);
		avroData8.put("myData", testValue8);
		
		// Output object
		GenericEnumSymbol<EnumSymbol> expTestValue8 = new EnumSymbol(WriterSchema, "OPTION_3");
		GenericRecord expAvroData8 = new GenericData.Record(WriterSchema);
		expAvroData8.put("myData", expTestValue8);
		
		
		return myTester.testCompatibility(avroData8, WriterSchema, ReaderSchema, expAvroData8);

	}
	
	private int test9(Schema WriterSchema, Schema ReaderSchema){
		
		// Input object
		GenericEnumSymbol<EnumSymbol> testValue9 = new EnumSymbol(WriterSchema, "OPTION_4");
		GenericRecord avroData9 = new GenericData.Record(WriterSchema);
		avroData9.put("myData", testValue9);
		
		// Output object
		GenericRecord expAvroData9 = null;

		
		return myTester.testCompatibility(avroData9, WriterSchema, ReaderSchema, expAvroData9);

	}
	

	
	
}
