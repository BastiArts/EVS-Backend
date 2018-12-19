package repository;

import entity.Equipment;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    
    
    public void add(Equipment equ){
        eList.add(equ);
        em.getTransaction().begin(); //Starts a transaction with the database
        em.persist(equ); //pushing the Object into DataBase
        em.getTransaction().commit(); //Finished with pushing
    }
    
    
    public List<Equipment> getEquipment() {
        return em.createNamedQuery("Equipment.findAll", Equipment.class)
                .getResultList();
        
        
    }
    
    
    

    /*List of Person
    LinkedList<Person> person = new LinkedList<>(); //Funktioniert wegen (siehe oben) import entity.Person

    String csvFile = "rest.csv";
    BufferedReader br = null;
    String line = "";

    public void loadFromFile() throws FileNotFoundException, IOException {

        br = new BufferedReader(new FileReader(csvFile));
        while ((line = br.readLine()) != null) {
            String[] p = line.split(";");

            person.add(new Person(Integer.parseInt(p[0]),
                    p[1],
                    p[2],
                    p[3],
                    p[4],
                    p[5],
                    Integer.parseInt(p[6]),
                    Boolean.parseBoolean(p[7])
            ));
        }
    }
    
    
    
    

    public List<Person> find() {
        return person;
    }
    
    
    
    public List<Person> find(int page, int size){
        LinkedList<Person> pHelp = new LinkedList<>();
        
        for (int i = 0; i < person.size(); i++) {
            if(i >= (page-1)*size && i < page*size){
                pHelp.add(person.get(i));
            }
        }
        return pHelp;
    }
    
    

    public void insert(Person p) {
        person.add(p);
    }

    public void delete(long id) {
        /*for (int i = 0; i < person.size(); i++) {
            if(id == person.get(i).getId()){
                person.remove(i);
            }
        }*//*
        person.removeIf((person) -> person.getId() == id);
    }

    public Person update(Person p, long id) {
        boolean found = false;
        for (int i = 0; i < person.size(); i++) {
            if (id == person.get(i).getId()) {
                this.person.set(i, p);
                found = true;
            }
        }
        if (!found) {
            person.add(p);
        }
        return p;
    }
    
    public int count(){
        return person.size();
    }*/

    
    
}
