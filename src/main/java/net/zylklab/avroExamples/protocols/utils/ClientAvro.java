package net.zylklab.avroExamples.protocols.utils;

import org.apache.avro.ipc.Transceiver;

import net.zylklab.avroExamples.generated.MyTests;
import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;

/**
 * 
 */
public abstract class ClientAvro {
	
	public Transceiver client;
	protected MyTests proxy; 
	    
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
    
    public void sendOneWayRecordMsg_test_1(Record_1 message){      
    	proxy.send_records_oneway_test_1(message);
        return;
    }
    
    public void sendOneWayRecordMsg_test_2(Record_2 message){      
    	proxy.send_records_oneway_test_2(message);
        return;
    }
    
    public void sendOneWayRecordMsg_test_3(Record_1 message){      
    	proxy.send_records_oneway_test_3(message);
        return;
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
    	CharSequence response = proxy.send_primitive_test_4(message);
        return response.toString();
    }
    
    public String sendPrimitiveMsg_test_5(String message){      
    	CharSequence response = (String) proxy.send_primitive_test_5(message);
        return response.toString();
    }
    
    public void sendOneWayPrimitiveMsg_test_1(String message){      
    	proxy.send_primitive_oneway_test_1(message);
        return;
    }
    
    public void sendOneWayPrimitiveMsg_test_2(){      
    	proxy.send_primitive_oneway_test_2(null);
        return;
    }
    
    public void sendOneWayPrimitiveMsg_test_3(String message){      
    	proxy.send_primitive_oneway_test_3(message);
        return;
    }
    
	public Record_1 sendExpensiveOperation(Record_1 msg) {
		Record_1 response = (Record_1) proxy.expensive_operation(msg);
		return response;
	}
    
    public abstract void closeClient();
    
}
