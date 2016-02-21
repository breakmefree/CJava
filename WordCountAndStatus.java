package com.prac.interview.threads.producerconsumer;

/**
 * Used as shared objects between threads for tracking the number of words counted.
 * All to check if the file reading is over from the disk.
 * 
 * @author kshamanidhi
 *
 */
public class WordCountAndStatus {
	
	private int words; // Used to track total number of words read.
	boolean status;	   // Used to track if the IOWorker is done with its job.

	public int getWords() {
		return words;
	}
	
	/**
	 * Used to count all the words read till now by FileProcWorker.
	 */
	public synchronized void increment() {
		words++;
	}

	/**
	 * Used by FileProcWorker to track if IOWorker is done with reading files from disk.
	 * @return
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * Set to 'false' when this object is initialized.
	 * Later set to 'true' by IOWorker when it's job is done.
	 * 
	 * @param status
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
}
