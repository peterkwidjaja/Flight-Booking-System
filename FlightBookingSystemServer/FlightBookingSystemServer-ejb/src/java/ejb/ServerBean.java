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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
        String date = departureTime.substring(6);
        for (Object o: l){ 
            ScheduleEntity temp = (ScheduleEntity) o;
            String dateTemp = temp.getDepartureTime().substring(6);
            if(date.equals(dateTemp)){
                return 2;
            }
        }
        return 1;
    }

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
        Query q = em.createQuery("SELECT r FROM Requests r WHERE r.status='UNREAD' OR r.status='PROCESSING'");
        List<Vector> requests = new ArrayList();
        for(Object o: q.getResultList()){
            RequestEntity r = (RequestEntity) o;
            Vector req = new Vector();
            req.add(r.getId());
            req.add(r.getTime().toString());
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

    @Override
    public List<Vector> searchSchedule(String departDate, String departCity, String arrivCity, int seats){
        Query q = em.createQuery("SELECT s FROM Schedules s WHERE s.flight.departureCity='"+departCity+"' AND s.availableSeats>="+seats);   //get flights that have more than required seats and correct departure city
        List list = q.getResultList();
        if(list.size()==0){
            return null;
        }
        List<Vector> result = new ArrayList<>();
        for(Object o: list){
            ScheduleEntity schedule = (ScheduleEntity) o;
            String flightDate = schedule.getDepartureTime().substring(6);
            System.out.println(flightDate+ " "+departDate);
            DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            
            if(flightDate.equals(departDate)){
                //Add schedule to result if the arrival city matches the requirement
                if(schedule.getFlight().getArrivalCity().equalsIgnoreCase(arrivCity)){
                    Vector v = new Vector();
                    v.add(false);
                    v.add(schedule.getId());
                    v.add(schedule.getFlight().getFlightNo());
                    v.add(schedule.getFlight().getDepartureCity());
                    v.add(schedule.getDepartureTime());
                    v.add(schedule.getFlight().getArrivalCity());
                    v.add(schedule.getArrivalTime());
                    v.add(schedule.getPrice());
                    result.add(v);
                }

                else{
                    String stop = schedule.getFlight().getArrivalCity();
                    Query q2 = em.createQuery("SELECT s FROM Schedules s WHERE s.flight.departureCity='"+stop+"' AND s.flight.arrivalCity ='"+arrivCity+"' AND s.availableSeats>="+seats);
                    for(Object obj: q2.getResultList()){
                        try{
                            ScheduleEntity transit = (ScheduleEntity) obj;
                            Calendar c1 = Calendar.getInstance();
                            Calendar c2 = Calendar.getInstance();
                            c1.setTime(formatter.parse(schedule.getArrivalTime()));
                            c2.setTime(formatter.parse(transit.getDepartureTime()));
                            long timeDist = c2.getTimeInMillis()-c1.getTimeInMillis();
                            //time distance between flights has at least 30 minutes and maximumof 6 hours
                            if(timeDist <= 21600000 && timeDist > 1800000){
                                Vector v = new Vector();
                                v.add(true);
                                v.add(schedule.getId());
                                v.add(schedule.getFlight().getFlightNo());
                                v.add(schedule.getFlight().getDepartureCity());
                                v.add(schedule.getDepartureTime());
                                v.add(schedule.getFlight().getArrivalCity());
                                v.add(schedule.getArrivalTime());
                                v.add(schedule.getPrice());
                                v.add(transit.getId());
                                v.add(transit.getFlight().getFlightNo());
                                v.add(transit.getFlight().getDepartureCity());
                                v.add(transit.getDepartureTime());
                                v.add(transit.getFlight().getArrivalCity());
                                v.add(transit.getArrivalTime());
                                v.add(transit.getPrice());
                                result.add(v);
                            }                        
                        }
                        catch(ParseException e){
                            System.err.println("Error at Search Schedule: "+e);
                        }

                    }
                }
            }
        }
        return result;
    }
    
    //Create a database entry of booking with username and updated schedules. Seats are processed.
    /*
    RETURN VALUE DESCRIPTION:
    
    */
    @Override
    public int createBooking(String username, int[] scheduleID, int seats, List<Vector> passengers) {
        BookingEntity newBooking = new BookingEntity();
        UserEntity user = em.find(UserEntity.class, username);
        newBooking.setOwner(user);
        for(int i=0; i<seats;i++){
            Vector v = passengers.get(i);
            PassengerEntity passenger = em.find(PassengerEntity.class, (String)v.get(1));
            if(passenger==null){
                passenger = new PassengerEntity();
                passenger.create((String)v.get(1), (String)v.get(0), (String)v.get(2), (String)v.get(3));
            }
            newBooking.getPassengers().add(passenger);
        }
        double price = 0;
        for(int i=0; i<scheduleID.length; i++){
            ScheduleEntity schedule = em.find(ScheduleEntity.class, scheduleID[i]);
            price += (schedule.getPrice()*seats);
            newBooking.getSchedules().add(schedule);
            schedule.setAvailableSeats(schedule.getAvailableSeats()-seats);
            if(!schedule.isHasBooking()){
                schedule.setHasBooking(true);
            }
        }
        newBooking.setTotalAmount(price);
        em.persist(newBooking);
        
        return 0;
    }

    @Override
    public List<Vector> getUnpaidBooking(String username) {
        Query q = em.createQuery("SELECT b from Bookings b where b.owner.username=:user AND b.payment IS NULL");
        q.setParameter("user", username);
        List list = q.getResultList();
        List<Vector> result = new ArrayList<Vector>();
        for(Object o: list){
            BookingEntity book = (BookingEntity) o;
            Vector v = new Vector();
            v.add(book.getId());
            v.add(book.getBookingTime());
            String dest = "";
            Set<ScheduleEntity> sch = book.getSchedules();
            for(ScheduleEntity s: sch){
                if(dest.equals("")){
                    dest = s.getFlight().getDepartureCity() + " - "+s.getFlight().getArrivalCity();
                }
                else{
                    dest = dest + " - " + s.getFlight().getArrivalCity();
                }
            }
            v.add(dest);
            v.add(book.getPassengers().size());
            v.add(book.getTotalAmount());
            result.add(v);
        }
        
        return result;
    }

    @Override
    public void makePayment(int bookingID, String cardType, long cardNo, String name) {
        BookingEntity booking = em.find(BookingEntity.class, bookingID);
        PaymentEntity payment = new PaymentEntity();
        payment.create(cardType, cardNo, name);
        booking.setPayment(payment);
        em.persist(payment);
    }

    @Override
    public List<Vector> getBookings(String username) {
        Query q = em.createQuery("SELECT b FROM Bookings b WHERE b.owner.username=:user");
        q.setParameter("user", username);
        List<Vector> bookings = new ArrayList();
        for(Object o: q.getResultList()){
            BookingEntity b = (BookingEntity) o;
            List<Vector> schedules = new ArrayList();
            for(Object s: b.getSchedules()){
                ScheduleEntity schedule = (ScheduleEntity) s;
                Vector sch = new Vector();
                sch.add(schedule.getFlight().getFlightNo());
                sch.add(schedule.getFlight().getDepartureCity()+" "+schedule.getDepartureTime());
                sch.add(schedule.getFlight().getArrivalCity()+" "+schedule.getArrivalTime());
                schedules.add(sch);
            }
            List<Vector> passengers = new ArrayList();
            for(Object p: b.getPassengers()){
                PassengerEntity passenger = (PassengerEntity) p;
                Vector psg = new Vector();
                psg.add(passenger.getName());
                psg.add(passenger.getPassportNo());
                psg.add(passenger.getGender());
                psg.add(passenger.getDob());
                passengers.add(psg);
            }
            Vector bookingDetails = new Vector();
            bookingDetails.add(b.getId());
            bookingDetails.add(b.getBookingTime());
            bookingDetails.add(schedules);
            bookingDetails.add(passengers);  
            bookingDetails.add(b.getTotalAmount());
            PaymentEntity payment = b.getPayment();
            if(payment==null){
                bookingDetails.add(false);
            }
            else{
                bookingDetails.add(true);
            }

            bookings.add(bookingDetails);
        }
        return bookings;
    }

    @Override
    public List<Vector> viewUserRequests(String username) {
        Query q = em.createQuery("SELECT r FROM Requests r WHERE r.owner.username=:user ORDER BY r.reqTime");
        q.setParameter("user", username);
        List<Vector> result = new ArrayList<Vector>();
        for(Object o: q.getResultList()){
            RequestEntity r = (RequestEntity) o;
            Vector v = new Vector();
            v.add(r.getId());
            v.add(r.getTime().toString());
            v.add(r.getContent());
            v.add(r.getStatus());
            v.add(r.getComment());
            result.add(v);
        }
        return result;
    }
    
    
    
}
