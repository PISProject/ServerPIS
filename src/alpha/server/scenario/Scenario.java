/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.scenario;

import java.util.HashMap;

/**
 *
 * @author kirtash
 */
public class Scenario {
    HashMap<Integer, Actor> actores;
    
    public Scenario(){
        actores = new HashMap<>();
        
    }
    public String parseScenario(){
        //TODO
        return "";
    }

    /*
     * En adelante le pondremos tambien el tipo de heroe que crearemos. De momento solo teenmos uno
     * 
     */
    
    public void addHeroe(int uid) {
       actores.put(uid, new Actor());
    }
    
}
