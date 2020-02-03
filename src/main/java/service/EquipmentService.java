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
import entity.LogEntry;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import repository.Repository;
import org.json.JSONArray;
import javax.ws.rs.core.*;
import org.json.JSONObject;

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
    @Produces(MediaType.APPLICATION_JSON)
    public String insert(String equ) {
        Gson gson = new Gson();
        Equipment e = gson.fromJson(equ, Equipment.class);
        e.setPhotopath(findRightPicture(e.getCategory()));
        e.setDisplayname(e.getBrand() + " " + e.getName());
        repo.add(e);
        return gson.toJson(e);
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

    /**
     * Send the log entries as List of LogEntry
     *
     * @param none
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getalllogsfiles")
    public String sendlogs() {
        List<LogEntry> logentries = new ArrayList<>();
        File file = new File("log");
        File[] logs = file.listFiles();
        List<String> logsasstring = new ArrayList<>();
        for (int i = 0; i < logs.length; i++) {
            logsasstring.add(logs[i].getName());
        }
        return new Gson().toJson(logsasstring);
    }

    /**
     * Send the log entries as List of LogEntry
     *
     * @param none
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getdatafromlog/{name}")
    public String senddatafromlog(@PathParam("name") String filename) {
        File file = new File("log/" + filename);
        List<LogEntry> logentries = new ArrayList<>();
        try {
            List<String> alllines = Files.readAllLines(file.toPath());
            alllines.forEach(line -> {

                String timestamp = line.substring(0, 19);
                int linebegin = line.indexOf('[');
                int lineend = line.indexOf(']');
                String status = line.substring(linebegin + 1, lineend);
                linebegin = lineend + 1;
                lineend = line.indexOf('-') - 1;
                String name = line.substring(linebegin, lineend);
                String equname = line.substring(lineend + 2);
                logentries.add(new LogEntry(timestamp, status, name, equname));
            });
            Collections.reverse(logentries);
            return new Gson().toJson(logentries);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Some errors appeared sorry dude!");
            return "failed!" + filename;
        }
    }

    public String setDisplayName(Equipment e) {
        return e.getBrand() + " " + e.getName();
    }

    public String findRightPicture(String category) {
        switch (category) {
            case "camera":
                return "../assets/icons/equip/camera_icon.svg";
            case "videokamera":
                return "../assets/icons/equip/videocamera_icon.svg";
            case "audio":
                return "../assets/icons/equip/microphone_icon.svg";
            default:
                return "";
        }
    }

    @Context
    private UriInfo context;
    private static final String UPLOAD_FOLDER = "uploads/equipment/";

    /**
     * Returns text response to caller containing uploaded file location
     *
     * @return error response in case of missing parameters an internal
     * exception or success response if file has been stored successfully
     */
    @POST
    @Path("uploadimage")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("serialnumber") String serialnumber) {
        // check if all form parameters are provided
        JSONObject json = new JSONObject();

        if (uploadedInputStream == null || fileDetail == null || serialnumber == null || serialnumber == "") {
            System.out.println("");
            System.out.println("");
            System.out.println(EVSColorizer.cyan()
                    + "Formdata Parameter not correct"
                    + EVSColorizer.reset());
            json.append("status", "failed");
            json.append("exception", "formdataparam not correct");
            return new Gson().toJson(json);
        }
        // create our destination folder, if it not exists
        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (SecurityException se) {
            System.out.println("");
            System.out.println("");
            System.out.println(EVSColorizer.cyan()
                    + "Destination Folder could not be created"
                    + EVSColorizer.reset());
            json.append("status", "failed");
            json.append("exception", "cannot create destinationfolder on vm");
            return new Gson().toJson(json);
        }
        String uploadedFileLocation = UPLOAD_FOLDER + fileDetail.getFileName();
        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
            Equipment equ = repo.getEquBySer(serialnumber);
            equ.setPhotopath("http://vm88.htl-leonding.ac.at/" + uploadedFileLocation);
            repo.update(equ);
        } catch (IOException e) {
            System.out.println("");
            System.out.println("");
            System.out.println(EVSColorizer.cyan()
                    + "File could not be safed (maybe because of same filename)"
                    + EVSColorizer.reset());
            json.append("status", "failed");
            json.append("exception", "could not save file");
            return new Gson().toJson(json);
        }
        json.append("status", "success");
        json.append("filename", fileDetail.getFileName());
        return new Gson().toJson(json);
    }

    /**
     * Utility method to save InputStream data to target location/file
     *
     * @param inStream - InputStream to be saved
     * @param target - full path to destination file
     */
    private void saveToFile(InputStream inStream, String target)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File(target));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    /**
     * Creates a folder to desired location if it not already exists
     *
     * @param dirName - full path to the folder
     * @throws SecurityException - in case you don't have permission to create
     * the folder
     */
    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File theDir = new File(dirName);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }
    
    /** HANNA PART */
    /** NOT WORKING*/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("exportAllLogs")
    public String exportAllLogs() throws FileNotFoundException, IOException{
        String logfilenames = sendlogs(); 
        Gson gson = new Gson();
        String[] files = gson.fromJson(logfilenames, String[].class);
        FileOutputStream out = new FileOutputStream(new File("logs.xlsx"));
        List<LogEntry> logentries = new ArrayList<>();
        
        for (String file : files) {
            try {
            List<String> alllines = Files.readAllLines(new File(file).toPath());
            alllines.forEach(line ->{
                String timestamp = line.substring(0, 19);
                int linebegin = line.indexOf('[');
                int lineend = line.indexOf(']');
                String status = line.substring(linebegin + 1, lineend);
                linebegin = lineend + 1;
                lineend = line.indexOf('-') - 1;
                String name = line.substring(linebegin, lineend);
                String equname = line.substring(lineend + 2);
                logentries.add(new LogEntry(timestamp, status, name, equname));
            });
            return new Gson().toJson(logentries);
           
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Some errors appeared sorry dude!");
            return "failed!" + file;
        }
        }
        Collections.reverse(logentries);
         out.write(logentries.toString().getBytes());
         out.close();   
        
        return null;
        
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("exportEquipment")
    public String exportEquipment() throws FileNotFoundException, IOException{
      List<Equipment> allEquipment = repo.getEquipment();
      StringBuilder sb = new StringBuilder();
      sb.append("Marke;Name;Interne Nummer;Inventarnummer;Seriennummer")
              .append("\n");
      for(Equipment e : allEquipment){
          sb.append(e.getBrand())
                  .append(";")
                  .append(e.getName())
                  .append(";")
                  .append(e.getInternenummer())
                  .append(";")
                  .append(e.getInventorynumber())
                  .append(";")
                  .append(e.getSerialnumber())
                  .append("\n");
      }
      Files.write(new File("equipment.csv").toPath(), sb.toString().trim().getBytes("UTF-8"));
       return null; 
    }
}
