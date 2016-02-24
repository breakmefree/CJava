package com.prac.interview.threads.producerconsumer;

import java.io.File;
import java.nio.CharBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadExecutor {
	public static final String DIR_PATH = "/Users/kshamanidhi/kshama/Notes";	// All the files from this location are read - not recursively.

	public static void main(String[] args) {
		
		WordCountAndStatus counter = new WordCountAndStatus();
		counter.setCurrStatus('i');	//Setting the current processing status to i -> 'init'
		
		ConcurrentLinkedQueue<CharBuffer> conQueue = new ConcurrentLinkedQueue<>();
		counter.setConQueue(conQueue);
		
		ConcurrentLinkedQueue<Future<?>> futureIO = new ConcurrentLinkedQueue<>();
		
		ExecutorService exeSIO = Executors.newFixedThreadPool(5);
		ExecutorService exeSWorker = Executors.newFixedThreadPool(3);
		
		File dir = new File(DIR_PATH);
		File[] flist = dir.listFiles();
		//System.out.println("Dirs: " + flist.length);
		int ct = 0;
		for(File file : flist) {
			if(ct++ == 0) continue;
			if(file.isFile()) {
				// Start a IOWorker thread
				Future<?> iof = exeSIO.submit(new IOWorker(file, counter));
				futureIO.offer(iof);
				
				// Start a CountingWorker thread to start the processing on the file.
				exeSWorker.execute(new CountingWorker(counter));
			} else {
				System.out.println("Processing text files only.");
			}
		}
		
		for(Future<?> f : futureIO) {
			if(f == null || f.isDone() || f.isCancelled()) continue;
			try {
				System.out.println("This IOWorker thread completed " + f.get());
			} catch (InterruptedException e) {
				System.out.println("Main Thread interrupted. Check the current status again before exit.");
				e.printStackTrace();
			} catch (ExecutionException e) {
				System.out.println("IOWorker task aborted. Cause: " + e.getCause());
				e.printStackTrace();
			}
		}
		
		synchronized (counter) {
			counter.setCurrStatus('f');
			System.out.println("Status: Fetched");
		}
		exeSIO.shutdown();
		
		synchronized (counter) {
			while(counter.getCurrStatus() != 'p') {
				try {
					counter.wait();
					exeSWorker.shutdown();
				} catch (InterruptedException e) {
					System.out.println("Main Thread interrupted. Check the current status again before exit.");
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("Total words: " + counter.getWords());

	}
	
}
