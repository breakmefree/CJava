package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

/**
 * Used as shared objects between threads for tracking the number of words counted.
 * All to check if the file reading is over from the disk.
 * 
 * @author kshamanidhi
 *
 */
public class WordCountAndStatus {
	
	private int words; // Used to track total number of words read.
	char currStatus;
	ConcurrentLinkedQueue<CharBuffer> conQueue; // Used to enqueue the data read from the file
	
	static ExecutorService exeSWorker;

	public int getWords() {
		return words;
	}
	
	/**
	 * Used to count all the words read till now by FileProcWorker.
	 */
	public synchronized void increment() {
		words++;
	}

	public char getCurrStatus() {
		return currStatus;
	}

	/**
	 * Set currStatus to 
	 * i = 'init'
	 * f = 'fetched'
	 * p = 'processed'
	 * 
	 * @param currStatus
	 */
	public void setCurrStatus(char currStatus) {
		this.currStatus = currStatus;
	}

	public ConcurrentLinkedQueue<CharBuffer> getConQueue() {
		return conQueue;
	}

	public void setConQueue(ConcurrentLinkedQueue<CharBuffer> conQueue) {
		this.conQueue = conQueue;
	}
	
	public CharBuffer pollConQueue() {
		return conQueue.poll();
	}

	public void offerConQueue(CharBuffer cbuff) {
		this.conQueue.offer(cbuff);
	}
	
	public CharBuffer peekConQueue() {
		return this.conQueue.peek();
	}

	public void setExeSWorker(ExecutorService exeSWorker) {
		WordCountAndStatus.exeSWorker = exeSWorker;
	}
	
}
