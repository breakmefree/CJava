package com.prac.interview.threads.producerconsumer;

import static com.prac.interview.threads.producerconsumer.WordCountAndStatus.*;

import java.io.File;
import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.prac.interview.threads.producerconsumer.WordCountAndStatus.State;

/**
 * This file creates IOWorker runnables and submit it to the executor service.
 * It keeps track of both the IOWorker and Counting worker jobs and finally 
 * prints the output for the processing.
 * 
 * @author kshamanidhi
 *
 */
public class ThreadExecutor {
	// All the files from this location are read - not recursively.
	public static final String DIR_PATH = "/Users/kshamanidhi/kshama/Notes";

	public static void main(String[] args) {

		WordCountAndStatus counter = new WordCountAndStatus();

		// Setting the current processing status to INIT
		WordCountAndStatus.setCurrStatus(State.INIT);

		ConcurrentLinkedQueue<CharBuffer> conQueue = new ConcurrentLinkedQueue<>();
		setConQueue(conQueue);

		ExecutorService exeServiceIO = Executors.newFixedThreadPool(5);
		ExecutorService exeServiceCW = Executors.newFixedThreadPool(3);

		File dir = new File(DIR_PATH);
		File[] flist = dir.listFiles();
		// System.out.println("Dirs: " + flist.length);
		int ct = 0;
		for (File file : flist) {
			if (ct++ == 0)
				continue;
			if (file.isFile()) {
				// increment the number of currently executing jobs by 1
				incrementIoJobTrack();
				// Start a IOWorker thread
				exeServiceIO.execute(new IOWorker(file, counter, exeServiceCW));

			} else {
				System.out.println("Processing text files only.");
			}
		}

		// Since all the IOWorker jobs are submitted changed the status to SCHEDULED
		synchronized (WordCountAndStatus.class) {
			if (getCurrStatus() == State.INIT) {
				setCurrStatus(State.SCHEDULED);
				System.out.println("ThreadExe: Current status is SCHEDULED");
			}
		}

		synchronized (counter) {
			while (getCurrStatus() != State.FETCHED) {
				try {
					counter.wait();
					System.out.println("ThreadExe: Current status is FETCHED - Stopping IOWorker ExecutorService");
					exeServiceIO.shutdown();
				} catch (InterruptedException e) {
					System.out.println("Main Thread interrupted. Check the current status again before exit.");
					e.printStackTrace();
				}
			}
		}

		synchronized (counter) {
			while (getCurrStatus() != State.COMPUTED) {
				try {
					counter.wait();
					System.out.println("ThreadExe: Current status is COMPUTED - Stopping CountingWorker ExecutorService");
					exeServiceCW.shutdown();
				} catch (InterruptedException e) {
					System.out.println("Main Thread interrupted. Check the current status again before exit.");
					e.printStackTrace();
				}
			}
		}
		// printing the total number of words counted.
		System.out.println("Total words: " + getWords());
	}
}
