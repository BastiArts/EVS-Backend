/** ********************* */
/*      VERSION 2.0     */
/** ********************* */
package service;

/**
 * GIT INFORMATIONS 1. COMMIT WITH COMMIT Message --> write what has been done
 * or what works 2. PUSH Program under Git -> Remote -> Push...
 */
import entity.Equipment;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * This Class is for the Web Orientation f.e.:
 * https://localhost/rest/equipment/msg
 */
@Path("equipment")
public class EquipmentService {

    /**
     * Gets an instance of the Class Repository where all Data is put and pulled
     * in and from the database (List in current version)
     */
    Repository repo = Repository.getInstance();

    ;

    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("msg")
    public String msg() {
        return "Java SE Server from Equipment is started...";
    }

    //Initialise test data
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("init")
    public String init() {
        Equipment e1 = new Equipment("camera", "Z12", "Canon");
        e1.setDisplayname(e1.getBrand() + " " + e1.getName());
        Equipment e2 = new Equipment("mikro", "MX75", "Rode");
        e2.setDisplayname(e2.getBrand() + " " + e2.getName());
        repo.add(e1);
        repo.add(e2);
        return "Equipment is initialized! ";
    }

    /**
     * Gets a List of all existing equipment from the database to send it to the
     * Front-End as a big String
     */
    @GET
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll() {
        //public LinkedList<Equipment> findAll()
        List<Equipment> eList = repo.getEquipment();
        JSONArray equipments = new JSONArray(eList);
        return equipments.toString();
    }

    /**
     * Gets an equipment from the Front-End (or testing applications) and
     * inserts it into the database
     */
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    public Equipment insert(Equipment e) {
        e.setDisplayname(e.getBrand() + " " + e.getName());
        System.out.println(EVSColorizer.GREEN + e.getDisplayname() + EVSColorizer.reset());
        return repo.insert(e);
    }

    @POST
    @Path("insertEquipment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String insertEquipment(Equipment e) {

        return "Works well";
//        e.setDisplayname(e.getBrand() + " " + e.getName());
//        //repo.add(e);
//        return e.getDisplayname();
    }

    /*Equipmet löschen --> wird aus der Datenbank entfernt */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteEquipment")
    public void delete(Equipment e) {
        repo.delete(e);
    }

    /*Eqipment update */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("updateEquipment")
    public void update(Equipment e) {
        repo.update(e);
    }

}
