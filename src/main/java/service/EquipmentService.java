/** ********************* */
/*      VERSION 3.4     */
/** ********************* */
package service;

/**
 * GIT INFORMATIONS 1. COMMIT WITH COMMIT Message --> write what has been done
 * or what works 2. PUSH Program under Git -> Remote -> Push...
 */
import com.google.gson.Gson;
import entity.Equipment;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;
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
    
    /**
     * Test message for testing if the server is running
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("msg")
    public String msg() {
        return "Java SE Server from Equipment is started...";
    }

    /**
     * Some test initialization for equipment
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("init")
    public String init() {
        Equipment e1 = new Equipment("camera", "Z12", "Canon");
        e1.setDisplayname(e1.getBrand() + " " + e1.getName());
        Equipment e2 = new Equipment("mikro", "MX75", "Rode");
        e2.setDisplayname(e2.getBrand() + " " + e2.getName());
        e1.setInterneNummer("HULULULULULULULU");
        e2.setInterneNummer("MUHAHAHAHAHAHAHA");
        repo.add(e1);
        repo.add(e2);
        return "Equipment is initialized! ";
    }

    /**
     * Gets a List of all existing equipment from the database to send it to the
     * Front-End as a big String
     * @return 
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
     * @param e
     * @return 
     */
    @POST
    @Path("addEquipment")
    @Consumes(MediaType.APPLICATION_JSON)
    public String insert(String e) {
        Gson gson = new Gson();
        Equipment equipment = gson.fromJson(e, Equipment.class);
        equipment.setDisplayname(equipment.getBrand() + " " + equipment.getName());
        repo.add(equipment);
        return equipment.getDisplayname();
    }

    /**
     * Delete existing equipment out of database
     * @param e
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteEquipment")
    public void delete(String e) {
        Gson gson = new Gson();
        Equipment equ = gson.fromJson(e, Equipment.class);
        repo.delete(equ);
    }

    /**
     * Update existing Equipment
     * @param e
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("updateEquipment")
    public void update(String e) {
        Gson gson = new Gson();
        Equipment equipment = gson.fromJson(e, Equipment.class);
        repo.update(equipment);
    }

}
