/*******************************************************************************
 * Machango Fight, the Massive Multiplayer Online.
 * Server Application
 * 
 * Curso 2012-2013
 * 
 * Este software ha sido desarrollado integramente para la asignatura 'Projecte
 * Integrat de Software' en la Universidad de Barcelona por los estudiantes
 * Pablo Martínez Martínez, Albert Folch, Xavi Moreno y Aaron Negrín.
 * 
 ******************************************************************************/
package server;

import database.MySQLConnection;

/**
 *
 * @author PabloMartinez
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

    /*
     * Devuelve: 0 si todo va bien
     * 1 si el nombre de usuario esta en uso
     * 2 si hay una excepcion en el SQL (Error de conexion con la DB)
     */
    public int register(String username, String password, String email) {
        if (db.isUsernameInUse(username)) return -1;
        return (db.addPlayer(username, password, email)) ? 0 : -2; 
    }

    public String getPlayerName(int uid) {
        return db.getUserName(uid);
    }
}
