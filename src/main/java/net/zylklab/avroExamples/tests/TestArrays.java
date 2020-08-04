package main.java.net.zylklab.avroExamples.tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.Main;
import main.java.net.zylklab.avroExamples.utils.ManageTMP;


public class TestArrays {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);
	
	private static final String tmpDir = ".";
	private static final String tmpPath = tmpDir + File.separator +"tmp.avro";

	private <T> void testBackwardComp(List<T> myArray, Schema schema1, Schema schema2){
		// Create object
		GenericRecord avroArray = new GenericData.Record(schema1);
		avroArray.put("myArray", myArray);
		
		// Serialize
		File file = new File(tmpPath);
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema1);
		DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
		try {
			dataFileWriter.create(schema1, file);
			dataFileWriter.append(avroArray);
			dataFileWriter.close();
		} catch (IOException ex) {
			// TODO 
		}
		
		
		// Deserialize
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema2);
		GenericRecord record = null;
		try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader)) {
			while (dataFileReader.hasNext()) {
				record = dataFileReader.next(record);
			}
		} catch (IOException ex) {
			// TODO
		}
		
		return;
		
	}
	
	
	private <T> void testForwardComp(List<T> myArray, Schema schema1, Schema schema2){
		
		
		
	}
	
	public void testCompatibility(){
				
		// Create tmp file
		ManageTMP.createTmp(tmpDir);
		
		// Read all schemas
		Schema schema1, schema2, schema3, schema4;
		try {
			schema1 = new Schema.Parser().parse(new File("src/main/avro/arrays/arrays1.avsc"));
			//schema2 = new Schema.Parser().parse(new File("user.avsc"));
			//schema3 = new Schema.Parser().parse(new File("user.avsc"));
			//schema4 = new Schema.Parser().parse(new File("user.avsc"));
		} catch (IOException ex) {
			LOGGER.error(ex.toString());
			return;			
		}
		
		LOGGER.info("Test 1: Arrays have same item types");
		List<String> testArray1 = new ArrayList();
		testArray1.add("Hello");
		testBackwardComp(testArray1, schema1, schema1);
		testForwardComp(testArray1, schema1, schema1);
		
		LOGGER.info("Test 2: Arrays have different item types, but you can promote from one to the other");
		String testArray2[] = new String[10];
		// TODO
		
		LOGGER.info("Test 3: Arrays have different item types, and you cannot promote from one to the other");
		String testArray3[] = new String[10];
		// TODO
		
		// Remove tmp file
		ManageTMP.removeTmp(tmpDir);
		
		// Return info
		// TODO
		
	}
	
}
