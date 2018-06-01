package com.walmart.ticketsystem.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class added to handle functionalities related to HoldIdGenerator
 * @author Sudhindra
 *
 */
public class HoldIdGenerator {
	private static final AtomicInteger counter = new AtomicInteger(1);

	public static int generateHoldId() {
		int holdId = nextValue();
		System.out.println("Hold ID = " + holdId);
		return holdId;
	}

	public synchronized static int nextValue() {
		return counter.getAndIncrement();
	}
}