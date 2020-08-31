package net.zylklab.avroExamples.protocols.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.MyTests;

/**
 * Default client for a Jetty server.
 */
public class ClientAvroHTTP extends ClientAvro {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientAvroHTTP.class);
	

	public ClientAvroHTTP(String host, int port){
		try {
			// Initialize variables
			client = new HttpTransceiver(new URL("http://127.0.0.1:"+port+"/"));     
			proxy = (MyTests) SpecificRequestor.getClient(MyTests.class, client);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getStackTrace().toString());
		}
	}
	
	@Override
    public void closeClient(){
    	try {
			client.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getStackTrace().toString());
		}
    }
    
}
