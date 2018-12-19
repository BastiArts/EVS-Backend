/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Manuel Fadljevic
 */
@Entity
public class Equipment {
    @Id
    long id;
    String category;
    String name; //750 D
    String brand; //Canon
    String displayname; //brand + name
    String interneNummer;
    String serielNumber; //F33 for example
    String[] usableClasses;
    int price;
    String photoPath;
    String specs;

    public Equipment() {
    }

    public Equipment(String category, String name, String brand, String interneNummer, String serielNumber, String[] usableClasses, int price, String photoPath, String specs) {
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.interneNummer = interneNummer;
        this.serielNumber = serielNumber;
        this.usableClasses = usableClasses;
        this.price = price;
        this.photoPath = photoPath;
        this.specs = specs;
    }

    public Equipment(String category, String name, String brand) {
        this.category = category;
        this.name = name;
        this.brand = brand;
    }
    
    
    
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getInterneNummer() {
        return interneNummer;
    }

    public void setInterneNummer(String interneNummer) {
        this.interneNummer = interneNummer;
    }

    public String getSerielNumber() {
        return serielNumber;
    }

    public void setSerielNumber(String serielNumber) {
        this.serielNumber = serielNumber;
    }

    public String[] getUsableClasses() {
        return usableClasses;
    }

    public void setUsableClasses(String[] usableClasses) {
        this.usableClasses = usableClasses;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
    
    
    
    
    
}
