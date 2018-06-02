package com.walmart.ticketsystem;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.walmart.ticketsystem.constants.Constants;
import com.walmart.ticketsystem.entity.SeatHold;
import com.walmart.ticketsystem.exception.EmailValidationException;
import com.walmart.ticketsystem.service.TicketService;
import com.walmart.ticketsystem.service.TicketServiceImpl;
import com.walmart.ticketsystem.service.ValidationService;
import com.walmart.ticketsystem.service.ValidationServiceImpl;
import com.walmart.ticketsystem.utils.Utility;

/**
 * Driver Class to start the Application
 * 
 * @author Sudhindra
 *
 */
@SpringBootApplication
public class TicketSystemApplication {

	private static ScheduledExecutorService scheduledExecuter = Executors.newScheduledThreadPool(10);

	public static void main(String[] args) {
		TicketService service = new TicketServiceImpl();

		Scanner sc = new Scanner(System.in);
		System.out.println(" TicketService System");
		System.out.println("*********************");
		boolean loop = true;
		String options = "\nOptions:\n1. start/Clear \t2. Available Seats \t3. Request for Hold \t4. Reserve/commit \t5. Exit.";
		while (loop) {
			System.out.println(options);
			String str = sc.next();
			boolean isvalidInput = Utility.isValidNumber(str);
			if (!isvalidInput) {
				System.out.println("Please enter valid numbers.");
				continue;
			}
			int input = Integer.parseInt(str);
			switch (input) {
			case 1:
				System.out.println("How many rows in the movie theater?");
				String numRows = sc.next();
				boolean isvalidRow = Utility.isValidNumber(numRows);
				if(!isvalidRow){
					while(!isvalidRow){
						System.out.println("Invalid Row.");
						System.out.println("Enter positive number of rows");
						numRows = sc.next();
						isvalidRow = Utility.isValidNumber(numRows);
					}
				}
				
				System.out.println("How many seats/row in the movie theater?");
				String clsPerRow = sc.next();
				boolean isValidColsPerRow = Utility.isValidNumber(clsPerRow);
				if(!isValidColsPerRow){
					while(!isValidColsPerRow){
						System.out.println("Invalid seats/row/nEnter positive number of seats/row.");
						clsPerRow = sc.next();
						isValidColsPerRow = Utility.isValidNumber(clsPerRow);
					}
				}
				if (Utility.isValidRowAndColumn(sc, numRows, clsPerRow)) {
					Constants.MAX_ROWS = Integer.parseInt(numRows);
					Constants.MAX_SEATS_PER_ROW = Integer.parseInt(clsPerRow);
					System.out.println("Set Ticket Hold Expiration Minutes");
					System.out.println("Hit enter to skip)");
					try {
						String duration  = sc.next();
						if(!duration.isEmpty()){
							Constants.SEAT_HOLD_DURATION_EXPIRY = Integer.parseInt(duration);
						}else {
							System.out.println("Invalid format, considering default one minute expiry");
						}
					} catch (NumberFormatException e) {
						System.out.println("Invalid format, considering default one minute expiry");
					}
				}
				//System.gc();
				break;

			case 2:
				System.out.println("\nNo of seats available now: " + service.numSeatsAvailable());
				break;

			case 3:
				System.out.println("How many seats for hold?");
				String xs = sc.next();
				boolean isvalidSeat = Utility.isValidNumber(xs);
				if (!isvalidSeat) {
					while (!isvalidSeat) {
						System.out.println("Invalid seat no.");
						System.out.println("Enter valid no:");
						xs = sc.next();
						isvalidSeat = Utility.isValidNumber(xs);
					}
				}
				int seats = Integer.parseInt(xs);
				System.out.println("Customer email?");
				String email = sc.next();
				boolean isvalid = false;
				try{
					isvalid = ValidationServiceImpl.getValidationServiceInstance().validateCustomerEmaildId(email);
				}catch(EmailValidationException ex) {
					System.out.println(ex.getMsg());
				}
				if (!isvalid) {
					while (!isvalid) {
						System.out.println("Invalid email pattern.");
						System.out.println("Enter valid email:");
						email = sc.next();
						try {
							isvalid = ValidationServiceImpl.getValidationServiceInstance().validateCustomerEmaildId(email);
						}catch (EmailValidationException e) {
							System.out.println(e.getMsg());
						}
					}
				}
				SeatHold hold = service.findAndHoldSeats(seats, email);
				if (hold != null) {
					System.out.println("\n" + seats + " held!\n" + hold);
				} else {
					System.out.println("\nHolding seats Failed. Pleae try again!");
				}
				break;

			case 4:
				System.out.println("SeatHold Id?");
				String seatHoldId = sc.next();
				boolean isvalidDigit = Utility.isValidNumber(seatHoldId);
				if (!isvalidDigit) {
					while (!isvalidDigit) {
						System.out.println("Invalid no pattern.");
						System.out.println("Enter valid no:");
						seatHoldId = sc.next();
						isvalidDigit = Utility.isValidNumber(seatHoldId);
					}
				}
				int id = Integer.parseInt(seatHoldId);
				System.out.println("Associated with which customer email?");
				String cust = sc.next();
				boolean validateCustomerEmaildId = false;
				try{
					validateCustomerEmaildId = ValidationServiceImpl.getValidationServiceInstance().validateCustomerEmaildId(cust);
				}catch (EmailValidationException e) {
					System.out.println(e.getMsg());
				}
						
				if (!validateCustomerEmaildId) {
					while (!validateCustomerEmaildId) {
						System.out.println("Invalid email pattern.");
						System.out.println("Enter valid email:");
						cust = sc.next();
						try{
							validateCustomerEmaildId = ValidationServiceImpl.getValidationServiceInstance().validateCustomerEmaildId(cust);
						}catch (EmailValidationException e) {
							System.out.println(e.getMsg());
						}
					}
				}
				System.out.println("\n" + service.reserveSeats(id, cust));
				break;

			case 5:
				loop = false;
				System.out.println("\nExiting the Ticket System!");
				scheduledExecuter.shutdownNow();
				break;
			default:
				System.out.println("Invalid option.");
			}
		}
		sc.close();

		/*
		 * service.numSeatsAvailable(); service.findAndHoldSeats(3, "1");
		 * service.findAndHoldSeats(4, "pqr@gmail.com"); service.findAndHoldSeats(3,
		 * "xyz@gmail.com"); service.findAndHoldSeats(3, "123@gmail.com");
		 * service.reserveSeats(1, "1"); service.numSeatsAvailable(); int holdId1 = 1;
		 * int holdId2 = 2; try { System.out.println("\nSleeping for 1 mins");
		 * Thread.sleep(1*30*1000);
		 * System.out.println("Thread Awake and continue reserving seats");
		 * service.reserveSeats(holdId1, "pqr@gmail.com"); service.reserveSeats(holdId2,
		 * "xyz@gmail.com"); } catch (InterruptedException e) { e.printStackTrace(); }
		 */
	}

	public static ScheduledExecutorService getScheduledExecuterInstance() {
		return scheduledExecuter;
	}
}
