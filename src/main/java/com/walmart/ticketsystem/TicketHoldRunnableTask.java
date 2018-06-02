package com.walmart.ticketsystem;

import java.util.List;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.walmart.ticketsystem.entity.MovieTheater;
import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.SeatHold;
import com.walmart.ticketsystem.service.MovieTheaterService;
import com.walmart.ticketsystem.service.TicketServiceImpl;

/**
 * This class is a Asynchronous runnable task which runs when seats are put on
 * hold and counts down when to release the held seats to available
 * 
 * @author Sudhindra
 *
 */
public class TicketHoldRunnableTask implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(TicketHoldRunnableTask.class);
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
		if (seatHold != null) {
			LOGGER.debug("Running Ticket Hold thread " + Thread.currentThread().getId());
			LOGGER.debug("Booking hold expired!");
			for (Seat seat : seats) {
				if (seat.getSeatStatus().equals(Seat.SEAT_STATUS.HOLD)) {
					LOGGER.debug("Seat Number " + seat.getSeatNumber() + " in row " + seat.getRowNumber()
							+ " is made available");
					seat.setSeatStatus(Seat.SEAT_STATUS.AVAILABLE);
					MovieTheaterService.setAvailableSeatsPerRow(seat.getRowNumber(), 1, true);
				}
			}
			// MovieTheaterService.prettyPrintMovieTicket();
		}

	}
}
