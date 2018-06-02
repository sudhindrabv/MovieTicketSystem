# WalmartTicketSystem
Implementing a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

# Requirements
  1.  Find the number of seats available within the venue
  Note: available seats are seats that are neither held nor reserved.
  2.  Find and hold the best available seats on behalf of a customer
  Note: each ticket hold should expire within a set number of seconds.
  3.  Reserve and commit a specific group of held seats for a customer
  4.  The ticket service implementation should be written in Java
  5.  The solution and tests should build and execute entirely via the command line using either Maven or Gradle as the build tool
  6.  A README file should be included in your submission that documents your assumptions and includes instructions for building the solution and executing the tests
  7.  Implementation mechanisms such as disk-based storage, a REST API, and a front-end GUI are not required
  8.  For full Problem statement refer **Ticket Service Coding Challenge.pdf**

# Assumptions
  * Customers can book tickets based on the availablity of the seats.
  * Customers are given seats from top row from left to right considering it to be the best available seats and least best seats are the bottom row righmost seat.
  * Best available seats also means that the seats are contiguous and if the seats are not available contiguous then the I am     considering user will not wish to choose the seats(Although a small change in TicketServiceImpl is enough to handle this)
  * Seats are held only for a small duration of time(1 minute) after which seats availability will change from Hold to Available and customer has to send a request again to hold the seats.(Hold Expiry Duration can be customised in Constants file)
  * After the duration is expired no notification will be sent to customer.
  
# Installations Details
  ##  Basic Setup
  Assuming Java and Git are already installed on device
  * git clone https://github.com/sudhindramanohar/WalmartTicketSystem.git
  * cd WalmartTicketSystem

  ## Maven Commands:
  Assuming you are at the directory where pom.xml is present. Open command prompt type
  * mvn clean
  * mvn clean package assembly:assembly OR mvn clean package assembly:assembly -DskipTests (To skip the test cases) // This will generate   jar(*-jar-with-dependenciees.jar) file under target folder.
  * java -jar target\WalmartTicketSystem-0.0.1-SNAPSHOT.jar com.walmart.ticketsystem.TicketSystemApplication //Main Program will start. 
  * Then follow the command prompt.
  

# Class Diagram
![Class Diagram](https://github.com/sudhindramanohar/WalmartTicketSystem/blob/master/Class%20Diagram.jpg)

# Design Overview
I have considered three main entities
  1.  **MovieTheater**   
    * This entity is used to store information about all the two dimentional array of Seat object and **a one dimensional array of available seats per row. Because, of this datastructure it reduces the running time complexity to get number of seats available at any instant from O(n*m) to O(n)** where n is number of rows and m is number of columns.
    * It also holds the information about all the SeatHold object at any given moment.
      
  2.  **SeatHold**
    * This entity a uniquely generated SeatHoldId, customer email, and list of Seat held by the customer and a flag to check whether held seats are expired or not.
    * This object is is destroyed once the hold duration has lapsed.
      
  3.  **Seat**
    * This object is used to capture the seatNumber, rowNumber and seat status.
    * Seat Status is an enumerator which can have any value from "AVAILABLE, HOLD, RESERVED".
    * Initially all the seats are initialzed with AVAILABLE status and is changed to HOLD when customer holds seats and when the held seats are booked it is changed to RESERVATION status and no customers can book the RESERVED seats.

Utility classes:
  1.  **ConfirmationCodeGenerator**
    * This class is used to generate UUID when the seats are booked by a customer successfully.
    * This is a generated uniquely accross all threads.
  
  2.  **HoldIdGenerator**
    * This class is used to generate atomic integer value when seats are held by a customer successfully.
    * This generated uniquely across all threads.


Custom Exception Handler classes:
  1.  **EmailValidationException**
    * Currently I have created one class to handle to throw an custom exception when invalid email is provided
    * This class extends RuntimeExcption.
    
Service classes:
I have added two main service implementation classes.
  1.  **TicketServiceImpl**
    * This class provides the implemenattion of all the three API's.
    * It internally MovieTheater interface(to do all Seat related operation), TicketHoldRunnableTask class(To make seats available once timer expires)
  2.  **ValidationServiceImpl**
    * This class provides implementation to varies validations like customer email validation and handling custom exceptions. 
