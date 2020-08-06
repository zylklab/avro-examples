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

import main.java.net.zylklab.avroExamples.utils.TestCompatibility;

public class TestEnums {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);

	public void testArrayCompatibility(){
		
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
		
		
		// Instantiate the class that checks for compatibility
		TestCompatibility myTester = new TestCompatibility();
		
		// Keep results stored somewhere
		List<Integer> results = new ArrayList<Integer>();
		
		LOGGER.info("Test 1: Enumerations have same size and unqualified names in the same order");
		// Schema 1
		GenericEnumSymbol<EnumSymbol> testValue1 = new EnumSymbol(schema1, "OPTION_1");
		GenericRecord avroData1 = new GenericData.Record(schema1);
		avroData1.put("myData", testValue1);
		results.add(myTester.testCompatibility(avroData1, schema1, schema1));	// Pass
		
		
		LOGGER.info("Test 2: Enumerations have same size and unqualified names in different order");
		// Schema 1 and 2
		GenericEnumSymbol<EnumSymbol> testValue2 = new EnumSymbol(schema1, "OPTION_2");
		GenericRecord avroData2 = new GenericData.Record(schema1);
		avroData2.put("myData", testValue2);
		results.add(myTester.testCompatibility(avroData2, schema1, schema2)); // Pass
		
		
		LOGGER.info("Test 3: Enumerations have same size, different unqualified names (name sets intersect) but no default values. Object value is present in both name sets");
		// Schema 1 and 3
		GenericEnumSymbol<EnumSymbol> testValue3 = new EnumSymbol(schema1, "OPTION_1");
		GenericRecord avroData3 = new GenericData.Record(schema1);
		avroData3.put("myData", testValue3);
		results.add(myTester.testCompatibility(avroData3, schema1, schema3)); // Pass

		
		LOGGER.info("Test 4: Enumerations have same size, different unqualified names (name sets intersect) but no default values. Object value is only present in one name set");
		// Schema 1 and 3
		GenericEnumSymbol<EnumSymbol> testValue4 = new EnumSymbol(schema1, "OPTION_3");
		GenericRecord avroData4 = new GenericData.Record(schema1);
		avroData4.put("myData", testValue4);
		results.add(myTester.testCompatibility(avroData4, schema1, schema3)); // Fail

		
		LOGGER.info("Test 5: Enumerations have same size, different unqualified names (name sets intersect) and default values. Object value is only present in one name set and default value is present in both name sets");
		// schema 4 and schema 5
		GenericEnumSymbol<EnumSymbol> testValue5 = new EnumSymbol(schema4, "OPTION_3");
		GenericRecord avroData5 = new GenericData.Record(schema4);
		avroData5.put("myData", testValue5);
		results.add(myTester.testCompatibility(avroData5, schema4, schema5)); // Pass
		
		LOGGER.info("Test 6: Enumerations have same size, different unqualified names (name sets intersect) and default values. Object value is only present in one name set and default value is only present in one name set");
		// schema 6 and schema 7
		GenericEnumSymbol<EnumSymbol> testValue6 = new EnumSymbol(schema6, "OPTION_3");
		GenericRecord avroData6 = new GenericData.Record(schema6);
		avroData6.put("myData", testValue6);
		results.add(myTester.testCompatibility(avroData6, schema6, schema7)); // Pass	
		
		
		LOGGER.info("Test 7: Enumerations have different sizes, one name set is a subset of the other and there are default values. Object value is only present in one name set");
		// schema 6 and schema 8
		GenericEnumSymbol<EnumSymbol> testValue71 = new EnumSymbol(schema6, "OPTION_3");
		GenericRecord avroData71 = new GenericData.Record(schema6);
		avroData71.put("myData", testValue71);
		results.add(myTester.testCompatibility(avroData71, schema6, schema8)); // Pass
		
		GenericEnumSymbol<EnumSymbol> testValue72 = new EnumSymbol(schema8, "OPTION_4");
		GenericRecord avroData72 = new GenericData.Record(schema8);
		avroData72.put("myData", testValue72);
		results.add(myTester.testCompatibility(avroData72, schema8, schema6)); // Pass		
		
		
		LOGGER.info("Test 8: Enumerations have different sizes, one name set is a subset of the other but there are no default values. Object value is present in both name sets");
		// schema 1 and schema 9
		GenericEnumSymbol<EnumSymbol> testValue81 = new EnumSymbol(schema1, "OPTION_1");
		GenericRecord avroData81 = new GenericData.Record(schema1);
		avroData81.put("myData", testValue81);
		results.add(myTester.testCompatibility(avroData81, schema1, schema9));	// Pass
		
		GenericEnumSymbol<EnumSymbol> testValue82 = new EnumSymbol(schema9, "OPTION_1");
		GenericRecord avroData82 = new GenericData.Record(schema9);
		avroData82.put("myData", testValue82);
		results.add(myTester.testCompatibility(avroData82, schema9, schema1));	// Pass
		
		LOGGER.info("Test 9: Enumerations have different sizes, one name set is a subset of the other but there are no default values. Object value is only present in one name set");
		// schema 1 and schema 9
		GenericEnumSymbol<EnumSymbol> testValue9 = new EnumSymbol(schema9, "OPTION_4");
		GenericRecord avroData9 = new GenericData.Record(schema9);
		avroData9.put("myData", testValue9);
		results.add(myTester.testCompatibility(avroData9, schema9, schema1));	// Fail
		
		
		// Return info
		// // Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + 9 + " tests to run successfully. Got " + count);		// TODO Hardcoded number here
		
	}
	
	
	
}
