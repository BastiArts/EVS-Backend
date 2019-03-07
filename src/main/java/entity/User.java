package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author M. Fadljevic
 */
@Entity(name="evs_user")
@XmlRootElement
public class User implements Serializable{
    @Id
    private String username;
    private String firstname;
    private String lastname;
    private String email = "";
    private String schoolclass;
    private boolean isStudent;
    private String picturePath = "";
    
    public User(){}

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
        this.picturePath = "";
    }
    
    public User(String username, String firstname, String lastname, String email, String schoolclass, boolean isStudent, String picturePath) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.schoolclass = schoolclass;
        this.isStudent = isStudent;
        this.picturePath = picturePath;
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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
