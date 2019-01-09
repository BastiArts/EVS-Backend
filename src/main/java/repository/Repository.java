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
    
    
    public void add(Object o){
        //eList.add((Equipment) o);
        em.getTransaction().begin(); //Starts a transaction with the database
        em.persist(o); //pushing the Object into DataBase
        em.getTransaction().commit(); //Finished with pushing
    }
    
    
    public List<Equipment> getEquipment() {
        /*Select e from Equipment e --> Gets all elements from class Equipment
        * out of Database */
        return em.createNamedQuery("Equipment.findAll", Equipment.class)
                .getResultList();
    }
    
    
    
    public Equipment insert(Equipment e) {
        em.getTransaction().begin();
        em.persist(e);
        em.getTransaction().commit();
        return e;
    }
         
/**********************************************************
    **********************************************************
       ALL WHERE USER TEST DATAS ARE GENERATED AND EMPLOYED
    **********************************************************
     * @return p
    **********************************************************/
    
/*
    public String proofUser(User user) {
        
        List<User> userList = em.createNamedQuery("User.findAll", User.class)
                .getResultList();
        
        if(userList.contains(user)){
            return "User found! " + user.getUsername();
        }else{
            return "No user found!";
        }
        
    }
    */
    public List<User> getUsers() {
        return em.createNamedQuery("evs_user.findAll", User.class)
                .getResultList();
    }
}
