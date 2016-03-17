package com.prac.interview.threads.producerconsumer;

import static com.prac.interview.threads.producerconsumer.WordCountAndStatus.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.ExecutorService;

import com.prac.interview.threads.producerconsumer.WordCountAndStatus.State;

/**
 * This file reads a file from the disk, adds the data to a concurrent queue.
 * It then spawns the CountingWorker jobs and submit it to a ExecutorService.
 * It also updated its exit status to the shared object of class WordCountAndStatus.
 * 
 * @author kshamanidhi
 *
 */
public class IOWorker implements Runnable {
	private WordCountAndStatus counter;
	private File file;
	ExecutorService exeServiceCW;

	public IOWorker(File file, WordCountAndStatus counter, ExecutorService exeServiceCW) {
		this.file = file;
		this.counter = counter;
		this.exeServiceCW = exeServiceCW;
	}

	@Override
	public void run() {
		System.out.println("START - IO Worker");
		try (FileReader freader = new FileReader(file); BufferedReader breader = new BufferedReader(freader);) {
			CharBuffer cbuff = CharBuffer.allocate(1024 * 1024);
			while (breader.read(cbuff) > 0) {
				cbuff.flip();
				if (cbuff.hasRemaining()) {
					offerConQueue(cbuff);
					synchronized (WordCountAndStatus.class) {
						// increment the number of counting word job by 1.
						incrementCWJobTrack();
					}
					// Start a CountingWorker thread to start the processing on the file.
					exeServiceCW.execute(new CountingWorker(counter));
				}
				cbuff = CharBuffer.allocate(1024 * 1024);
			}
			breader.close();
			freader.close();
		} catch (IOException e) {
			System.out.println("The File is not found on the disk, stop processing." + e);
			e.printStackTrace();
		}
		synchronized (counter) {
			decrementIoJobTrack();
			if (getIoJobCountTrack() == 0 && getCurrStatus() == State.SCHEDULED) {
				setCurrStatus(State.FETCHED);
				System.out.println("IOWorker: Current status is FETCHED");
				counter.notify();
			}
		}
		System.out.println("END - IO Worker");
	}
}
