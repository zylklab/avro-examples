package net.zylklab.avroExamples.protocols.utils;

import net.zylklab.avroExamples.generated.MyTests;
import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;
import net.zylklab.avroExamples.protocols.Main;

public class MyTestsImpl implements MyTests {

	@Override
	public Object send_records_test_1(Record_1 req_message) {
		// Echo message
		Record_1 response = new Record_1();
		response.setPrimitiveValue(req_message.getPrimitiveValue());
		response.setComplexValue(req_message.getComplexValue());
		
		return response;
	}

	@Override
	public Object send_records_test_2(Record_2 req_message) {
		// Echo message
		Record_2 response = new Record_2();
		response.setPrimitiveValue(req_message.getPrimitiveValue());
		response.setComplexValue(req_message.getComplexValue());
		
		return response;
	}

	@Override
	public Object send_records_test_3(Record_1 req_message) {
		// Send a completely new message (because input is null)
		
		Record_1 response = new Record_1();
		response.setPrimitiveValue(Main.DEFAULT_INT);
		response.setComplexValue(Main.DEFAULT_STRING);
		
		return response;
	}

	@Override
	public Object send_records_test_4(Record_1 req_message) {
		// Always return null
		return null;
	}

	@Override
	public CharSequence send_primitive_test_1(CharSequence req_message) {
		// Echo input
		CharSequence response = req_message;
		return response;
	}
	
	@Override
	public CharSequence send_primitive_test_2(Void req_message) {
		// Return expected output
		CharSequence response = Main.DEFAULT_STRING;
		return response;
	}

	@Override
	public void send_primitive_test_3(CharSequence req_message) {
		// Return null
		// return null;	
		return;
	}
	
	@Override
	public CharSequence send_primitive_test_4(CharSequence req_message) {
		// Return expected output
		CharSequence response = Main.DEFAULT_STRING;
		return response;
	}

	@Override
	public CharSequence send_primitive_test_5(CharSequence req_message) {
		// Return null
		return null;
	}

}