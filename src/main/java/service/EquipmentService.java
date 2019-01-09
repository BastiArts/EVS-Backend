/************************/
/*      VERSION 1.3     */
/************************/
package service;
/*
GIT INFORMATIONS
1. COMMIT WITH COMMIT Message --> write what has been done or what works
2. PUSH Program under Git -> Remote -> Push...
*/
import entity.Equipment;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;
import entity.User;

/*
This Class is for the Web Orientation
f.e.: https://localhost/rest/evs/message
*/
@Path("evs")
public class EquipmentService {
    
    /*
    * Gets an instance of the Class Repository where all Data is put and pulled
    * in and from the database (List in current version)
    */
    Repository repo = Repository.getInstance();;

    // Show message 
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("message")
    public String message() {
        return "EVS-Server powered by JAVA SE!";
    }
    
    
    //Initialise some Data
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("init")
    public String init() {
        Equipment e0 = new Equipment("camera", "Fadljevic", "Manuel", "FF2223", "LMAO222", "4AHITM,5AHITM", 1000, "photopath", "lol");
        e0.setDisplayname(e0.getBrand() + " " + e0.getName());
        Equipment e1 = new Equipment("camera", "Z12", "Canon");
        e1.setDisplayname(e1.getBrand() + " " + e1.getName());
        Equipment e2 = new Equipment("mikro", "MX75", "Rode");
        e2.setDisplayname(e2.getBrand() + " " + e2.getName());
        repo.add(e1);
        repo.add(e2);
        repo.add(e0);
        return "Equipment is initialized! ";
    }
    
    
    
    //sends a LinkedList<Equipment> --> all data from database
    @GET
    @Path("find")
    public String findAll(){
    //public LinkedList<Equipment> findAll()
        List<Equipment> eList = repo.getEquipment();
        String ausgabe = "";
        for (Equipment e : eList) {
            ausgabe += e.getDisplayname() + "<br>";
        }
        return ausgabe;
        //return repo.getEquipment();
    }
    
    
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    public Equipment insert(Equipment e){
        e.setDisplayname(e.getBrand() + " " + e.getName());
        
        repo.add(e);
        return e;
    }    
    
    /**********************************************************
    **********************************************************
       ALL WHERE USER TEST DATAS ARE GENERATED AND EMPLOYED
    **********************************************************
    **********************************************************/
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
    
    @GET
    @Path("initUser")
    public String initUser(){
        System.out.println("hello there");
        
        User u1 = new User("it150160", "mypwd");
        User u2 = new User("it150156", "password");
        repo.add(u1);
        repo.add(u2);
        return "Users are initialized!";
    }
    
}
