/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

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
}
