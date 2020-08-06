package main.java.net.zylklab.avroExamples.utils;

import java.io.File;
import java.io.IOException;

import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.tests.TestArrays;

public class TestCompatibility {
	
	private final String tmpDir = ".";
	private final String tmpPath = tmpDir + File.separator +"tmp.avro";

	private static final Logger LOGGER = LoggerFactory.getLogger(TestArrays.class);
	
	public int testCompatibility(GenericRecord avroRecord, Schema schema1, Schema schema2){
		
		// Create tmp file
		createTmp(tmpDir);
		File file = new File(tmpPath);

		
		// Serialize
		try {
			serializeToFile(avroRecord, schema1, file);
		} catch (IOException ex) {
			LOGGER.error("Could not serialize object to file because of an IO exception. Test aborted.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			removeTmp(tmpPath);
			return -1;
		}
		
		
		// Deserialize
		try {
			deserializeFromFile(schema2, file);
		} catch (IOException ex) {
			LOGGER.error("Could not deserialize object from file because of an IO exception. Test aborted.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			removeTmp(tmpPath);
			return -1;		
		} catch (AvroTypeException ex) {
			LOGGER.error("Could not deserialize object from file because of a type exception. Test failed.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			removeTmp(tmpPath);
			return -2;		
		}
		
		
		// Remove tmp file
		removeTmp(tmpPath);
		
		return 1;
		
	}
	
	private int serializeToFile (GenericRecord avroRecord, Schema schema, File file) throws IOException {
		

		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		
		try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter)) {
			dataFileWriter.create(schema, file);
			dataFileWriter.append(avroRecord);
		} catch (IOException ex) {
			throw new IOException();
		}
		
		return 1;
	}
	
	private int deserializeFromFile (Schema schema, File file) throws IOException, AvroTypeException {
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		GenericRecord record = null;
		try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader)) {
			while (dataFileReader.hasNext()) {
				record = dataFileReader.next(record);
			}
		} catch (IOException ex) {
			throw new IOException();
		} catch (AvroTypeException ex) {
			throw new AvroTypeException(ex.getMessage());
		}
		
		return 1;
	}
	
	
	private void createTmp (String path) {
		new File(path).mkdirs();
	}
	
	private void removeTmp (String path) {
		new File(path).delete();
	}
	
	
	
}
