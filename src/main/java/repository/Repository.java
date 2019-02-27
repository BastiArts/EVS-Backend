package repository;

import entity.Equipment;
import entity.User;
import evs.ldapconnection.EVSBridge;
import evs.ldapconnection.LdapAuthException;
import evs.ldapconnection.LdapException;
import evs.ldapconnection.LdapUser;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author M. Fadljevic
 */
public class Repository {
    
    EVSBridge ldap = EVSBridge.getInstance();
    
    LdapUser userLdpa;
    
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
    
    /**
    * Starts a transaction to communicate with the database and persists
    * an Object into the database. Finally a commit happens to make sure the
    * Object is pushing up into database
    */
    public void add(Equipment e){
        em.getTransaction().begin(); //Starts a transaction with the database
        em.persist(e); //pushing the Object into DataBase
        em.getTransaction().commit(); //Finished with pushing
    }
    
    /**
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
    
    /**
    * Is the same as:
    * SELECT u FROM evs_user u
    * This command gets all elements of User.class in a List from database
    *
    public List<User> getUsers() {
        return em.createNamedQuery("evs_user.findAll", User.class)
                .getResultList();
    }
    */
    /**
    * proofLogin sends 2 Strings to another Method in LDAP to compare 
    * @param username and
    * @param password with the HTL School LDAP (For user login)
    */
    public User proofLogin(String username, String password) throws LdapException, LdapAuthException {
        try{
            LdapUser lUser = new LdapUser(username, password.toCharArray());            
            return new User(lUser.getUserId(), lUser.getFirstname(), lUser.getLastname(), lUser.getClassId(), lUser.isStudent());
            
        }catch (Exception e){
            return null;
        }
           
    }
    
    
}
