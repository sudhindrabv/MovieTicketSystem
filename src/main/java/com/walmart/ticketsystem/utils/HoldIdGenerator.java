package com.walmart.ticketsystem.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class HoldIdGenerator {
	private static final AtomicInteger counter = new AtomicInteger(1);

	public synchronized static int generateHoldId() {
		int holdId = nextValue();
		System.out.println("Hold ID = " + holdId);
		return holdId;
	}

	public static int nextValue() {
		return counter.getAndIncrement();
	}
}