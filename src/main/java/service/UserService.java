package service;

import entity.Equipment;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import evs.ldapconnection.LdapAuthException;
import evs.ldapconnection.LdapException;
import jwt.JwtBuilder;

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
    */
    @GET
    @Path("login")
    public User login(
            @QueryParam("user") String username,
            @QueryParam("pwd") String password) throws LdapException, LdapAuthException{
        System.out.println(EVSColorizer.RED + "Some data are incomming: " + username + EVSColorizer.reset());
        return repo.proofLogin(username, password);
    }
    
    @GET
    @Path("jwtTest")
    @Produces(MediaType.TEXT_PLAIN)
    public String jwtTesting(){
        return new JwtBuilder().create("Manuel");
    }
    
    
    @GET
    @Path("msg")
    @Produces(MediaType.TEXT_PLAIN)
    public String message(){
        return "Java SE Server powered by EVS GmbH!";
    }
    
}
