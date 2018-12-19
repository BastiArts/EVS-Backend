package service;

import entity.Equipment;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import repository.Repository;

@Path("evs")
public class UserService {
    
    Repository repo = Repository.getInstance();;

    // Show message 
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("message")
    public String message() {
        return "EVS-Server powered by JAVA SE!";
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("init")
    public String init() {
        Equipment e1 = new Equipment("camera", "Z12", "Canon");
        e1.setDisplayname(e1.getBrand() + " " + e1.getName());
        Equipment e2 = new Equipment("mikro", "MX75", "Rode");
        e2.setDisplayname(e2.getBrand() + " " + e2.getName());
        Equipment e3 = new Equipment("mikro", "470F", "Rode");
        e3.setDisplayname(e3.getBrand() + " " + e3.getName());
        Equipment e4 = new Equipment("camera", "Eos 650D", "Canon");
        e4.setDisplayname(e4.getBrand() + " " + e4.getName());
        Equipment e5 = new Equipment("camcorder", "Scheisshaufn", "Canon");
        e5.setDisplayname(e5.getBrand() + " " + e5.getName());
        Equipment e6 = new Equipment("camera", "Black Magic", "Sony");
        e6.setDisplayname(e6.getBrand() + " " + e6.getName());
        Equipment e7 = new Equipment("mikro", "Kraken", "Razor");
        e7.setDisplayname(e7.getBrand() + " " + e7.getName());
        Equipment e8 = new Equipment("camera", "GH5 4K", "Lumix");
        e8.setDisplayname(e8.getBrand() + " " + e8.getName());
        repo.add(e1);
        repo.add(e2);
        repo.add(e3);
        repo.add(e4);
        repo.add(e5);
        repo.add(e6);
        repo.add(e7);
        repo.add(e8);
        
        return "Equipment is inizialised! ";
    }
    
    @GET
    @Path("find")
    public String getEquipment(){
        LinkedList<Equipment> eList = repo.getEquipment();
        String ausgabe = "";
        for (Equipment e : eList) {
            ausgabe += e.getDisplayname() + "<br>";
        }
        return ausgabe;      
    }
    
    
    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("findAll")
    public List<Person> find() throws IOException {
        return Repository.getInstance().find();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("find")
    public List<Person> find(@QueryParam("page") int page,
                             @QueryParam("size") int size) throws IOException{
        return Repository.getInstance().find(page, size);
    }
    
    
    @POST
    @Path("insert")
    @Consumes(MediaType.APPLICATION_JSON)
    public void insert(Person p) throws IOException{
        Repository.getInstance().insert(p);
    }
    
    @DELETE
    @Path("delete/{id}")
    public void delete(@PathParam("id") long id) throws IOException{
        Repository.getInstance().delete(id);
    }
    
    @PUT
    @Path("update/{id}")
    public void update(@PathParam("id") long id, Person p) throws IOException{
        Repository.getInstance().update(p, id);
    }
    
    @GET
    @Path("count")
    public String count() throws IOException{
        return "{\"count\":"+Repository.getInstance().count()+"}";
    }
    */
}
