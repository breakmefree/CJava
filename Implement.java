package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Implement {

	public static void main(String[] args) {
		Counter counter = new Counter();
		counter.setStatus(false);
		ConcurrentLinkedQueue<CharBuffer> conQueue = new ConcurrentLinkedQueue<>();
		
		String directory = "/Users/macbookpro/Notes";
		ProduceData pdata = new ProduceData(directory, conQueue, counter);
		ConsumeData cdata = new ConsumeData(counter, conQueue);
		
		Thread producer = new Thread(pdata);
		Thread consumerOne = new Thread(cdata);
		
		producer.start();
		consumerOne.start();
		//consumerTwo.start();
		try {
			producer.join();
			consumerOne.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Total words: " + counter.getWords());

	}

}
