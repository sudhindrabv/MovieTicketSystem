package com.walmart.ticketsystem.entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatHold {
	private int seatHoldId;
	private String customerEmail;
	private List<Seat> seatsHold;
	private boolean isTimedOut;
	private static final AtomicInteger counter = new AtomicInteger(1);

	public SeatHold(String customerEmail) {
		super();
		this.seatHoldId = generateHoldId();
		this.customerEmail = customerEmail;
		this.setTimedOut(false);
	}

	public synchronized int generateHoldId() {
		int holdId = nextValue();
		System.out.println("Hold ID =" + holdId);
		return holdId;
	}

	public static int nextValue() {
		return counter.getAndIncrement();
	}

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public List<Seat> getHoldSeats() {
		return seatsHold;
	}

	public void setHoldSeats(List<Seat> holdSeats) {
		this.seatsHold = holdSeats;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public boolean isTimedOut() {
		return isTimedOut;
	}

	public void setTimedOut(boolean isTimedOut) {
		this.isTimedOut = isTimedOut;
	}

}
