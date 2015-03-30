/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Peter
 */
@Entity(name="Passengers")
public class PassengerEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String passportNo;
    private String name;
    private String gender;
    private String dob;
    
    public void create(String passportNo, String name, String gender, String dob){
        this.passportNo = passportNo;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }
    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passportNo != null ? passportNo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the passportNo fields are not set
        if (!(object instanceof PassengerEntity)) {
            return false;
        }
        PassengerEntity other = (PassengerEntity) object;
        if ((this.passportNo == null && other.passportNo != null) || (this.passportNo != null && !this.passportNo.equals(other.passportNo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Passenger[ id=" + passportNo + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
}
