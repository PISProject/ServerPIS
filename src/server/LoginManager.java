/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.MySQLConnection;

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
                if (!MFServer.SERVER.isClientOnline(id)){
                    return id;
                }
                return -2;
            }
        }
        return -1;
    }
}
