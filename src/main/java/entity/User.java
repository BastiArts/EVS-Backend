package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author M. Fadljevic
 */
@Entity(name="evs_user")
@NamedQuery(name="evs_user.findAll", query="SELECT u FROM evs_user u")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String username;
    private String firstname;
    private String lastname;
    private String schoolclass;
    private String role;
    
    public User(){
        
    }

    
    public User(String username, String firstname, String lastname, String schoolclass, String role) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.schoolclass = schoolclass;
        this.role = role;
    }
    
    
    /*
    * Just some Getter and Setter for all existing fields in this class
    * to make sure JPA can change and get the data correct from the server
    */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSchoolclass() {
        return schoolclass;
    }

    public void setSchoolclass(String schoolclass) {
        this.schoolclass = schoolclass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
}
