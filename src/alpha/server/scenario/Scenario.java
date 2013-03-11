/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.scenario;

import java.util.HashMap;
import java.util.Map;

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
        String map = "";
        for (Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue();
            map+=a.uid+","+a.posX+","+a.posY+"*";
        }
        map = map.substring(0, map.length()-2);
        return map;
    }

    /*
     * En adelante le pondremos tambien el tipo de heroe que crearemos. De momento solo teenmos uno
     * 
     */
    
    public void addHeroe(int uid) {
       actores.put(uid, new Actor());
    }

    public void moveTo(int uid, int x, int y) {
        actores.get(uid).moveTo(x,y);
    }
    
}
