/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 *
 * @author Peter
 */
@Entity(name="Bookings")
public class BookingEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String bookingTime;
    private double totalAmount;
    
    @ManyToOne
    private UserEntity owner = new UserEntity();
    @OneToOne(cascade={CascadeType.PERSIST})
    private PaymentEntity payment;
    @ManyToMany(cascade={CascadeType.PERSIST})
    @JoinTable(name="BOOKINGMMBI_PASSENGERMMBI")
    private Set<PassengerEntity> passengers = new HashSet<PassengerEntity>();
    @ManyToMany(cascade={CascadeType.MERGE})
    @JoinTable(name="BOOKINGMMBI_SCHEDULEMMBI")
    private Set<ScheduleEntity> schedules = new HashSet<ScheduleEntity>();
    
    public BookingEntity(){
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        this.bookingTime = formatter.format(now);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BookingEntity)) {
            return false;
        }
        BookingEntity other = (BookingEntity) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Booking[ id=" + id + " ]";
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public Set<PassengerEntity> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<PassengerEntity> passengers) {
        this.passengers = passengers;
    }

    public Set<ScheduleEntity> getSchedules() {
        return schedules;
    }

    public void setSchedules(Set<ScheduleEntity> schedules) {
        this.schedules = schedules;
    }
    
}
