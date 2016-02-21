package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CountingWorker implements Runnable {

	private WordCountAndStatus counter;
	ConcurrentLinkedQueue<CharBuffer> conQueue;

	public CountingWorker(WordCountAndStatus counter, ConcurrentLinkedQueue<CharBuffer> conQueue) {
		this.counter = counter;
		this.conQueue = conQueue;
	}

	@Override
	public void run() {
		while (conQueue.peek() == null && !counter.isStatus()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("The CountingWorker thread has been interrupted, stop processing." + e);
				e.printStackTrace();
			}
			
			while (conQueue.peek() != null || !counter.isStatus()) {
				CharBuffer buff = conQueue.poll();
				while (buff.hasRemaining()) {
					char c = buff.get();
					System.out.print(c);
					if (c == ' ' || c == '\n')
						counter.increment();
				}
			}
		} // while outer

		while (conQueue.peek() != null || !counter.isStatus()) {
			CharBuffer buff = conQueue.poll();
			while (buff.hasRemaining()) {
				char c = buff.get();
				//System.out.print(c);
				if (c == ' ' || c == '\n')
					counter.increment();
			}

			while (conQueue.peek() == null && !counter.isStatus()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("The CountingWorker thread has been interrupted, stop processing." + e);
					e.printStackTrace();
				}
			}
		} // Outer while
	} // run()

}
