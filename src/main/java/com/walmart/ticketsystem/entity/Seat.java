package com.walmart.ticketsystem.entity;

/**
 * Pojo class used to store Seat related information 
 * @author sudhi
 *
 */
public class Seat {

	private int seatNumber;
	private int rowNumber;
	private Seat.SEAT_STATUS seatStatus;

	public enum SEAT_STATUS {
		AVAILABLE, HOLD, RESERVED;
	}

	public Seat(int rowNumber, int seatNumber) {
		this.seatStatus = SEAT_STATUS.AVAILABLE;
		this.rowNumber = rowNumber;
		this.seatNumber = seatNumber;
	}

	public Seat.SEAT_STATUS getSeatStatus() {
		return seatStatus;
	}

	public synchronized void setSeatStatus(Seat.SEAT_STATUS seatStatus) {
		this.seatStatus = seatStatus;
	}

	public int getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(int seatNumber) {
		this.seatNumber = seatNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

}
