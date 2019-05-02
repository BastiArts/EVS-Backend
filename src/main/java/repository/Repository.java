package repository;

import entity.Equipment;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import evs.ldapconnection.LdapAuthException;
import evs.ldapconnection.LdapException;
import evs.ldapconnection.LdapUser;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author M. Fadljevic
 */
public class Repository {

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
     * Starts a transaction to communicate with the database and persists an
     * Object into the database. Finally a commit happens to make sure the
     * Object is pushing up into database
     */
    public void add(Equipment e) {
        em.getTransaction().begin(); //Starts a transaction with the database
        em.persist(e); //pushing the Object into DataBase
        em.getTransaction().commit(); //Finished with pushing
    }

    /**
     * Is the same as: SELECT e FROM evs_equipment e This command gets all
     * elements of Equipment.class in a List from database
     */
    public List<Equipment> getEquipment() {
        return em.createNamedQuery("Equipment.findAll", Equipment.class)
                .getResultList();
    }

    /**
     * Is the same as: SELECT u FROM evs_user u This command gets all elements
     * of User.class in a List from database
     *
     * public List<User> getUsers() { return
     * em.createNamedQuery("evs_user.findAll", User.class) .getResultList(); }
     */
    /**
     * proofLogin sends 2 Strings to another Method in LDAP to compare
     *
     * @param username and
     * @param password with the HTL School LDAP (For user login)
     * @return
     * @throws evs.ldapconnection.LdapException
     * @throws evs.ldapconnection.LdapAuthException
     */
    public User proofLogin(String username, String password) throws LdapException, LdapAuthException {
        try {
            LdapUser ldapUser = new LdapUser(username, password.toCharArray());
            User user = new User(ldapUser.getUserId(), ldapUser.getFirstname(), ldapUser.getLastname(), ldapUser.getClassId(), ldapUser.isStudent());
            System.out.println(user.getUsername() + " hat den Namen: " + user.getFirstname());
            return user;
            //return true;
        } catch (LdapAuthException | LdapException e) {
            System.out.println(EVSColorizer.YELLOW + "Nothing happened!" + EVSColorizer.reset());
            e.printStackTrace();
            return null;
        }

    }

    public User insertUser(User u) {
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
        return u;
    }

    public Equipment delete(Equipment e) {
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
        return e;
    }

    public Equipment update(Equipment e) {
        em.getTransaction().begin();
        Equipment equ = em.merge(e);
        em.getTransaction().commit();
        return equ;
    }

    public String updateUser(User u) {
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
        return u.getFirstname() + " " + u.getLastname() + " is updated!";
    }

    public List getUserEquipment(String username) {
        return em.createNamedQuery("Equipment.findUserEquipment").setParameter("userId", username).getResultList();
    }
    
    public List getAvailableEquipment(){
        return em.createNamedQuery("Equipment.available").getResultList();
    }
}
