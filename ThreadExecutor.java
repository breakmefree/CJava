package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadExecutor {

	public static void main(String[] args) {
		WordCountAndStatus counter = new WordCountAndStatus();
		counter.setStatus(false);
		ConcurrentLinkedQueue<CharBuffer> conQueue = new ConcurrentLinkedQueue<>();
		
		String directory = "/Users/kshamanidhi/kshama/Notes";
		IOWorker pdata = new IOWorker(directory, conQueue, counter);
		//CountingWorker cdata = new CountingWorker(counter, conQueue);
		
		try {
			Thread producerOne = new Thread(pdata);
			//Thread producerTwo = new Thread(pdata);
			//Thread consumerOne = new Thread(cdata);
		
			producerOne.start();
			//consumerOne.start();
			//consumerTwo.start();
		
			producerOne.join();
			//consumerOne.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Total words: " + counter.getWords());

	}

}
