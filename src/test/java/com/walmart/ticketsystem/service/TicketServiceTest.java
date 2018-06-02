package com.walmart.ticketsystem.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.walmart.ticketsystem.constants.Constants;
import com.walmart.ticketsystem.entity.Seat;
import com.walmart.ticketsystem.entity.SeatHold;

public class TicketServiceTest {

	private TicketService ticketService;

	@Before
	public void before() {
		ticketService = new TicketServiceImpl();
	}

	@Test
	public void testFindAndHoldSeats() {
        int seatsRequired = 1;
        Constants.MAX_ROWS = 3;
		Constants.MAX_SEATS_PER_ROW = 6;
		String customerEmail = "test@email.com";
		SeatHold sh = new SeatHold(customerEmail);
		List<Seat> seats = new ArrayList<Seat>();
		seats.add(new Seat(2,20));
		sh.setHoldSeats(seats);
        assertNotEquals(sh,ticketService.findAndHoldSeats(seatsRequired,customerEmail));
    }
	
	@Test
	public void testNumSeatsAvailable() {
		Constants.MAX_ROWS = 3;
		Constants.MAX_SEATS_PER_ROW = 6;
		assertNotEquals(10,ticketService.numSeatsAvailable());
	}
	
	@Test
	public void testNoRowsNowCols() {
		ticketService.numSeatsAvailable();
		Constants.MAX_ROWS = 0;
		Constants.MAX_SEATS_PER_ROW = 0;
		assertNotEquals(0,ticketService.numSeatsAvailable());
	}
	
	@Test
	public void testFindAndHoldSeatsWithInputSeatsGreaterThanAvailableSeats() {
        int seatsRequired = 100;
        Constants.MAX_ROWS = 3;
		Constants.MAX_SEATS_PER_ROW = 6;
        assertEquals(null,ticketService.findAndHoldSeats(seatsRequired,"test@email.com"));
    }
	
}
