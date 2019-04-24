package service;

import com.google.gson.Gson;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import evs.ldapconnection.LdapAuthException;
import evs.ldapconnection.LdapException;
import jwt.JwtBuilder;
import org.json.JSONObject;

/**
 *
 * @author M. Fadljevic
 */
@Path("users")
public class UserService {

    Repository repo = Repository.getInstance();

    /**
     * Login Path with parameters to proof if the username and password is the
     * same as the username and password from the school
     *
     * @param username
     * @param password
     * @return
     * @throws evs.ldapconnection.LdapException
     * @throws evs.ldapconnection.LdapAuthException
     */
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public String login(
            @QueryParam("user") String username,
            @QueryParam("pwd") String password) throws LdapException, LdapAuthException {

        System.out.println(EVSColorizer.RED + "Some data are incomming: " + username + EVSColorizer.reset());
        User endUser = repo.proofLogin(username, password);

        if (endUser != null) {
            Gson gsonObject = new Gson();

            //endUser.setPicturePath(username + "_PB.jpg");
            System.out.println("");
            System.out.println("This is user: " + endUser.getFirstname());
            repo.insertUser(endUser);
            String retString = gsonObject.toJson(endUser);
            System.out.println(retString);
            return retString;
        } else {
            return "{}";
        }
    }

    /**
     *
     * @return
     */
    @GET
    @Path("jwtTest")
    @Produces(MediaType.TEXT_PLAIN)
    public String jwtTesting() {
        return new JwtBuilder().create("Manuel");
    }

    /**
     *
     * @return
     */
    @GET
    @Path("msg")
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        return "Java SE Server powered by EVS GmbH!";
    }
    
    /**
     *
     * @param user
     * @return
     */
    @POST
    @Path("updateUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String setProfilePath(String user){
        Gson gson = new Gson();
        User u = gson.fromJson(user, User.class);
        repo.updateUser(u);
        return gson.toJson(u);
    }
}