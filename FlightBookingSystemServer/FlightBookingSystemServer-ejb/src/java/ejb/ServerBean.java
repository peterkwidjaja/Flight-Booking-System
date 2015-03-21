/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.FlightEntity;
import entity.ScheduleEntity;
import entity.UserEntity;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Peter K W
 */
@Stateless
public class ServerBean implements ServerBeanRemote {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public boolean addUser(String username, String password, int contactNo, String email) {
        UserEntity temp = em.find(UserEntity.class, username);
        if(temp==null){
            UserEntity newUser = new UserEntity();
            newUser.create(username, password, contactNo, email);
            em.persist(newUser);
            return true;
        }
        return false;
    }

    @Override
    public int delUser(String username) {
        UserEntity temp = em.find(UserEntity.class, username);
        if(temp!=null){
            if(!temp.getBookings().isEmpty())
                return 3;
            em.remove(temp);
            return 1;
        }
        return 2;
    }

    @Override
    public boolean addFlight(String flightNo, String departureCity, String arrivalCity, String aircraftType, int totalSeats) {
        FlightEntity temp = em.find(FlightEntity.class, flightNo);
        if(temp==null){
            temp = new FlightEntity();
            temp.create(flightNo, departureCity, arrivalCity, aircraftType, totalSeats);
            em.persist(temp);
            return true;
        }
        return false;
    }

    //Return Value:
    //0 --> flight not found
    //1 --> flight has no schedule
    //2 --> flight has schedule
    @Override
    public int searchFlight(String flightNo) {
        FlightEntity temp = em.find(FlightEntity.class, flightNo);
        if(temp==null){
            return 0;
        }
        if(temp.getSchedule()==null){
            return 1;
        }
        return 2;
    }

    @Override
    public void updateFlightAll(String flightNo, String departure, String arrival, String aircraftType, int totalSeats) {
        FlightEntity temp = em.find(FlightEntity.class, flightNo);
        temp.setDepartureCity(departure);
        temp.setArrivalCity(arrival);
        temp.setAircraftType(aircraftType);
        temp.setTotalSeats(totalSeats);
        em.flush();
    }

    @Override
    public boolean updateFlightSchedule(String flightNo, String aircraftType, int totalSeats) {
        FlightEntity temp = em.find(FlightEntity.class, flightNo);
        ArrayList<ScheduleEntity> schedules = (ArrayList)temp.getSchedule();
        if(totalSeats<temp.getTotalSeats()){
            for (ScheduleEntity schedule : schedules) {
                int bookedSeats = temp.getTotalSeats() - schedule.getAvailableSeats();
                if(bookedSeats>totalSeats){
                    return false;
                }
            }
        }
        int addition = totalSeats-temp.getTotalSeats();
        temp.setAircraftType(aircraftType);
        temp.setTotalSeats(totalSeats);
        for (ScheduleEntity schedule : schedules) {
            schedule.setAvailableSeats(schedule.getAvailableSeats()+addition);
        }
        em.merge(temp); //To find out
        return true;
    }

    @Override
    public int deleteFlight(String flightNo) {
        FlightEntity temp = em.find(FlightEntity.class, flightNo);
        if(temp==null){
            return 0;
        }
        if(!temp.getSchedule().isEmpty()){
            return 2;
        }
        em.remove(temp);
        return 1;
    }

    @Override
    public void addSchedule(String flightNo, String departureTime, String arrivalTime, double price) {
        ScheduleEntity newSchedule = new ScheduleEntity();
        newSchedule.create(new Date(toDate(departureTime)), new Date(toDate(arrivalTime)), price);
        FlightEntity temp = em.find(FlightEntity.class, flightNo);
        newSchedule.setFlight(temp);
        newSchedule.setAvailableSeats(temp.getTotalSeats());
        em.persist(newSchedule);
    }

    @Override
    public int checkSchedule(String flightNo, String departureTime) {
        if(em.find(FlightEntity.class, flightNo)==null)
            return 0;
        Query q = em.createQuery("SELECT s FROM Schedules s WHERE s.flight.flightNo='"+flightNo+"'");
        List l = q.getResultList();
        Calendar newCal = Calendar.getInstance();
        newCal.setTimeInMillis(toDate(departureTime));
        for (Object o: l){
            ScheduleEntity temp = (ScheduleEntity) o;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(temp.getDepartureTime().getTime());
            if(c.get(Calendar.YEAR)==newCal.get(Calendar.YEAR) && c.get(Calendar.MONTH)==newCal.get(Calendar.MONTH) && c.get(Calendar.DAY_OF_MONTH)==newCal.get(Calendar.DAY_OF_MONTH)){
                return 2;
            }
        }
        return 1;
    }
    private long toDate(String input){
        try{
            DateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            return format.parse(input).getTime();
        }
        catch(ParseException e){
            return -1;
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
