package main.java.net.zylklab.avroExamples;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.java.net.zylklab.avroExamples.tests.TestArrays;

public class Main {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	
    public static void main(String[] args) {
    	/*
        System.out.println("Looking for configuration on classpath:");
        URL resource = ClassLoader.getSystemResource("log4j.properties"); 
        System.out.println("found"+String.valueOf(resource));
        */
        
        LOGGER.info("Testing arrays");
        TestArrays testArrays = new TestArrays();
        testArrays.testCompatibility();
        
        
    }
}
