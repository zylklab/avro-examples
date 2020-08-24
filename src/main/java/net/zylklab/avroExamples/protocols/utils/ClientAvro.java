package net.zylklab.avroExamples.protocols.utils;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.util.Utf8;

import net.zylklab.avroExamples.generated.MyTests;
import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;

/**
 * 
 */
public abstract class ClientAvro {
	
	protected HttpTransceiver client;
	protected MyTests proxy; 
	    
    public abstract Record_1 sendRecordMsg_test_1(Record_1 message);
    
    public abstract Record_2 sendRecordMsg_test_2(Record_2 message);
    
    public abstract Record_1 sendRecordMsg_test_3(Record_1 message);
    
    public abstract Record_1 sendRecordMsg_test_4(Record_1 message);
    
    public abstract String sendPrimitiveMsg_test_1(String message);
    
    public abstract String sendPrimitiveMsg_test_2();
    
    public abstract void sendPrimitiveMsg_test_3(String message);
    
    public abstract String sendPrimitiveMsg_test_4(String message);
    
    public abstract String sendPrimitiveMsg_test_5(String message);
    
    public abstract void closeClient();
    
}
