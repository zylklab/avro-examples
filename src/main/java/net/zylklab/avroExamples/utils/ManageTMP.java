package main.java.net.zylklab.avroExamples.utils;

import java.io.File;

public class ManageTMP {

	public static void createTmp (String path) {
		new File(path).mkdirs();
	}
	
	public static void removeTmp (String path) {
		new File(path).delete();
	}
	
}

