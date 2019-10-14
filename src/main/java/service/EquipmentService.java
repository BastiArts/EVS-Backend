/** ********************* */
/*      VERSION 3.4     */
/** ********************* */
package service;

/**
 * GIT INFORMATIONS 1. COMMIT WITH COMMIT Message --> write what has been done
 * or what works 2. PUSH Program under Git -> Remote -> Push...
 */
import com.google.gson.Gson;
import entity.Entlehnung;
import entity.Equipment;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import java.util.ArrayList;
import java.util.Date;
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
     *
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
     *
     * @return
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("init")
    public String init() {
        String[] classes = new String[5];
        classes[0] = "4AHITM";
        classes[1] = "3AHITM";
        classes[2] = "3BHITM";
        classes[3] = "5AHITM";
        classes[4] = "2BHITM";
        Equipment e1 = new Equipment("camera", "nikon rx70", "Nikon", "F21", "ADDF202D", classes, "", null);
        Equipment e2 = new Equipment("camera", "Canon eos 5d mark", "Canon", "F22", "BDDF202D", classes, "", null);
        Equipment e3 = new Equipment("camera", "Gh4", "FujiFilm", "F23", "CDDF202D", classes, "", null);
        Equipment e4 = new Equipment("video", "Blackmagic", "FujiFilm", "V06", "DDDF202D", classes, "", null);
        Equipment e5 = new Equipment("audio", "Zoom", "Zoomer", "A04", "EDDF202D", classes, "", null);
        repo.add(e1);
        repo.add(e2);
        repo.add(e3);
        repo.add(e4);
        repo.add(e5);

        return "Equipment is initialized!";
    }

    /*
     * Gets a List of all existing equipment from the database to send it to the
     * Front-End as a big String
     *
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

    /**
     * Method returns a list of all borrowed equipment to the associated user.
     * This list is displayed to the user on the client under 'Mein Equipment'
     *
     * @return
     */
    @GET
    @Path("find/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findUserEquipment(@PathParam("username") String username) {
        //List userEquList = repo.getUserEquipment(username);
        Gson gson = new Gson();
        return gson.toJson(new Equipment());
    }

    /**
     * Method returns a list of all available equipment to the client, who wants
     * to borrow some new stuff
     *
     * @return
     */
    @GET
    @Path("findAvailable")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAvailableEquipment() {
        List availableEquipment = repo.getAvailableEquipment();
        return new Gson().toJson(availableEquipment);
    }

    /**
     * Gets an equipment from the Front-End (or testing applications) and
     * inserts it into the database
     *
     * @param e
     * @return
     */
    @POST
    @Path("addEquipment")
    @Consumes(MediaType.APPLICATION_JSON)
    public String insert(Equipment e) {
        e.setDisplayname(e.getBrand() + " " + e.getName());
        repo.add(e);
        return e.getDisplayname();
    }

    /**
     * Delete existing equipment out of database
     *
     * @param e
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteEquipment")
    public void delete(String e) {
        Gson gson = new Gson();
        System.out.println(EVSColorizer.CYAN + e + EVSColorizer.reset());
        Equipment equ = gson.fromJson(e, Equipment.class);
        repo.delete(equ.getId());
    }

    /**
     * Update existing Equipment
     *
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

    public String setDisplayName(Equipment e) {
        return e.getBrand() + " " + e.getName();
    }

}
