package com.prac.interview.threads.producerconsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;

public class IOWorker implements Runnable {
	private WordCountAndStatus counter;
	private File file;
	
	public IOWorker(File file, WordCountAndStatus counter) {
		this.file = file;
		this.counter = counter;
	}
	

	@Override
	public void run() {
		System.out.println("START - IO Worker");
		try (FileReader freader = new FileReader(file); BufferedReader breader = new BufferedReader(freader);) {
			CharBuffer cbuff = CharBuffer.allocate(1024 * 1024);
			while (breader.read(cbuff) > 0) {
				cbuff.flip();
				if (cbuff.hasRemaining()) {
					counter.offerConQueue(cbuff);
				}
				cbuff = CharBuffer.allocate(1024 * 1024);
			}
			breader.close();
			freader.close();
		} catch (IOException e) {
			System.out.println("The File is not found on the disk, stop processing." + e);
			e.printStackTrace();
		}
		System.out.println("END - IO Worker");
	}
}
