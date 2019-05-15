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
import entity.User;
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
        e1.setDisplayname(this.setDisplayName(e1));
        Equipment e2 = new Equipment("audio", "MX75", "Rode");
        e2.setDisplayname(this.setDisplayName(e2));
        e1.setInterneNummer("F22E2");
        e2.setInterneNummer("F23E4");
        
        User user1 = new User("it150160", "Manuel", "Fadljevic", "4AHITM", true);
        User user2 = new User("it150178", "Sebastian", "Schiefermayr", "4AHITM", true);
        User teacher = new User("it150158", "Julian", "Dannigner", "4AHITM", false);
        
        
        
        Equipment eu1 = new Equipment("video", "Camcorder", "Sony");
        eu1.setDisplayname(this.setDisplayName(eu1));
        eu1.setBorrowUser(user1);
        Equipment eu2 = new Equipment("camera", "CoolCam", "Ericson");
        eu2.setDisplayname(this.setDisplayName(eu2));
        eu2.setInterneNummer("F23 F20");
        eu2.setBorrowUser(user2);
        Equipment eu3 = new Equipment("video", "Camcorder3", "Apple");
        eu3.setInterneNummer("F22 A300");
        eu3.setDisplayname(this.setDisplayName(eu3));
        eu3.setBorrowUser(teacher);
        
        
        repo.add(e1);
        repo.add(e2);
        repo.updateUser(user1);
        repo.updateUser(user2);
        repo.updateUser(teacher);
        repo.add(eu1);
        repo.add(eu2);
        repo.add(eu3);
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
        return new Gson().toJson(eList);
    }
    
    @GET
    @Path("find/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findUserEquipment(@PathParam("username") String username){
        List userEquList = repo.getUserEquipment(username);
        Gson gson = new Gson();
        return gson.toJson(userEquList);
    }
    
    @GET
    @Path("findAvailable")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAvailableEquipment(){
        List equipment = repo.getAvailableEquipment();
        return new Gson().toJson(equipment);
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
    
    
    public String setDisplayName(Equipment e){
        return e.getBrand() + " " + e.getName();
    }
    
}
