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
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Peter
 */
@Entity(name="Users")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String username;
    private String password;
    private int contactNo;
    private String email;
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="owner")
    private Collection<BookingEntity> bookings = new ArrayList<BookingEntity>();
    public UserEntity(){
    }
    public void create(String username, String password, int contactNo, String email){
        this.username = username;
        this.password = password;
        this.contactNo = contactNo;
        this.email = email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getContactNo() {
        return contactNo;
    }

    public void setContactNo(int contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the username fields are not set
        if (!(object instanceof UserEntity)) {
            return false;
        }
        UserEntity other = (UserEntity) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.User[ id=" + username + " ]";
    }

    public Collection<BookingEntity> getBookings() {
        return bookings;
    }

    public void setBookings(Collection<BookingEntity> bookings) {
        this.bookings = bookings;
    }
}
