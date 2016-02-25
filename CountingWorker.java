package com.prac.interview.threads.producerconsumer;

import static com.prac.interview.threads.producerconsumer.WordCountAndStatus.*;

import java.nio.CharBuffer;

import com.prac.interview.threads.producerconsumer.WordCountAndStatus.State;

/**
 * It reads data from the concurrent queue and counts the number of words in it.
 * Before exit it updates its exit status to the shared object of class WordCountAndStatus.
 * @author kshamanidhi
 *
 */
public class CountingWorker implements Runnable {

	private static WordCountAndStatus counter;

	public CountingWorker(WordCountAndStatus counter) {
		CountingWorker.counter = counter;
	}

	@Override
	public void run() {
		System.out.println("START - Counting Worker");

		if (peekConQueue() != null) {
			CharBuffer buff = pollConQueue();
			while (buff != null && buff.hasRemaining()) {
				char c = buff.get();
				// System.out.print(c);
				if (c == ' ' || c == '\n') {
					incrementWordCount();
				}
			}
		} else {
			System.out.println("The conQueue is empty");
		}

		//System.out.println("current job complete");

		// All files processed - setting the status to COMPUTED
		synchronized (counter) {
			decrementCWJobTrack();
			if (peekConQueue() == null && getCWJobCountTrack() == 0 && getCurrStatus() == State.FETCHED) {
				setCurrStatus(State.COMPUTED);
				System.out.println("All counting comlete! Great job!");
				counter.notify();
			}
		}
		System.out.println("END - Counting Worker");
	} // run()

}
