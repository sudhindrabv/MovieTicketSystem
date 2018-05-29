package com.walmart.ticketsystem.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.walmart.ticketsystem.constants.Constants;

public class MovieTheater {
	private Seat[][] seats;
	private int[] availableSeatsPerRow;
	private List<SeatHold> allSeatHold;
	// private int[] maxConsecutiveSeatsPerRow;

	//****************Singleton Design Pattern*****************************
	// static variable moviewTheaterInstance of type MovieTheater
	private static MovieTheater moviewTheaterInstance = null;

	// static method to create instance of Singleton MovieTheater class
	public static MovieTheater getMovieTheaterInstance() {
		if (moviewTheaterInstance == null) {
			synchronized (MovieTheater.class) {
				if (moviewTheaterInstance == null) {
					moviewTheaterInstance = new MovieTheater();
				}
			}
		}
		return moviewTheaterInstance;
	}

	public MovieTheater() {
		super();
		init();
	}

	private void init() {
		seats = new Seat[Constants.MAX_ROWS][Constants.MAX_SEATS_PER_ROW];
		availableSeatsPerRow = new int[Constants.MAX_ROWS];
		for (int i = 0; i < Constants.MAX_ROWS; i++) {
			for (int j = 0; j < Constants.MAX_SEATS_PER_ROW; j++) {
				availableSeatsPerRow[i] = j + 1;
				seats[i][j] = new Seat(i, Integer.valueOf(String.valueOf(i) + String.valueOf(j)));
			}
		}
	}

	public Seat[][] getSeats() {
		return seats;
	}

	public void setSeats(Seat[][] seats) {
		this.seats = seats;
	}

	public int[] getAvailableSeatsPerRow() {
		return availableSeatsPerRow;
	}

	public synchronized void setAvailableSeatsPerRow(int[] availableSeatsPerRow) {
		this.availableSeatsPerRow = availableSeatsPerRow;
	}

	public void setAvailableSeatsPerRow(int row, int holdedSeats) {
		int[] availableSeatsPerRow = getAvailableSeatsPerRow();
		for (int rowNumber = availableSeatsPerRow.length - 1; rowNumber >= 0; rowNumber--) {
			if (rowNumber == row) {
				availableSeatsPerRow[row] -= holdedSeats;
				break;
			}
		}
		setAvailableSeatsPerRow(availableSeatsPerRow);
	}

	public List<SeatHold> getAllSeatHold() {
		return (allSeatHold==null)?new LinkedList<SeatHold>() : allSeatHold;
	}

	public void addToSeatHoldList(SeatHold seatHold) {
		List<SeatHold> seatHoldList = getAllSeatHold();
		seatHoldList.add(seatHold);
		this.allSeatHold = seatHoldList;
	}

	public SeatHold findSeatHoldById(int seatHoldId) {
		List<SeatHold> seatHoldList = getAllSeatHold();
		for (SeatHold seatHold : seatHoldList) {
			if (seatHold.getSeatHoldId() == seatHoldId) {
				return seatHold;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "MovieTheatre [seats=" + Arrays.toString(seats) + ", availableSeatsPerRow="
				+ Arrays.toString(availableSeatsPerRow) + "]";
	}

}
