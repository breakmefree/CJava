package com.prac.interview.threads.producerconsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IOWorker implements Runnable {
	private WordCountAndStatus counter;
	private String directory;
	ConcurrentLinkedQueue<CharBuffer> conQueue;
	
	public IOWorker(String directory, ConcurrentLinkedQueue<CharBuffer> conQueue, WordCountAndStatus counter) {
		this.directory = directory;
		this.conQueue = conQueue;
		this.counter = counter;
	}
	

	@Override
	public void run() {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		File dir = new File(directory);
		File[] flist = dir.listFiles();
		//System.out.println("Dirs: " + flist.length);
		int ct = 0;
		for(File file : flist) {
			if(ct++ == 0) continue;
			if(file.isFile()) {
				System.out.println("Producer File: " + file.getName());
				try (FileReader freader = new FileReader(file); 
						BufferedReader breader = new BufferedReader(freader);) {
					//
					CharBuffer cbuff = CharBuffer.allocate(1024 * 1024);
					while(breader.read(cbuff) > 0) {
						cbuff.flip();
						if(cbuff.hasRemaining()) {
							conQueue.offer(cbuff);
							CountingWorker cdata = new CountingWorker(counter, conQueue);
							executorService.execute(cdata);
						}
						cbuff = CharBuffer.allocate(1024 * 1024);
					}
					breader.close();
					freader.close();
				} catch (IOException e) {
					System.out.println("The File is not found on the disk, stop processing." + e);
					e.printStackTrace();
				}
			} //if
		} //For loop
		synchronized (counter) {
			counter.setStatus(true);;
		}
		executorService.shutdown();
	}
}
