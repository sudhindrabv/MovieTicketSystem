package com.walmart.ticketsystem;

import java.util.List;
import java.util.concurrent.Executors;

import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.SeatHold;

public class TicketHoldRunnableTask implements Runnable {

	private List<Seat> holdSeats;
	private SeatHold seatHold;
	
	public TicketHoldRunnableTask(List<Seat> holdSeats,SeatHold seatHold ) {
		this.holdSeats = holdSeats;
		this.seatHold = seatHold;
	}

	public List<Seat> getHoldSeats() {
		return holdSeats;
	}

	public SeatHold getSeatHold() {
		return seatHold;
	}

	public void setSeatHold(SeatHold seatHold) {
		this.seatHold = seatHold;
	}

	@Override
	public void run() {
		System.out.println("Running Ticket Hold thread " + Thread.currentThread().getId());
		System.out.println("Booking hold expired!");
		getSeatHold().setTimedOut(true);
		for (Seat seat : getHoldSeats()) {
			if (seat.getSeatStatus().equals(Seat.SEAT_STATUS.HOLD)) {
				System.out.println("Seat " + seat.getSeatNumber() + " is made available");
				seat.setSeatStatus(Seat.SEAT_STATUS.AVAILABLE);
			}
		}
	}
}
