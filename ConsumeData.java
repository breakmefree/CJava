package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConsumeData implements Runnable {
	
	private Counter counter;
	ConcurrentLinkedQueue<CharBuffer> conQueue;
	
	public ConsumeData(Counter counter, ConcurrentLinkedQueue<CharBuffer> conQueue) {
		this.counter = counter;
		this.conQueue = conQueue;
	}

	@Override
	public void run() {
		synchronized (conQueue) {
			while(conQueue.peek() == null) {
				try {
					//System.out.println("Waiting..");
					conQueue.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//System.out.println("Wait over! - status: " + counter.isStatus());
				
				while(conQueue.peek() != null) {
					//System.out.println("Consumer Peek");
					CharBuffer buff = conQueue.poll();
					while(buff.hasRemaining()) {
						char c = buff.get();
						System.out.print(c);
						//StringBuilder s = new StringBuilder();
						if(c == ' ' || c == '\n')
							counter.increment();
					}
				}
				//System.out.println("Consumer Inner While Done");
				if(counter.isStatus()) {
					break;	
				}
			} //while outer
			//System.out.println("Consumer Outer While Done");
		}
		//System.out.println("Consumer Run Done");
	} //run()

}
