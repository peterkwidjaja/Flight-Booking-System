/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.BookingEntity;
import entity.FlightEntity;
import entity.PassengerEntity;
import entity.PaymentEntity;
import entity.RequestEntity;
import entity.ScheduleEntity;
import entity.UserEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
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
        em.merge(temp);
        em.flush();
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
        newSchedule.create(departureTime, arrivalTime, price);
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
        Calendar newCal = getDate(departureTime);
        String date = departureTime.substring(6);
        for (Object o: l){
            ScheduleEntity temp = (ScheduleEntity) o;
            String dateTemp = temp.getDepartureTime().substring(6);
            //Calendar c = getDate(temp.getDepartureTime());
            //if(c.get(Calendar.YEAR)==newCal.get(Calendar.YEAR) && c.get(Calendar.MONTH)==newCal.get(Calendar.MONTH) && c.get(Calendar.DAY_OF_MONTH)==newCal.get(Calendar.DAY_OF_MONTH)){
            if(date.equals(dateTemp)){
                return 2;
            }
        }
        return 1;
    }
    private Calendar getDate(String input){
        try{
            DateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(format.parse(input).getTime());
            return cal;
        }
        catch(ParseException e){
            System.err.println(e);
            return null;
        }
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private ScheduleEntity findSchedule(String flightNo, String departureDate){
        Query q = em.createQuery("SELECT s FROM Schedules s WHERE s.flight.flightNo='"+flightNo+"'");
        List l = q.getResultList();
        for(Object o: l){
            ScheduleEntity temp = (ScheduleEntity) o;
            String flightDate = temp.getDepartureTime().substring(6);
            if(flightDate.equals(departureDate)){
                return temp;
            }
        }
        return null;
    }
    @Override
    public void updateScheduleAll(String flightNo, String departure, String newDeparture, String newArrival, double price) {
        ScheduleEntity temp = findSchedule(flightNo, departure);
        temp.setDepartureTime(newDeparture);
        temp.setArrivalTime(newArrival);
        temp.setPrice(price);
        em.merge(temp);
        em.flush();
    }

    @Override
    public int checkScheduleBooking(String flightNo, String departure) {
        /*
        0-->schedule not found
        1-->true
        2-->schedule got booking (only can change price)
        */
        Query q = em.createQuery("SELECT s FROM Schedules s WHERE s.flight.flightNo='"+flightNo+"'");
        List l = q.getResultList();
        for(Object o: l){
            ScheduleEntity temp = (ScheduleEntity) o;
            String date = temp.getDepartureTime().substring(6);
            if(date.equals(departure)){
                if(temp.isHasBooking()){
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void updateScheduleBooking(String flightNo, String departure, double newPrice) {
        ScheduleEntity temp = findSchedule(flightNo, departure);
        temp.setPrice(newPrice);
        em.merge(temp);
        em.flush();
    }

    @Override
    public int deleteSchedule(String flightNo, String departure) {
        ScheduleEntity temp = findSchedule(flightNo, departure);
        if(temp == null){
            return 0;
        }
        if(temp.isHasBooking()){
            return 2;
        }
        em.remove(temp);
        return 1;
    }

    @Override
    public List<Vector> viewBookings() {
        Query q = em.createQuery("SELECT b FROM Bookings b");
        List<Vector> bookings = new ArrayList();
        for(Object o: q.getResultList()){
            BookingEntity b = (BookingEntity) o;
            List<Vector> schedules = new ArrayList();
            for(Object s: b.getSchedules()){
                ScheduleEntity schedule = (ScheduleEntity) s;
                Vector sch = new Vector();
                sch.add(schedule.getFlight().getFlightNo());
                sch.add(schedule.getDepartureTime());
                schedules.add(sch);
            }
            List<Vector> passengers = new ArrayList();
            for(Object p: b.getPassengers()){
                PassengerEntity passenger = (PassengerEntity) p;
                Vector psg = new Vector();
                psg.add(passenger.getPassportNo());
                psg.add(passenger.getName());
                psg.add(passenger.getGender());
                psg.add(passenger.getDob());
                passengers.add(psg);
            }
            Vector bookingDetails = new Vector();
            bookingDetails.add(b.getId());
            bookingDetails.add(b.getBookingTime());
            UserEntity users = b.getOwner();
            bookingDetails.add(users.getUsername());
            bookingDetails.add(users.getContactNo());
            bookingDetails.add(users.getEmail());
            Vector paymentDetails = new Vector();
            PaymentEntity payment = b.getPayment();
            paymentDetails.add(b.getTotalAmount());
            if(payment==null){
                paymentDetails.add(false);
            }
            else{
                paymentDetails.add(true);
                paymentDetails.add(payment.getPaymentTime());
                paymentDetails.add(payment.getCardType());
                paymentDetails.add(payment.getCardNo());
                paymentDetails.add(payment.getCardHolderName());
            }
            bookingDetails.add(schedules);
            bookingDetails.add(passengers);
            bookingDetails.add(paymentDetails);
            bookings.add(bookingDetails);
        }
        return bookings;
    }

    @Override
    public List<Vector> viewSchedules() {
        Query q = em.createQuery("SELECT s FROM Schedules s");
        List<Vector> schedules = new ArrayList();
        for(Object o: q.getResultList()){
            ScheduleEntity s = (ScheduleEntity) o;
            Vector schedule = new Vector();
            schedule.add(s.getFlight().getFlightNo());
            schedule.add(s.getDepartureTime());
            schedule.add(s.getArrivalTime());
            schedule.add(s.getAvailableSeats());
            schedule.add(s.getPrice());
            schedules.add(schedule);
        }
        return schedules;
    }

    @Override
    public List<Vector> viewFlights() {
        Query q = em.createQuery("SELECT f FROM Flights f");
        List<Vector> flights = new ArrayList();
        for(Object o: q.getResultList()){
            FlightEntity f = (FlightEntity) o;
            Vector flight = new Vector();
            flight.add(f.getFlightNo());
            flight.add(f.getDepartureCity());
            flight.add(f.getArrivalCity());
            flight.add(f.getAircraftType());
            flight.add(f.getTotalSeats());
            flights.add(flight);
        }
        return flights;
    }

    @Override
    public List<Vector> viewRequests() {
        Query q = em.createQuery("SELECT r FROM Requests r WHERE r.status='Unread' OR r.status='Processing'");
        List<Vector> requests = new ArrayList();
        for(Object o: q.getResultList()){
            RequestEntity r = (RequestEntity) o;
            Vector req = new Vector();
            req.add(r.getId());
            req.add(r.getTime());
            req.add(r.getOwner().getUsername());
            req.add(r.getContent());
            
            requests.add(req);
        }
        return requests;
    }

    @Override
    public boolean processRequest(int id, String status, String comment) {
        RequestEntity temp = em.find(RequestEntity.class, id);
        if(temp==null){
            return false;
        }
        temp.setStatus(status);
        temp.setComment(comment);
        return true;
    }

    @Override
    public boolean login(String username, String password) {
        UserEntity temp = em.find(UserEntity.class, username);
        if(temp==null){
            return false;
        }
        if(temp.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    @Override
    public List getUserInfo(String username) {
        UserEntity temp = em.find(UserEntity.class, username);
        if(temp!=null){
            List l = new ArrayList();
            l.add(temp.getUsername());
            l.add(temp.getContactNo());
            l.add(temp.getEmail());
            return l;
        }
        return null;
    }

    @Override
    public void changeUserPass(String username, String newPassword) {
        UserEntity temp = em.find(UserEntity.class, username);
        temp.setPassword(newPassword);
        em.merge(temp);
        em.flush();
    }

    @Override
    public void changeUserDetails(String username, int contactNo, String email) {
        UserEntity temp = em.find(UserEntity.class, username);
        temp.setContactNo(contactNo);
        temp.setEmail(email);
        em.merge(temp);
        em.flush();
    }
    
}
