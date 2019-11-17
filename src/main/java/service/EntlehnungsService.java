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

    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    Gson gson = new Gson();

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
    @Path("proofdates/{id}")
    public String proofDateForEquipment(@PathParam("id") long id) {
        return gson.toJson(repo.proofDateOfEquipmentReservation(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("createentlehnung")
    public String createEntlehnung(
            @QueryParam("userid") String username,
            @QueryParam("serialnumber") String serial,
            @QueryParam("fromdate") String fromdate,
            @QueryParam("todate") String todate
    ) throws ParseException {
        User user = repo.findUser(username);
        Equipment equ = repo.getEquBySer(serial);

        Date fromdate1 = dateFormat.parse(fromdate);
        Date todate1 = dateFormat.parse(todate);
        Entlehnung entl = new Entlehnung(fromdate1, todate1, "pending", user, equ);
        entl = repo.makeNewEntlehnung(entl);
        return new Gson().toJson(entl);
    }

    @GET
    @Path("editentlehnung")
    @Produces(MediaType.APPLICATION_JSON)
    public String editentlehnung(
            @QueryParam("id") long id,
            @QueryParam("status") String status
    ) {
        Entlehnung e;
        if (status.equalsIgnoreCase("confirmed")) {
            // Ändern des status auf ausgeborgt
            e = repo.findEntlehnung(id);
            /*
            fromdate <= new DATE() < todate --> status - borrowed
            new DATE() < fromdate           --> future --> status - guarded
             */
            Date nowdate = new Date();
            System.out.println(EVSColorizer.cyan() + nowdate + EVSColorizer.reset());
            System.out.println(EVSColorizer.cyan() + e.getFromdate() + EVSColorizer.reset());
            System.out.println(EVSColorizer.cyan() + e.getTodate() + EVSColorizer.reset());
            System.out.println(EVSColorizer.cyan() + e.getFromdate().after(nowdate) + EVSColorizer.reset());

            if (e.getFromdate().before(nowdate) && e.getTodate().after(nowdate)) {
                // borrowed
                e.setStatus("borrowed");
            } else if (dateFormat.format(nowdate).equals(dateFormat.format(e.getFromdate()))) {
                // borrowed
                e.setStatus("borrowed");
            } else if (e.getFromdate().after(nowdate)) {
                // guarded
                e.setStatus("guarded");
            }

            e.setApproved(true);
            e = repo.confirmEntlehnung(e);
            return new Gson().toJson(repo.findPendingEntlehnungen());
        } else {
            System.out.println(repo.declineEntlehnung(repo.findEntlehnung(id)));
            return "Entlehnung was declined";
        }
    }

    @GET
    @Path("getpendingentlehnungen")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPendingEntlehnungen() {
        return gson.toJson(repo.findPendingEntlehnungen());
    }

    @GET
    @Path("getallentlehnungen")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEntlehnungen() {
        return gson.toJson(repo.getAllEntlehnungen());
    }

    @GET
    @Path("getequbyser/{serie}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findEquBySeriel(@PathParam("serie") String serialnumber) {
        return gson.toJson(repo.getEquBySer(serialnumber));
    }
    /*
    

    @GET
    @Path("getdateexample")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @GET
    @Path("editentlehnung")
    @Produces(MediaType.APPLICATION_JSON)
    public String editentlehnung(
            @QueryParam("id") long id,
            @QueryParam("status") String status
    ) {
        if (status.equalsIgnoreCase("confirmed")) {
            // Ändern des status auf ausgeborgt
            Entlehnung e = repo.findEntlehnung(id);
            if (dateFormat.format(new Date()).equalsIgnoreCase(dateFormat.format(e.getFromdate()))) {
                // Wenn es der heutige Tag ist --> ausborgen
                e.setStatus("verborgt");
            }
            e.setApproved(true);
            e = repo.confirmEntlehnung(e);
            return new Gson().toJson(repo.findAllEntlehnungen());
        } else {
            System.out.println(repo.declineEntlehnung(repo.findEntlehnung(id)));
        }
        return "";
    }

    @GET
    @Path("getanfragen")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAnfragen() {
        return new Gson().toJson(repo.getAllAnfragen());
    }*/
}
