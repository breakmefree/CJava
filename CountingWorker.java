package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;

public class CountingWorker implements Runnable {

	private WordCountAndStatus counter;

	public CountingWorker(WordCountAndStatus counter) {
		this.counter = counter;
	}

	@Override
	public void run() {
		System.out.println("START - Counting Worker");
		while (counter.peekConQueue() == null && counter.getCurrStatus() != 'f') {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("The CountingWorker thread has been interrupted, stop processing." + e);
				e.printStackTrace();
			}
			
			while (counter.peekConQueue() != null || counter.getCurrStatus() != 'f') {
				CharBuffer buff = counter.pollConQueue();
				while (buff!= null && buff.hasRemaining()) {
					char c = buff.get();
					//System.out.print(c);
					if (c == ' ' || c == '\n')
						counter.increment();
				}
			}
		} // while outer

		while (counter.peekConQueue() != null || counter.getCurrStatus() != 'f') {
			CharBuffer buff = counter.pollConQueue();
			while (buff != null && buff.hasRemaining()) {
				char c = buff.get();
				//System.out.print(c);
				if (c == ' ' || c == '\n')
					counter.increment();
			}

			while (counter.peekConQueue() == null && counter.getCurrStatus() != 'f') {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("The CountingWorker thread has been interrupted, stop processing." + e);
					e.printStackTrace();
				}
			}
		} // Outer while
		System.out.println("counting complete");
		
		// All files processed - setting the status to 'p' -> 'processed'
		synchronized (counter) {
			if(counter.peekConQueue() == null && counter.getCurrStatus() == 'f') {
				System.out.println("Notify from counting worker*****************");
				counter.setCurrStatus('p');
				counter.notifyAll();
			} else {
				System.out.println("There is some problem counting worker*****************");
			}
		}
		System.out.println("END - Counting Worker");
	} // run()

}
