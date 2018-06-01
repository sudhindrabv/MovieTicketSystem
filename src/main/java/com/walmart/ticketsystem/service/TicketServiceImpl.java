package com.walmart.ticketsystem.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.walmart.ticketsystem.TicketHoldRunnableTask;
import com.walmart.ticketsystem.TicketSystemApplication;
import com.walmart.ticketsystem.constants.Constants;
import com.walmart.ticketsystem.entity.MovieTheater;
import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.Seat.SEAT_STATUS;
import com.walmart.ticketsystem.entity.SeatHold;
import com.walmart.ticketsystem.exception.EmailValidationException;
import com.walmart.ticketsystem.utils.ConfirmationCodeGenerator;

public class TicketServiceImpl implements TicketService {
	private static final Logger LOGGER = LogManager.getLogger(TicketServiceImpl.class);
	private ScheduledExecutorService scheduledExecuter = TicketSystemApplication.getScheduledExecuterInstance();

	@Override
	public int numSeatsAvailable() {
		MovieTheater movieTheater = MovieTheater.getMovieTheaterInstance();
		int[] availableSeatsPerRow = movieTheater.getAvailableSeatsPerRow();
		int totalSeatsAvailable = 0;
		for (int rowTotal : availableSeatsPerRow) {
			totalSeatsAvailable += rowTotal;
		}
		LOGGER.debug("Number of rows is "+availableSeatsPerRow.length);
		LOGGER.debug("Number of available seats " + totalSeatsAvailable);
		return totalSeatsAvailable;
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		try {
			if (ValidationServiceImpl.getValidationServiceInstance().validateCustomerEmaildId(customerEmail)) {
				return getBestAvailableSeats(numSeats, customerEmail);
			}
		} catch (EmailValidationException e) {
			LOGGER.error(e.getMsg());
		}
		return null;
	}

	private synchronized SeatHold getBestAvailableSeats(int numSeats, String customerEmail) {
		LOGGER.debug("Number of Seats requested by customer " + customerEmail + " is " + numSeats);
		SeatHold seatHold = new SeatHold(customerEmail);
		List<Seat> bestAvailableSeats = new LinkedList<Seat>();
		MovieTheater movieTheater = MovieTheater.getMovieTheaterInstance();
		int[] availableSeatsPerRow = movieTheater.getAvailableSeatsPerRow();
		if (numSeatsAvailable() > numSeats) { // if seats are available irrespective of row
			boolean consecutiveSeatsAvailable = false;

			for (int rowNumber = availableSeatsPerRow.length - 1; rowNumber >= 0; rowNumber--) {// start from top row
				LOGGER.debug("Checking row " + (rowNumber + 1));
				if (numSeats <= availableSeatsPerRow[rowNumber]) { // if seats are available in the row
					Seat[][] allSeats = movieTheater.getSeats();
					int consecutiveSeatsSatisfied = 0;
					consecutiveSeatsAvailable = checkConsecutiveSeatsAreAvailable(numSeats, bestAvailableSeats,
							movieTheater, consecutiveSeatsAvailable, rowNumber, allSeats, consecutiveSeatsSatisfied);
					if (consecutiveSeatsAvailable) { // if seats are satisfied return the best available seats
						break;
					} else { // clear the bestAvailable seats list for current row and start checking for
						// below row
						bestAvailableSeats.clear();
					}
				} else {
					LOGGER.error("Seats are not available in current row " + (rowNumber + 1) + " Hence checking below row\n");
				}
			}
		} else {
			LOGGER.error("Seats are not available to satisfy required seats");
		}

		scheduleSeatHold(seatHold, bestAvailableSeats, movieTheater);
		return seatHold;
	}

	private boolean checkConsecutiveSeatsAreAvailable(int numSeats, List<Seat> bestAvailableSeats,
			MovieTheater movieTheater, boolean consecutiveSeatsAvailable, int rowNumber, Seat[][] allSeats,
			int consecutiveSeatsSatisfied) {
		for (int j = 0; j < Constants.MAX_SEATS_PER_ROW; j++) { // get seats starting from left corner
			Seat seat = allSeats[rowNumber][j];
			LOGGER.debug("Checking Seat " + (j + 1) + " Status is " + seat.getSeatStatus());
			if (seat.getSeatStatus() == Seat.SEAT_STATUS.AVAILABLE) {
				consecutiveSeatsSatisfied++;
				bestAvailableSeats.add(seat); // add to best available seats collection
			}
			if (consecutiveSeatsSatisfied == numSeats) {
				consecutiveSeatsAvailable = true;
				movieTheater.setAvailableSeatsPerRow(rowNumber, numSeats);
				LOGGER.debug("Consecutive seats are available");
				LOGGER.debug("Seats are put on hold status\n");
				break;
			}
		}
		return consecutiveSeatsAvailable;
	}

	private void scheduleSeatHold(SeatHold seatHold, List<Seat> bestAvailableSeats, MovieTheater movieTheater) {
		TicketHoldRunnableTask task = new TicketHoldRunnableTask(bestAvailableSeats, seatHold);
		scheduledExecuter.schedule(task, Constants.HOLD_DURATION_EXPIRY, TimeUnit.MINUTES);

		for (Seat seat : bestAvailableSeats) { // change the seat status to hold
			seat.setSeatStatus(Seat.SEAT_STATUS.HOLD);
		}
		seatHold.setHoldSeats(bestAvailableSeats);
		movieTheater.addToSeatHoldList(seatHold);
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		SeatHold seatHold = MovieTheater.getMovieTheaterInstance().findSeatHoldById(seatHoldId);
		if(seatHold != null) {
			List<Seat> holdSeatsList = seatHold.getHoldSeats();
			if (!seatHold.isTimedOut()) { // if booking is not times out
				for (Seat seat : holdSeatsList) {
					seat.setSeatStatus(SEAT_STATUS.RESERVED);
				}

				String confirmationCode = ConfirmationCodeGenerator.generateId();
				LOGGER.debug("Tickets booked for customer" + customerEmail + " and confirmation ID is " + confirmationCode);
				return confirmationCode;
			} else {
				LOGGER.debug("SeatHold id " + seatHoldId + " timed out");
				LOGGER.debug("Booking failed");
				seatHold.setTimedOut(false);
				/*
				 * for (Seat seat : holdSeatsList) { if
				 * (seat.getSeatStatus().equals(Seat.SEAT_STATUS.HOLD)) {
				 * System.out.println("Seat " + seat.getSeatNumber() + " is made available");
				 * seat.setSeatStatus(Seat.SEAT_STATUS.AVAILABLE); } }
				 */
			}
		}else {
			LOGGER.error("Wrong Hold ID or customer email id is incorrect");
		}
		return null;
	}
}	
