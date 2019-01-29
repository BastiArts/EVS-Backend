package service;

import entity.Equipment;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;
import entity.User;
import jwt.JwtBuilder;

/**
 *
 * @author M. Fadljevic
 */

@Path("users")
public class UserService {
    
    Repository repo = Repository.getInstance();
    
    
    /*
    * Login Path with parameters to proof if the username and password is the
    * same as the username and password from the school
    */
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean login(
            @QueryParam("user") String username,
            @QueryParam("pwd") String password){
        return repo.proofLogin(username, password);
    }
    
    @GET
    @Path("jwtTest")
    @Produces(MediaType.TEXT_PLAIN)
    public String jwtTesting(){
        return new JwtBuilder().create("Manuel");
    }
    
    
    
    /*
    * Gets a List of all existing Users and merge them into a big String to send
    * this String to the Front-End
    *
    @GET
    @Path("findUser")
    public String findUser(){
        List<User> uList = repo.getUsers();
        String ausgabe = "";
        for(User u : uList){
            ausgabe += u.getUsername() + "<br>";
        }
        return ausgabe;
    }
    
    /*
    * Initializes some Test Data into the Database to test the whole process
    * of controlling username and password and stuff.
    *
    @GET
    @Path("initUser")
    @Produces(MediaType.TEXT_PLAIN)
    public String initUser(){
        System.out.println("hello there");
        
        User u1 = new User("it150160", "mypwd");
        User u2 = new User("it150156", "password");
        repo.add(u1);
        repo.add(u2);
        return "Users are initialized!";
    }*/
    
}
