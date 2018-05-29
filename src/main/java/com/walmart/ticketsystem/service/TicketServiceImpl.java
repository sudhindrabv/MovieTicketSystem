package com.walmart.ticketsystem.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.walmart.ticketsystem.TicketHoldRunnableTask;
import com.walmart.ticketsystem.TicketSystemApplication;
import com.walmart.ticketsystem.constants.Constants;
import com.walmart.ticketsystem.entity.MovieTheater;
import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.Seat.SEAT_STATUS;
import com.walmart.ticketsystem.entity.SeatHold;
import com.walmart.ticketsystem.utils.ConfirmationCodeGenerator;

public class TicketServiceImpl implements TicketService {
	ScheduledExecutorService scheduledExecuter = TicketSystemApplication.getScheduledExecuterInstance();

	@Override
	public int numSeatsAvailable() {
		MovieTheater movieTheater = MovieTheater.getMovieTheaterInstance();
		int[] availableSeatsPerRow = movieTheater.getAvailableSeatsPerRow();
		int totalSeatsAvailable = 0;
		for (int rowTotal : availableSeatsPerRow) {
			totalSeatsAvailable += rowTotal;
		}
		System.out.println("Number of available seats " + totalSeatsAvailable);
		return totalSeatsAvailable;
	}

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		return getBestAvailableSeats(numSeats, customerEmail);
	}

	private synchronized SeatHold getBestAvailableSeats(int numSeats, String customerEmail) {
		System.out.println("num of Seats requested by customer " + customerEmail + " is " + numSeats);
		SeatHold seatHold = new SeatHold(customerEmail);
		List<Seat> bestAvailableSeats = new LinkedList<Seat>();
		MovieTheater movieTheater = MovieTheater.getMovieTheaterInstance();
		int[] availableSeatsPerRow = movieTheater.getAvailableSeatsPerRow();
		if (numSeatsAvailable() > numSeats) { // if seats are available irrespective of row
			boolean consecutiveSeatsAvailable = false;

			for (int rowNumber = availableSeatsPerRow.length - 1; rowNumber >= 0; rowNumber--) {// start from top row
				System.out.println("Checking row " + rowNumber);
				if (numSeats <= availableSeatsPerRow[rowNumber]) { // if seats are available in the row
					Seat[][] allSeats = movieTheater.getSeats();
					int consecutiveSeatsSatisfied = 0;

					for (int j = 0; j < Constants.MAX_SEATS_PER_ROW; j++) { // get seats starting from left corner
						Seat seat = allSeats[rowNumber][j];
						System.out.println("Checking Seat " + j + " Status is " + seat.getSeatStatus());
						if (seat.getSeatStatus() == Seat.SEAT_STATUS.AVAILABLE) {
							consecutiveSeatsSatisfied++;
							bestAvailableSeats.add(seat); // add to best available seats collection
						}
						if (consecutiveSeatsSatisfied == numSeats) {
							consecutiveSeatsAvailable = true;
							movieTheater.setAvailableSeatsPerRow(rowNumber, numSeats);
							System.out.println("Consecutive seats are available");
							System.out.println("Seats are put on hold status\n");
							break;
						}
					}
					if (consecutiveSeatsAvailable) { // if seats are satisfied return the best available seats
						break;
					} else { // clear the bestAvailable seats list and start checking for below row
						bestAvailableSeats.clear();
					}
				} else {
					System.out.println("Seats are not available in row " + rowNumber + " checking below row\n");
				}
			}
		} else {
			System.out.println("Seats are not available to satisfy required seats");
		}

		TicketHoldRunnableTask task = new TicketHoldRunnableTask(bestAvailableSeats,seatHold);
		scheduledExecuter.schedule(task, 1, TimeUnit.MINUTES);

		for (Seat seat : bestAvailableSeats) { // change the seat status to hold
			seat.setSeatStatus(Seat.SEAT_STATUS.HOLD);
		}
		seatHold.setHoldSeats(bestAvailableSeats);
		movieTheater.addToSeatHoldList(seatHold);
		return seatHold;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		SeatHold seatHold = MovieTheater.getMovieTheaterInstance().findSeatHoldById(seatHoldId);
		List<Seat> holdSeatsList = seatHold.getHoldSeats();
		if (!seatHold.isTimedOut()) { //if booking is not times out
			for (Seat seat : holdSeatsList) {
				seat.setSeatStatus(SEAT_STATUS.RESERVED);
			}

			String confirmationCode = ConfirmationCodeGenerator.generateId();
			System.out.println("Tickets booked for customer" + customerEmail + 
					" and confirmation ID is " + confirmationCode);
			return confirmationCode;
		} else {
			System.out.println("SeatHold id " + seatHoldId + " timed out");
			System.out.println("Booking failed");
			//scheduledExecuter.shutdownNow();
			//seatHold.setTimedOut(false);
			/*for (Seat seat : holdSeatsList) {
				if (seat.getSeatStatus().equals(Seat.SEAT_STATUS.HOLD)) {
					System.out.println("Seat " + seat.getSeatNumber() + " is made available");
					seat.setSeatStatus(Seat.SEAT_STATUS.AVAILABLE);
				}
			}*/
			return null;
		}
	}

}
