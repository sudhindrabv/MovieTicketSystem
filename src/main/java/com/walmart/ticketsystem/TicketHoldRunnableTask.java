package com.walmart.ticketsystem;

import java.util.List;
import java.util.concurrent.Executors;

import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.SeatHold;
import com.walmart.ticketsystem.service.MovieTheaterService;

/**
 * This class is a Asynchronous runnable task which runs when seats are put on
 * hold and counts down when to release the held seats to available
 * 
 * @author sudhi
 *
 */
public class TicketHoldRunnableTask implements Runnable {

	private List<Seat> holdSeats;
	private SeatHold seatHold;
	private String customerEmail;

	public TicketHoldRunnableTask(List<Seat> holdSeats, SeatHold seatHold, String customerEmail) {
		this.holdSeats = holdSeats;
		this.seatHold = seatHold;
		this.customerEmail = customerEmail;
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

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	@Override
	public void run() {
		getSeatHold().setTimedOut(true);
		List<Seat> seats = getHoldSeats();
		if(seatHold != null) {
			System.out.println("Running Ticket Hold thread " + Thread.currentThread().getId());
			System.out.println("Booking hold expired!");
			for (Seat seat : seats) {
				if (seat.getSeatStatus().equals(Seat.SEAT_STATUS.HOLD)) {
					System.out.println("Seat " + seat.getSeatNumber() + " is made available");
					seat.setSeatStatus(Seat.SEAT_STATUS.AVAILABLE);
				}
			}
			//MovieTheaterService.prettyPrintMovieTicket();
		}
		
	}
}
