package repository;

import entity.Equipment;
import entity.User;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author H. Lackinger
 */
public class Repository {
    
    
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("evsPU");
    EntityManager em = emf.createEntityManager();
    
    //Singleton
    private static Repository instance = null;
    LinkedList<Equipment> eList = new LinkedList<>();

    private Repository() {
        //this.loadFromFile();
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }
    
    /*
    * Starts a transaction to communicate with the database and persists
    * an Object into the database. Finally a commit happens to make sure the
    * Object is pushing up into database
    */
    public void add(Object o){
        em.getTransaction().begin(); //Starts a transaction with the database
        em.persist(o); //pushing the Object into DataBase
        em.getTransaction().commit(); //Finished with pushing
    }
    
    /*
    * Is the same as:
    * SELECT e FROM evs_equipment e
    * This command gets all elements of Equipment.class in a List from database
    */
    public List<Equipment> getEquipment() {
        return em.createNamedQuery("Equipment.findAll", Equipment.class)
                .getResultList();
    }
    
    public Equipment insert(Equipment e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        return e;
    }
    
    /*
    * Is the same as:
    * SELECT u FROM evs_user u
    * This command gets all elements of User.class in a List from database
    */
    public List<User> getUsers() {
        return em.createNamedQuery("evs_user.findAll", User.class)
                .getResultList();
    }

    /*
    * Function to make sure the user on the front end uses correct username
    * and password for login into EVS
    */
    public String proofLogin(String username, String password) {
        List<User> userList = em
                .createNamedQuery("evs_user.findAll", User.class)
                .getResultList();
        
        for(User u : userList){
            if(u.getUsername().equals(username) 
                    && u.getHashedPassword().equals(password)){
                return "Welcome user: " + username;
            }
        }
        return "You did wrong dude";
    }
}
