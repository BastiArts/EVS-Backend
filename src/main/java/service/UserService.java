package service;

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
            //endUser.setPicturePath(username + "_PB.jpg");
            repo.insertUser(endUser);
            JSONObject json = new JSONObject(endUser);
            System.out.println(json.toString());
            return json.toString();
        }else{
            return "{}";
        }
    }

    @GET
    @Path("jwtTest")
    @Produces(MediaType.TEXT_PLAIN)
    public String jwtTesting() {
        return new JwtBuilder().create("Manuel");
    }

    @GET
    @Path("msg")
    @Produces(MediaType.TEXT_PLAIN)
    public String message() {
        return "Java SE Server powered by EVS GmbH!";
    }
    
    @POST
    @Path("picturePath")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User setProfilePath(User user){
        return repo.setProfilePath(user);
    }
    
    
}


/*

System.out.println(EVSColorizer.CYAN + endUser.getFirstname()
                    + " -- " + endUser.getLastname() + " -- " + endUser.getUsername()
                    + " -- " + endUser.getSchoolclass() + " -- " + endUser.isIsStudent());


return "{\"username\" :  \"" + uname + "\","
                + " \"firstname\" : " + firstname + "\","
                + " \"lastname\" : " + lastname + "\","
                + " \"schoolclass\" : " + schoolclass + "\","
                + " \"isStudent\" : " + isStudent + "\"}";*/
