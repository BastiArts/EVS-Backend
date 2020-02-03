/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enums.RentType;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author manuel
 */
@Entity(name = "evs_entlehnung")
public class Entlehnung implements Serializable {

    // Felder: von; bis; status(verborgt, reserviert oder zurückgegeben)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date fromdate;
    private Date todate;
    private RentType eq_status;
    @Deprecated
    private String status; // 4 types (pending - guarded - borrowed - returned)
    private boolean approved = false; // 

    @ManyToOne
    private User user;
    @ManyToOne
    private Equipment equ;

    public Entlehnung() {
    }

    public Entlehnung(Date fromdate, Date todate, String status, User user, Equipment equ) {
        this.fromdate = fromdate;
        this.todate = todate;
        this.status = status;
        this.user = user;
        this.equ = equ;
    }

    public Entlehnung(Date fromdate, Date todate, String status) {
        this.fromdate = fromdate;
        this.todate = todate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Equipment getEqu() {
        return equ;
    }

    public void setEqu(Equipment equ) {
        this.equ = equ;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public RentType getEq_status() {
        return eq_status;
    }

    public void setEq_status(RentType eq_status) {
        this.eq_status = eq_status;
    }
    
}
