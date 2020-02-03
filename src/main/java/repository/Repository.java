package repository;

import entity.Entlehnung;
import entity.Equipment;
import entity.User;
import evs.ldapconnection.EVSColorizer;
import evs.ldapconnection.LdapAuthException;
import evs.ldapconnection.LdapException;
import evs.ldapconnection.LdapUser;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import util.EmailUtil;
import util.UserUtil;

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
     * @param e - Equipment
     */
    public void add(Equipment e) {
        em.getTransaction().begin(); //Starts a transaction with the database
        em.persist(e); //pushing the Object into DataBase
        em.getTransaction().commit(); //Finished with pushing
    }

    /**
     * Is the same as: SELECT e FROM evs_equipment e This command gets all
     * elements of Equipment.class in a List from database
     * @return List of Equiment
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
        return em.createQuery("SELECT equ FROM evs_equipment equ WHERE equ.id NOT IN (SELECT ent.equ.id FROM evs_entlehnung ent WHERE ent.status != :status)", Equipment.class).setParameter("status", "returned").getResultList();
    }

    public List getEquipmentFromUser(String username) {
        return em.createQuery("SELECT ent FROM evs_entlehnung ent WHERE ent.user.username = :username AND ent.status != :status1 AND ent.status != :status2", Equipment.class)
                .setParameter("username", username)
                .setParameter("status1", "returned")
                .setParameter("status2", "pending")
                .getResultList();
    }

    public Equipment getEquBySer(String seriennummer) {
        return em.createNamedQuery("Equipment.findSingleBySeriel", Equipment.class).setParameter("sernumber", seriennummer).getResultList().get(0);
    }

    /*                                              *\
                      USER METHODS
    \*                                              */
    /**
     * proofLogin sends 2 Strings to another Method in LDAP to compare
     *
     * @param username and
     * @param password with the HTL School LDAP (For user login)
     * @return Logged in User
     * @throws evs.ldapconnection.LdapException
     * @throws evs.ldapconnection.LdapAuthException
     */
    public User proofLogin(String username, String password) throws LdapException, LdapAuthException {
        try {
            LdapUser ldapUser = new LdapUser(username, password.toCharArray());
            allUsers = ldapUser.getAllStudents();
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
    public List<User> allUsers = new ArrayList<>();

    public List<User> getAllStudents() {
        System.out.println(EVSColorizer.RED + "USERS: " + allUsers.size() + EVSColorizer.RESET);
        return this.allUsers;
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
    public User updateUser(String username, String photoPath) {
        em.getTransaction().begin();
        User u = em.find(User.class, username);
        u.setPicturePath(photoPath);
        em.merge(u);
        em.getTransaction().commit();
        return u;
    }

    /*                                              *\
                   ENTLEHNUNG METHODS
    \*                                              *
    

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
    
    public List getAllAnfragen(){
        return em.createQuery("Select e from evs_entlehnung e where e.status=''").getResultList();
    }*/
    public Entlehnung findEntlehnung(long id) {
        return em.find(Entlehnung.class, id);
    }

    public List proofDateOfEquipmentReservation(long id) {
        return em.createQuery("SELECT e FROM evs_entlehnung e where e.equ.id = :id AND e.status = :status1 OR e.status = :status2")
                .setParameter("id", id)
                .setParameter("status1", "guarded")
                .setParameter("status2", "borrowed")
                .getResultList();
    }

    public Entlehnung makeNewEntlehnung(Entlehnung e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        return e;
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

    public List findPendingEntlehnungen() {
        return em.createQuery("Select e from evs_entlehnung e where e.status=:status").setParameter("status", "pending").getResultList();

    }

    public List getAllEntlehnungen() {
        return em.createQuery("SELECT e FROM evs_entlehnung e").getResultList();
    }

    public List findEntBySeriel(String serialnumber) {
        return em.createQuery("SELECT ent FROM evs_entlehnung ent WHERE ent.equ.serialnumber = :serial").setParameter("serial", serialnumber).getResultList();
    }

    public void findTeachers() {
        em.getTransaction().begin();
        Query q = em.createQuery("SELECT t from User t where t.isStudent = :STUDENT", User.class);
        q.setParameter("STUDENT", false);
        UserUtil.getInstance().setTeachers(q.getResultList());
        System.out.println(UserUtil.getInstance().getTeachers().size());
        em.getTransaction().commit();
    }
    public void updateEntlehnung(Entlehnung entl){
        em.getTransaction().begin();
            em.merge(entl);
        em.getTransaction().commit();
        
    }
}
