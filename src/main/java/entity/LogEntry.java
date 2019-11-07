/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author manuel
 */
public class LogEntry {

    private String timestamp;
    private String status;
    private String user;
    private String equipment;

    public LogEntry() {
    }

    public LogEntry(String timestamp, String status, String user, String equipment) {
        this.timestamp = timestamp;
        this.status = status;
        this.user = user;
        this.equipment = equipment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

}
