package com.prac.interview.threads.producerconsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProduceData implements Runnable {
	private Counter counter;
	private String directory;
	ConcurrentLinkedQueue<CharBuffer> conQueue;
	
	public ProduceData(String directory, ConcurrentLinkedQueue<CharBuffer> conQueue, Counter counter) {
		this.directory = directory;
		this.conQueue = conQueue;
		this.counter = counter;
	}
	

	@Override
	public void run() {
		File dir = new File(directory);
		File[] flist = dir.listFiles();
		//System.out.println("Dirs: " + flist.length);
		for(File file : flist) {
			//System.out.println("Producer File: " + file.getName());
			if(file.isFile()) {
				
				try (FileReader freader = new FileReader(file); BufferedReader breader = new BufferedReader(freader);) {
					CharBuffer cbuff = CharBuffer.allocate(1024);
					while(breader.read(cbuff) > 0) {
						cbuff.flip();
						if(cbuff.hasRemaining()) {
							//System.out.println("Producer cbuff: " + cbuff.remaining());
							synchronized (conQueue) {
								conQueue.offer(cbuff);
								conQueue.notifyAll();
								conQueue.wait(1000);
							}
							
						}
						cbuff.clear();
					}
					//System.out.println("Producer: Try block done");
					breader.close();
					freader.close();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println("Producer: After Catch block done");
			} //if
		} //For loop
		//System.out.println("Producer: For loop done");
		synchronized (counter) {
			counter.setStatus(true);;
		}
		//System.out.println("Producer: Run done - Status: " + counter.isStatus());
	}
}
