package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;


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
    private boolean isStudent;
    
    public User(){
        
    }

    /**
     * Constructor
     * 
     * @param username - Username
     * @param firstname - Firstname of the authenticated User
     * @param lastname - Lastname -||-
     * @param schoolclass - e.g. 4AHITM, 3AHITM,...
     * @param role - Defines if the User is a Student or a Teacher
     */
    public User(String username, String firstname, String lastname, String schoolclass, boolean isStudent) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.schoolclass = schoolclass;
        this.isStudent = isStudent;
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

    public boolean isIsStudent() {
        return isStudent;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
}
