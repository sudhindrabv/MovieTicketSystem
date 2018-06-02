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

 
