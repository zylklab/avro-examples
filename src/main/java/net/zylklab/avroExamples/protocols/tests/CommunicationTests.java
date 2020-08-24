package net.zylklab.avroExamples.protocols.tests;

import java.util.ArrayList;
import java.util.List;

import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.zylklab.avroExamples.generated.Record_1;
import net.zylklab.avroExamples.generated.Record_2;
import net.zylklab.avroExamples.protocols.Main;
import net.zylklab.avroExamples.protocols.utils.ClientAvro;
import net.zylklab.avroExamples.protocols.utils.ProxyServerAvro;
import net.zylklab.avroExamples.protocols.utils.ReportIO;

public class CommunicationTests {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationTests.class);

	public void testCommunications(ProxyServerAvro server, ClientAvro client) {

		ReportIO reporter = new ReportIO();
		ArrayList<Integer> results = new ArrayList<Integer>();

		LOGGER.info("Test Records 1: Echo input message.");
		results.add(testRecords_1(client)); // Pass
		reporter.reportResults(results.get(results.size() - 1));

		LOGGER.info("Test Records 2: Echo input message with expected nulls.");
		results.add(testRecords_2(client)); // Pass
		reporter.reportResults(results.get(results.size() - 1));

		LOGGER.info("Test Records 3: Input unexpected null, return expected record.");
		results.add(testRecords_3(client)); // Fail
		reporter.reportResults(results.get(results.size() - 1));

		LOGGER.info("Test Records 4: Input expected record, return unexpected null.");	
		results.add(testRecords_4(client)); // Fail
		reporter.reportResults(results.get(results.size() - 1 ));

		LOGGER.info("Test Primitives 1: Echo input message.");	
		results.add(testPrimitives_1(client)); // Pass
		reporter.reportResults(results.get(results.size() - 1 ));
		
		LOGGER.info("Test Primitives 2: Input expected null, return expected value.");	
		results.add(testPrimitives_2(client));  // Fail
		reporter.reportResults(results.get(results.size() - 1 ));

		LOGGER.info("Test Primitives 3: Input expected value, return expected null.");	
		results.add(testPrimitives_3(client));  // Pass
		reporter.reportResults(results.get(results.size() - 1 ));

		LOGGER.info("Test Primitives 4: Input unexpected null, return expected value.");			
		results.add(testPrimitives_4(client));  // Fail
		reporter.reportResults(results.get(results.size() - 1 ));

		LOGGER.info("Test Primitives 5: Input expected value, return unexpected null.");			
		results.add(testPrimitives_5(client));  // Fail
		reporter.reportResults(results.get(results.size() - 1 ));

		reportSuccesses(results, 4);
		
		return;
	}

	private int testRecords_1(ClientAvro client) {

		try {
			// Create input record
			Record_1 msg = new Record_1();
			msg.setPrimitiveValue(Main.DEFAULT_INT);
			msg.setComplexValue(Main.DEFAULT_STRING);

			// Send and receive the answer
			Record_1 response = client.sendRecordMsg_test_1(msg);

			// Compare with the expected results
			return compareRecords(msg, response);

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}

	}

	private int testRecords_2(ClientAvro client) {
		try {
			// Create input record
			Record_2 msg = new Record_2();
			msg.setPrimitiveValue(Main.DEFAULT_INT);
			msg.setComplexValue(null);

			// Send and receive the answer
			Record_2 response = client.sendRecordMsg_test_2(msg);

			// Compare with the expected results
			return compareRecords(msg, response);

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private int testRecords_3(ClientAvro client) {
		try {
			// Create input record
			Record_1 expectedResponse = new Record_1();
			expectedResponse.setPrimitiveValue(Main.DEFAULT_INT);
			expectedResponse.setComplexValue(Main.DEFAULT_STRING);

			// Send and receive the answer
			Record_1 response = client.sendRecordMsg_test_3(null);

			// Compare with the expected results
			return compareRecords(response, expectedResponse);	// We are never reaching this, we'll get an exception before that happens
			
		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private int testRecords_4(ClientAvro client) {
		try {
			// Create input record
			Record_1 msg = new Record_1();
			msg.setPrimitiveValue(Main.DEFAULT_INT);
			msg.setComplexValue(Main.DEFAULT_STRING);

			// Send and receive the answer
			Record_1 response = client.sendRecordMsg_test_4(msg);

			// Compare with the expected results
			if (response == null) {
				return 1;
			} else {
				return -1;
			}
		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private Integer testPrimitives_1(ClientAvro client) {
		try {

			String msg = Main.DEFAULT_STRING;
			// Send and receive the answer
			String response = client.sendPrimitiveMsg_test_1(msg);

			// Compare with the expected results
			return comparePrimitives(msg, response);

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private Integer testPrimitives_2(ClientAvro client) {
		try {
			String expectedResponse = Main.DEFAULT_STRING;
			// Send and receive the answer
			String response = client.sendPrimitiveMsg_test_2();

			// Compare with the expected results
			return comparePrimitives(expectedResponse, response);

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private Integer testPrimitives_3(ClientAvro client) {
		try {

			String msg = Main.DEFAULT_STRING;
			// Send and receive the answer
			client.sendPrimitiveMsg_test_3(msg);

			// If there are no errors, we assume the task performed successfully
			return 1;

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private Integer testPrimitives_4(ClientAvro client) {
		try {
			String expectedResponse = Main.DEFAULT_STRING;
			// Send and receive the answer
			String response = client.sendPrimitiveMsg_test_4(null);

			// Compare with the expected results
			return comparePrimitives(expectedResponse, response);

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private Integer testPrimitives_5(ClientAvro client) {
		try {
			String msg = Main.DEFAULT_STRING;
			// Send and receive the answer
			String response = client.sendPrimitiveMsg_test_5(msg);

			// Compare with the expected results
			return comparePrimitives(null, response);

		} catch (Exception ex) {
			LOGGER.error("Test failed due to a thrown exception.");
			LOGGER.error(ex.getMessage());
			return -1;
		}
	}

	private int compareRecords(SpecificRecord msg, SpecificRecord response) {
		if (msg.equals(response)) {
			return 1;
		} else {
			return -1;
		}
	}
	
	private int comparePrimitives(String msg, String response) {
		if (msg == null && response == null) {
			return 1;
		}
		else if (msg.equals(response)) {
			return 1;
		} else {
			return -1;
		}
	}
	
	protected void reportSuccesses(List<Integer> results, int expectedHits) {
		// Count number of successes
		int count=0;
		for (Integer i : results) {
			if (i>0) {
				count += 1;
			}
		}
		LOGGER.info("Expected " + expectedHits + " tests to run successfully. Got " + count + ".");
	}

}
