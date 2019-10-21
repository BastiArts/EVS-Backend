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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
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
        List userequ = repo.getEquipmentFromUser(username);
        Gson gson = new Gson();
        return gson.toJson(userequ);
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
        List<Equipment> available = repo.getAvailableEquipment();
        return new Gson().toJson(available);
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
    @Produces(MediaType.APPLICATION_JSON)
    public String insert(Equipment e) {
        e.setDisplayname(e.getBrand() + " " + e.getName());
        repo.add(e);
        return new Gson().toJson(e);
    }

    /**
     * Delete existing equipment out of database
     *
     * @param e
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deleteEquipment")
    public String delete(String e) {
        Gson gson = new Gson();
        System.out.println(EVSColorizer.CYAN + e + EVSColorizer.reset());
        Equipment equ = gson.fromJson(e, Equipment.class);
        repo.delete(equ.getId());
        return new Gson().toJson(equ);
    }

    /**
     * Update existing Equipment
     *
     * @param e
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("updateEquipment")
    public String update(String e) {
        Gson gson = new Gson();
        Equipment equ = gson.fromJson(e, Equipment.class);
        repo.update(equ);
        return new Gson().toJson(equ);
    }

    public String setDisplayName(Equipment e) {
        return e.getBrand() + " " + e.getName();
    }

    /**
     * Photoupload from Herr Professor Lackinger
     *
     * @param file
     * @return
     */
    @POST
    @Path("uploadimage")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public void uploadImage(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @FormDataParam("seriennummer") String seriennummer) throws Exception {
        boolean worked = false;
        Equipment equ = repo.getEquBySer(seriennummer);
        String UPLOAD_PATH = "uploads/equipment/";
        File dirs = new File(UPLOAD_PATH);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }
        UPLOAD_PATH += equ.getInterneNummer();
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            if (fileMetaData.getName().endsWith(".jpg")) {
                UPLOAD_PATH += ".jpg";

            } else if (fileMetaData.getName().endsWith(".png")) {
                UPLOAD_PATH += ".png";
            }
            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            worked = true;
        } catch (IOException e) {
            throw new Exception("Error while uploading file. Please try again !!");
        }

        if (worked) {
            // Update User on database
            equ.setPhotoPath(UPLOAD_PATH);
            repo.update(equ);
            System.out.println(EVSColorizer.purple() + "Equipment picture successfully uploaded!" + EVSColorizer.reset());
        }
    }
}
