package com.prac.interview.threads.producerconsumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {
	public static final String DIR_PATH = "/Users/kshamanidhi/kshama/Notes";
	public static final int NO_FILES = 10;

	public static void main(String[] args) {
		//File dir = new File(DIR_PATH);
		// Creating files in the given directory
		for(int i=0; i < NO_FILES; i++) {
			try {
				File file = new File(DIR_PATH + "/file-" + i + ".txt");
				// if file doesn't exists create new file
				if(!file.exists()) {
					file.createNewFile();
				}
				FileWriter write = new FileWriter(file);
				BufferedWriter bwrite = new BufferedWriter(write);
				for(int j = 0; j < 1000000; j++) {
					bwrite.write("cat cat cat cat cat cat cat cat ");
				}
				bwrite.close();
				
			} catch (IOException e) {
				System.out.println("IOException occured while creating file: " + i);
				e.printStackTrace();
			}
		}
		

	}

}
