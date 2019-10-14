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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@Path("entlehnung")
public class EntlehnungsService {

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
        return "JAVA SE Server - Entlehnungsservice ist startbereit...";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("createEntlehnung")
    public String createEntlehnung(
            @QueryParam("userid") String username,
            @QueryParam("equipmentid") long id,
            @QueryParam("fromdate") String fromdate,
            @QueryParam("todate") String todate
    ) throws ParseException {
        System.out.println(EVSColorizer.cyan() + username);
        System.out.println(EVSColorizer.cyan() + id);
        System.out.println(EVSColorizer.cyan() + fromdate);
        System.out.println(EVSColorizer.cyan() + todate + EVSColorizer.reset());
        User user = repo.findUser(username);
        Equipment equ = repo.getSingleEquipment(id);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date fromdate1 = dateFormat.parse(fromdate);
        Date todate1 = dateFormat.parse(todate);
        Entlehnung entl = new Entlehnung(fromdate1, todate1, "reserviert", user, equ);
        entl = repo.makeNewEntlehnung(entl);
        return new Gson().toJson(entl);
    }

    @GET
    @Path("getdateexample")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
