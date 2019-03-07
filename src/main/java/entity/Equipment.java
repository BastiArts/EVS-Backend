package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author M. Fadljevic
*/
@XmlRootElement
@Entity(name = "evs_equipment")
@NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM evs_equipment e")
public class Equipment implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    private String category;
    private String name; //750 D
    private String brand; //Canon
    @Transient
    private String displayname; //brand + name
    private String interneNummer;
    private String serielNumber; //F33 for example
    private String[] usableClasses;
    private long price;
    private String photoPath;
    private Detail specs;
    private String userId;

    /*What makes the Equipment special (24 Megapixel) and what 
                    is included with this Equipment(SD-Card, Akku, ...)*/

    public Equipment() {
    }

    /**
     * Constructor
     *
     * @param category - e.g. Audio, Video, Camera, Gadgets
     * @param name - Product name
     * @param brand - The brand of the Product e.g. Fujifilm, Canon, Nikon, ...
     * @param interneNummer - The internal Equipmentnumber e.g. F20
     * @param serialNumber - The Serialnumber of the Product
     * @param usableClasses - Defines the classes, who have access to this
     * Equipment e.g. 4AHITM, 3AHITM,...
     * @param price - The price of the Product
     * @param photoPath - Defines the Path of the stored Thumbnail
     * @param specs - Is a String, which contains all the useful information
     * about the Equipment (e.g. Resolution, Crop-Factor,...)
     */
    public Equipment(String category, String name, String brand, String interneNummer, String serialNumber, String[] usableClasses, long price, String photoPath, Detail specs) {
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.interneNummer = interneNummer;
        this.serielNumber = serialNumber;
        this.usableClasses = usableClasses;
        this.price = price;
        this.photoPath = photoPath;
        this.specs = specs;
    }

    public Equipment(String category, String name, String brand, String interneNummer, String serielNumber, String[] usableClasses, long price, String photoPath, Detail specs, String userId) {
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.interneNummer = interneNummer;
        this.serielNumber = serielNumber;
        this.usableClasses = usableClasses;
        this.price = price;
        this.photoPath = photoPath;
        this.specs = specs;
        this.userId = userId;
    }
    

    /**
     * Constructor
     * 
     * @param category - e.g. Audio, Video, Camera, Gadgets
     * @param name - Product name
     * @param brand - The brand of the Product e.g. Fujifilm, Canon, Nikon, ...
     */
    public Equipment(String category, String name, String brand) {
        this.category = category;
        this.name = name;
        this.brand = brand;
    }

    /**
    * Just some Getter and Setter for all existing fields in this class
    * to make sure JPA can change and get the data correct from the server
     */
    
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Detail getSpecs() {
        return specs;
    }

    public void setSpecs(Detail specs) {
        this.specs = specs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
