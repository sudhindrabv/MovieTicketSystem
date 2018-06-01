/**
 * 
 */
package com.walmart.ticketsystem.service;

import java.util.List;

import com.walmart.ticketsystem.entity.MovieTheater;
import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.SeatHold;

/**
 * This provides interface to handle all functionalities to MovieTheater 
 * @author Sudhindra
 *
 */
public interface MovieTheaterService {
	MovieTheater movieTheater = MovieTheater.getMovieTheaterInstance();
	
	public static SeatHold findSeatHoldByIdAndEmail(int seatHoldId,String cutomerEmaild) {
		List<SeatHold> seatHoldList = movieTheater.getAllSeatHold();
		for (SeatHold seatHold : seatHoldList) {
			if (seatHold.getSeatHoldId() == seatHoldId && seatHold.getCustomerEmail().equals(cutomerEmaild)) {
				return seatHold;
			}
		}
		return null;
	}
	
	public static int[] getAvailableSeatsPerRow() {
		return movieTheater.getAvailableSeatsPerRow();
	}
	
	public static Seat[][] getSeats(){
		return movieTheater.getSeats();
	}
	
	public static void setAvailableSeatsPerRow(int rowNumber, int numSeats) {
		movieTheater.setAvailableSeatsPerRow(rowNumber, numSeats);
	}
	
	public static void addToSeatHoldList(SeatHold seatHold) {
		movieTheater.addToSeatHoldList(seatHold);
	}
	
	public static void  prettyPrintMovieTicket() {
		movieTheater.prettyPrintMovieTicket();
	}
}
