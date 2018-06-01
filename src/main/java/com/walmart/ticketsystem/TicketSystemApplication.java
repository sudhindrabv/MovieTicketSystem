package com.walmart.ticketsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.walmart.ticketsystem.service.TicketService;
import com.walmart.ticketsystem.service.TicketServiceImpl;

/**
 * Driver Class to start the Application
 * @author Sudhindra
 *
 */
public class TicketSystemApplication {

	private static ScheduledExecutorService scheduledExecuter = Executors.newScheduledThreadPool(10);
	
	public static void main(String[] args) {
		TicketService service = new TicketServiceImpl();
		service.numSeatsAvailable();
		service.findAndHoldSeats(3, "1");
		service.findAndHoldSeats(4, "pqr@gmail.com");
		service.findAndHoldSeats(3, "xyz@gmail.com");
		service.findAndHoldSeats(3, "123@gmail.com");
		service.reserveSeats(1, "1");
		service.numSeatsAvailable();
		int holdId1 = 1;
		int holdId2 = 2;
		try {
			System.out.println("\nSleeping for 1 mins");
			Thread.sleep(1*60*1000);
			System.out.println("Thread Awake and continue reserving seats");
			service.reserveSeats(holdId1, "pqr@gmail.com");
			service.reserveSeats(holdId2, "xyz@gmail.com");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static ScheduledExecutorService getScheduledExecuterInstance() {
		return scheduledExecuter;
	}
}
