/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entity.User;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sebas
 */
public class UserUtil {
    private List<User> teachers = new LinkedList<>();
    
    private static UserUtil instance = null;
    
    public static UserUtil getInstance(){
        if(instance == null){
            instance = new UserUtil();
        }
        return instance;
    }

    private UserUtil() {
    }

    public List<User> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<User> teachers) {
        this.teachers = teachers;
    }
    
    
}
