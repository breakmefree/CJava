package com.prac.interview.threads.producerconsumer;

public class Counter {
	private int words;
	volatile boolean status;

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}
	
	public synchronized void increment() {
		words++;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
