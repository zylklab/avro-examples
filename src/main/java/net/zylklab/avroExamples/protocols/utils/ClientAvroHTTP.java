package net.zylklab.avroExamples.protocols.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.MyTests;
import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;

/**
 * 
 */
public class ClientAvroHTTP extends ClientAvro {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientAvroHTTP.class);
	
	protected HttpTransceiver client;
	protected MyTests proxy; 
	
	public ClientAvroHTTP(String host, int port){
		try {
			client = new HttpTransceiver(new URL("http://127.0.0.1:"+port+"/"));     
			proxy = (MyTests) SpecificRequestor.getClient(MyTests.class, client);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getStackTrace().toString());
		}
	}
    
    public Record_1 sendRecordMsg_test_1(Record_1 message){      
    	Record_1 response = (Record_1) proxy.send_records_test_1(message);
        return response;
    }
    
    public Record_2 sendRecordMsg_test_2(Record_2 message){      
    	Record_2 response = (Record_2) proxy.send_records_test_2(message);
        return response;
    }
    
    public Record_1 sendRecordMsg_test_3(Record_1 message){      
    	Record_1 response = (Record_1) proxy.send_records_test_3(message);
        return response;
    }
    
    public Record_1 sendRecordMsg_test_4(Record_1 message){      
    	Record_1 response = (Record_1) proxy.send_records_test_4(message);
        return response;
    }
    
    public String sendPrimitiveMsg_test_1(String message){      
    	CharSequence response = proxy.send_primitive_test_1(message);
        return response.toString();
    }
    
    public String sendPrimitiveMsg_test_2(){      
    	CharSequence response = proxy.send_primitive_test_2(null);
        return response.toString();
    }
    
    public void sendPrimitiveMsg_test_3(String message){      
    	proxy.send_primitive_test_3(message);
        return;
    }
    
    public String sendPrimitiveMsg_test_4(String message){      
    	String response = (String) proxy.send_primitive_test_4(message);
        return response;
    }
    
    public String sendPrimitiveMsg_test_5(String message){      
    	String response = (String) proxy.send_primitive_test_5(message);
        return response;
    }
    
    
    public void closeClient(){
    	try {
			client.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getStackTrace().toString());
		}
    }
    
}
