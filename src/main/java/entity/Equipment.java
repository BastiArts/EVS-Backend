package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author M. Fadljevic
 */
@XmlRootElement
@Entity(name = "evs_equipment")
@NamedQueries({
    @NamedQuery(name = "Equipment.findAll", query = "SELECT e FROM evs_equipment e")
    ,
    @NamedQuery(name = "Equipment.findSingleBySeriel", query = "SELECT e FROM evs_equipment e WHERE e.serialnumber = :sernumber")
})
public class Equipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    private String category;
    private String name; //750 D
    private String brand; //Canon
    private String internenummer;
    private String serialnumber; //F33 for example
    private String[] usableclasses;
    private String photopath;
    private ArrayList<String> specs;
    private String displayname; //brand + name
    private String longname;
    private String inventorynumber;

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
     * @param internenummer - The internenummer Equipmentnumber e.g. F20
     * @param serialNumber - The Serialnumber of the Product
     * @param usableclasses - Defines the classes, who have access to this
     * Equipment e.g. 4AHITM, 3AHITM,...
     * @param photopath - Defines the Path of the stored Thumbnail
     * @param specs - Is a String, which contains all the useful information
     * about the Equipment (e.g. Resolution, Crop-Factor,...)
     */
    public Equipment(String category, String name, String brand, String internenummer, String serialNumber, String[] usableclasses, String photopath, ArrayList<String> specs) {
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.internenummer = internenummer;
        this.serialnumber = serialNumber;
        this.usableclasses = usableclasses;
        this.photopath = photopath;
        this.specs = specs;
    }

    public Equipment(String category, String name, String brand, String internenummer, String serielnumber, String[] usableclasses, long price, String photopath, ArrayList<String> specs, User userId) {
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.internenummer = internenummer;
        this.serialnumber = serielnumber;
        this.usableclasses = usableclasses;
        this.photopath = photopath;
        this.specs = specs;
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

    public String getInternenummer() {
        return internenummer;
    }

    public void setInternenummer(String internenummer) {
        this.internenummer = internenummer;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String[] getUsableclasses() {
        return usableclasses;
    }

    public void setUsableclasses(String[] usableclasses) {
        this.usableclasses = usableclasses;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public ArrayList<String> getSpecs() {
        return specs;
    }

    public void setSpecs(ArrayList<String> specs) {
        this.specs = specs;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getInventorynumber() {
        return inventorynumber;
    }

    public void setInventorynumber(String inventorynumber) {
        this.inventorynumber = inventorynumber;
    }

    /**
     * Just some Getter and Setter for all existing fields in this class to make
     * sure JPA can change and get the data correct from the server
     */
    

}
