package repository;

import entity.Entlehnung;
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


    /*                                              *\
                    EQUIPMENT METHODS
    \*                                              */
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

    /*
    * Method deletes existing Equipment if something is broken
    *
     */
    public Equipment delete(long id) {
        em.getTransaction().begin();
        Equipment equ = em.find(Equipment.class, id);
        em.remove(equ);
        em.getTransaction().commit();
        return equ;
    }

    /*
    * This Method updates a equipment
    * Maybe price or something else changes
     */
    public Equipment update(Equipment e) {
        em.getTransaction().begin();
        Equipment equ = em.merge(e);
        em.getTransaction().commit();
        return equ;
    }

    public Equipment getSingleEquipment(long id) {
        em.getTransaction().begin();
        Equipment e = em.find(Equipment.class, id);
        em.getTransaction().commit();
        return e;
    }

    public List getAvailableEquipment() {
        return em.createQuery("SELECT equ FROM evs_equipment equ WHERE equ.id NOT IN (SELECT ent.equ.id FROM evs_entlehnung ent WHERE ent.status != 'zurückgegeben')", Equipment.class).getResultList();
    }

    public List getEquipmentFromUser(String username) {
        return em.createQuery("SELECT ent.equ FROM evs_entlehnung ent WHERE ent.user.username = :username AND ent.status != 'zurückgegeben'", Equipment.class).setParameter("username", username).getResultList();
    }

    /*                                              *\
                      USER METHODS
    \*                                              */
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

    /*
    * This Method updates a user --> Method name is not good!
    *
     */
    public User insertUser(User u) {
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
        return u;
    }

    /*
    * Method updates an existing user
    *
     */
    public String updateUser(User u) {
        em.getTransaction().begin();
        em.merge(u);
        em.getTransaction().commit();
        return u.getFirstname() + " " + u.getLastname() + " is updated!";
    }

    public User findUser(String userid) {
        em.getTransaction().begin();
        User user = em.find(User.class, userid);
        em.getTransaction().commit();
        return user;
    }

    /*
    * Updates a user for changing Photopath
    *
     */
    public String updateUser(String username, String photoPath) {
        em.getTransaction().begin();
        User u = em.find(User.class, username);
        u.setPicturePath(photoPath);
        em.merge(u);
        em.getTransaction().commit();
        return "Photopath for user updated!";
    }

    /*                                              *\
                   ENTLEHNUNG METHODS
    \*                                              */
    public Entlehnung makeNewEntlehnung(Entlehnung e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        return e;
    }

    public Entlehnung findEntlehnung(long id) {
        return em.find(Entlehnung.class, id);
    }

    public Entlehnung confirmEntlehnung(Entlehnung e) {
        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();
        return e;
    }

    public String declineEntlehnung(Entlehnung e) {
        em.getTransaction().begin();
        em.remove(e);
        em.getTransaction().commit();
        return e.getId() + " was deleted";
    }

    public List findAllEntlehnungen() {
        return em.createQuery("SELECT e FROM evs_entlehnung e").getResultList();
    }
}
