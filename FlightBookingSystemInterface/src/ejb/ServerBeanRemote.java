/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.List;
import java.util.Vector;
import javax.ejb.Remote;

/**
 *
 * @author Peter K W
 */
@Remote
public interface ServerBeanRemote {
    public boolean addUser(String username, String password,int contactNo, String email);

    public int delUser(String username);

    public boolean addFlight(String flightNo, String departureCity, String arrivalCity, String aircraftType, int totalSeats);

    public int searchFlight(String flightNo);

    public void updateFlightAll(String flightNo, String departure, String arrival, String aircraftType, int totalSeats);

    public boolean updateFlightSchedule(String flightNo, String aircraftType, int totalSeats);

    public int deleteFlight(String flightNo);

    void addSchedule(String flightNo, String departureTime, String arrivalTime, double price);

    int checkSchedule(String flightNo, String departureTime);

    void updateScheduleAll(String flightNo, String departure, String newDeparture, String newArrival, double price);

    int checkScheduleBooking(String flightNo, String departure);

    void updateScheduleBooking(String flightNo, String departure, double newPrice);

    int deleteSchedule(String flightNo, String departure);

    List<Vector> viewBookings();

    List<Vector> viewSchedules();

    List<Vector> viewFlights();

    List<Vector> viewRequests();

    boolean processRequest(int id, String status, String comment);

    boolean login(String username, String password);

    List getUserInfo(String username);

    void changeUserPass(String username, String newPassword);

    void changeUserDetails(String username, int contactNo, String email);
}
