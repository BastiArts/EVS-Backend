package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author M. Fadljevic
 */
@Entity
@Table(name = "evs_user")
@XmlRootElement
public class User implements Serializable {

    @Id
    @Column(length=10)
    private String username;
    private String firstname;
    private String lastname;
    private String email = "";
    private String schoolclass;
    private boolean isStudent;
    private String picturePath = "";

    @OneToMany(mappedBy = "borrowUser")
    private ArrayList<Equipment> borrowedEquipment = new ArrayList<>();

    public User() {
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

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public ArrayList<Equipment> getBorrowedEquipment() {
        return borrowedEquipment;
    }

    public void setBorrowedEquipment(ArrayList<Equipment> borrowedEquipment) {
        this.borrowedEquipment = borrowedEquipment;
    }

    //    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
}
