package com.walmart.ticketsystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.walmart.ticketsystem.service.TicketService;
import com.walmart.ticketsystem.service.TicketServiceImpl;

@SpringBootApplication
public class TicketSystemApplication {

	private static ScheduledExecutorService scheduledExecuter = Executors.newScheduledThreadPool(10);
	
	public static void main(String[] args) {
		//SpringApplication.run(TicketSystemApplication.class, args);
		TicketService service = new TicketServiceImpl();
		service.numSeatsAvailable();
		service.findAndHoldSeats(3, "1");
		service.findAndHoldSeats(4, "pqr@gmail.com");
		service.findAndHoldSeats(3, "xyz@gmail.com");
		service.findAndHoldSeats(18, "123@gmail.com");
		service.reserveSeats(1, "1");
		service.numSeatsAvailable();
		int uid = 2;
		try {
			System.out.println("\nSleeping for 2 mins");
			Thread.sleep(2*60*1000);
			System.out.println("Thread Awake and continue reserving seats");
			service.reserveSeats(uid, "pqr@gmail.com");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static ScheduledExecutorService getScheduledExecuterInstance() {
		return scheduledExecuter;
	}
}
