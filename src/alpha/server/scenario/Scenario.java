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
    
    public Scenario(int ch1,int ch2,int ch3,int ch4){
        actores = new HashMap<>();

        actores.put(ch1, new Actor());
        actores.put(ch2, new Actor());
        actores.put(ch3, new Actor());
        actores.put(ch3, new Actor());
        
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
     * En adelante le pondremos tambien el tipo de heroe que crearemos. De momento solo tenmos uno
     * 
     */
    
    public void addHeroe(int uid /*, HeroType type*/) {
       actores.put(uid, new Actor());
    }

    public void moveTo(int uid, int x, int y) {
        actores.get(uid).moveTo(x,y);
    }
    
    public void attack(int uid){
        /* Funcion de ataqueProvisional*/
        Actor attacker = actores.get(uid);
        for(Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue();
            if (Math.abs(attacker.getPos()[0]-a.getPos()[0])< 10 && Math.abs(attacker.getPos()[1]-a.getPos()[1])< 10){
                //Los parametros 10,10 son el area que definimos para el ataque
                //TODO: Poner el range del ataque en el Actor
                if(a.isAttacked(attacker,0)==1){ //0 es ataque basico
                    //Tratar muerte
                }
            }
        }
        
    }
    
}