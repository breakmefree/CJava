package com.prac.interview.threads.producerconsumer;

import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Used as shared objects between threads for tracking the number of words counted.
 * All to check if the file reading is over from the disk.
 * 
 * @author kshamanidhi
 *
 */
public class WordCountAndStatus {
	/*
	 * The list of standard states milestones used to tracking. 
	 */
	public enum State {
		INIT, SCHEDULED, FETCHED, COMPUTED
	}
			
		
	private static int words; 			// Used to track total number of words read.
	private static State currStatus;	//Used to track the current milestones achieved for overall jobs.
	private static int ioJobCountTrack; //Used to track the currently executing IO jobs.
	private static int cwJobCountTrack; //Used to track the currently executing Counting words jobs.
	
	private static ConcurrentLinkedQueue<CharBuffer> conQueue; // Used to enqueue the data read from the file

	public static int getWords() {
		return words;
	}
	
	/**
	 * Used to count all the words read till now by FileProcWorker.
	 */
	public synchronized static void incrementWordCount() {
		words++;
	}

	public static State getCurrStatus() {
		return currStatus;
	}

	/**
	 * Set currStatus to 
	 *  INIT, SCHEDULED, FETCHED, COMPUTED
	 * 
	 * @param currStatus
	 */
	public static void setCurrStatus(State currStatus) {
		WordCountAndStatus.currStatus = currStatus;
	}

	/**
	 * Gets the number of current IO jobs executing.
	 * @return
	 */
	public static int getIoJobCountTrack() {
		return ioJobCountTrack;
	}

	/**
	 * Increments the current IO jobs executing by 1.
	 */
	public static void incrementIoJobTrack() {
		WordCountAndStatus.ioJobCountTrack++;
	}
	
	/**
	 * Decrements the current IO jobs executing by 1.
	 */
	public static void decrementIoJobTrack() {
		WordCountAndStatus.ioJobCountTrack--;
	}

	/**
	 * Gets the number of currently executing IO jobs.
	 * @return
	 */
	public static int getCWJobCountTrack() {
		return cwJobCountTrack;
	}

	/**
	 * Increments the currently executing Counting words jobs by 1.
	 */
	public static void incrementCWJobTrack() {
		WordCountAndStatus.cwJobCountTrack++;
	}
	
	/**
	 * Decrements the number of currently executing Counting words jobs by 1.
	 */
	public static void decrementCWJobTrack() {
		WordCountAndStatus.cwJobCountTrack--;
	}

	public static ConcurrentLinkedQueue<CharBuffer> getConQueue() {
		return conQueue;
	}

	public static void setConQueue(ConcurrentLinkedQueue<CharBuffer> conQueue) {
		WordCountAndStatus.conQueue = conQueue;
	}
	
	public static CharBuffer pollConQueue() {
		return conQueue.poll();
	}

	public static void offerConQueue(CharBuffer cbuff) {
		WordCountAndStatus.conQueue.offer(cbuff);
	}
	
	public static CharBuffer peekConQueue() {
		return WordCountAndStatus.conQueue.peek();
	}
	
}
