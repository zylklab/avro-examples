package net.zylklab.avroExamples.protocols.utils;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.ipc.netty.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.MyTests;

/**
 * Default client for a Netty server.
 */
public class ClientAvroRPC extends ClientAvro{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientAvroHTTP.class);
	
	public ClientAvroRPC(String host, int port){
		try {
			// Initialize variables
	        client = new NettyTransceiver(new InetSocketAddress(host, port));
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
