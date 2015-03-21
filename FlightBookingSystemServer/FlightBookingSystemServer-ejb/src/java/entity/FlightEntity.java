/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Peter
 */
@Entity(name="Flights")
public class FlightEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String flightNo;
    private String departureCity;
    private String arrivalCity;
    private String aircraftType;
    private int totalSeats;
    @OneToMany(cascade={CascadeType.ALL},mappedBy="flight")
    private Collection<ScheduleEntity> schedule = new ArrayList<ScheduleEntity>();
    public FlightEntity() {
    }
    public void create(String flightNo, String depart, String arriv, String type, int seats){
        this.flightNo = flightNo;
        this.departureCity = depart;
        this.arrivalCity = arriv;
        this.aircraftType = type;
        this.totalSeats = seats;
    }
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (flightNo != null ? flightNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the flightNo fields are not set
        if (!(object instanceof FlightEntity)) {
            return false;
        }
        FlightEntity other = (FlightEntity) object;
        if ((this.flightNo == null && other.flightNo != null) || (this.flightNo != null && !this.flightNo.equals(other.flightNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Flight[ id=" + flightNo + " ]";
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Collection<ScheduleEntity> getSchedule() {
        return schedule;
    }

    public void setSchedule(Collection<ScheduleEntity> schedule) {
        this.schedule = schedule;
    }
    
}
