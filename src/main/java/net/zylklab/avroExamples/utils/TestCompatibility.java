package main.java.net.zylklab.avroExamples.utils;

import java.io.File;
import java.io.IOException;

import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.tests.TestArrays;


public class TestCompatibility {
	/**
	 * Tests whether two versions of an Avro schema are compatible.
	 */
	
	/**
	 * Directory that contains the temporary file where we will write/read our serialized data
	 */
	private String tmpDir = ".";
	/**
	 * Path to the temporary file where we will write/read our serialized data. 
	 */
	private String tmpPath = tmpDir + File.separator +"tmp.avro";
	/**
	 * Logger class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TestCompatibility.class);
	
	
	/**
	 * Tests whether two versions of an Avro schema are compatible by serializing/deserializing a record of data to/from a file.
	 * 
	 * It captures all exceptions and does not propagate them, but will submit a log message to inform the user.
	 * 
	 * @param avroRecord Object we wish to serialize/deserialize.
	 * @param schema1 Schema used to serialize and write the given object.
	 * @param schema2 Schema used to read and deserialize the given object.
	 * @return 1 if both operations were performed successfully. -1 if we could not perform one of the operations due to a schema incompatibility. -2 if we found an IO exception. 
	 */
	public int testCompatibility(GenericRecord avroRecord, Schema schema1, Schema schema2, GenericRecord expAvroRecord){
		
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
			return -2;
		}
		
		
		// Deserialize
		GenericRecord newAvroRecord;
		try {
			newAvroRecord = deserializeFromFile(schema2, file);
		} catch (IOException ex) {
			LOGGER.error("Could not deserialize object from file because of an IO exception. Test aborted.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			removeTmp(tmpPath);
			return -2;		
		} catch (AvroTypeException ex) {
			LOGGER.error("Could not deserialize object from file because of a type exception. Test failed.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			removeTmp(tmpPath);
			return -1;		
		}
		
		// Check that the deserialized Avro record has the exepcted values
		try {
			checkRecordData(newAvroRecord, expAvroRecord);
		} catch (RuntimeException ex) {
			LOGGER.error("The values of the new AvroRecord do not match the expected ones. Test failed.");
			LOGGER.error(ex.toString());
			ex.printStackTrace();
			removeTmp(tmpPath);
			return -1;	
		}
		
		
		// Remove tmp file
		removeTmp(tmpPath);
		
		return 1;
		
	}
	
	/**
	 * Serializes an object and writes it to a given file.
	 * 
	 * @param avroRecord Avro object we wish to serialize.
	 * @param schema Avro writer's schema.
	 * @param file Where to write the serielized data.
	 * @return 1 if operation performed successfully.
	 * @throws IOException
	 * @throws AvroTypeException
	 */
	private int serializeToFile (GenericRecord avroRecord, Schema schema, File file) throws IOException, AvroTypeException {
		
		DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
		
		try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter)) {
			dataFileWriter.create(schema, file);
			dataFileWriter.append(avroRecord);
		} catch (IOException ex) {
			throw new IOException();
		} catch (AvroTypeException ex) {
			throw new AvroTypeException(ex.getMessage());
		}
		
		return 1;
	}
	
	/**
	 * 
	 * Reads an file containing one or more Avro records and deserializes the first one.
	 * 
	 * Even if the given Avro file contains more than one record, it only works with the first one.
	 * 
	 * @param schema Avro reader's schema.
	 * @param file Where to read the serielized data from.
	 * @return Deserialized object.
	 * @throws IOException
	 * @throws AvroTypeException
	 */
	private GenericRecord deserializeFromFile (Schema schema, File file) throws IOException, AvroTypeException {
		
		DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
		GenericRecord record = null;
		
		try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader)) {
			while (dataFileReader.hasNext()) {
				record = dataFileReader.next(record);
				break;
			}
		} catch (IOException ex) {
			throw new IOException();
		} catch (AvroTypeException ex) {
			throw new AvroTypeException(ex.getMessage());
		}
		
		return record;
	}
	
	
	private int checkRecordData(GenericRecord AvroRecord, GenericRecord expectedAvroRecord) throws RuntimeException {

		for (Schema.Field f : AvroRecord.getSchema().getFields()) {
			
			String name = f.name();

			if (equalsWithNulls(AvroRecord.get(name), expectedAvroRecord.get(name))) {
				continue;
			} else {
				throw new RuntimeException("Deserialized values for field " + name + " did not match the expected values." );
			}
			
		}
		
		return 1;
		
	}
	
	private boolean equalsWithNulls(Object a, Object b) {
	    if (a==b) return true;
	    if ((a==null)||(b==null)) return false;
	    return a.equals(b);
	 }
	
	
	/**
	 * Creates a directory structure needed to allocate the tmp file.
	 * 
	 * @param path Path to the tmp file we wisht to create.
	 */
	private void createTmp (String path) {
		new File(path).mkdirs();
	}
	
	/**
	 * Removes a tmp file.
	 * 
	 * @param path Path to the tmp file.
	 */
	private void removeTmp (String path) {
		new File(path).delete();
	}
	
	
	
}
