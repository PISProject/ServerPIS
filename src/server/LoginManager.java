/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.MySQLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author kirtash
 */
public class LoginManager {
    private MySQLConnection db;
    
    public LoginManager(MySQLConnection con){
        this.db = con;
    }
    
    public int login(String name, String password){
        int id = db.getUserId(name);
        if(id != -1){
            if(db.getPassword(id).equals(password)){
                return id;
            }
        }
        return -1;
    }
}
