/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import connections.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kirtash
 */
public class Scenario {
    public int monsterCount;
    public HashMap<Integer, Actor> actores;
    
    public Scenario(Connection [] connections){
        monsterCount = 0;
        actores = new HashMap<>();

        for (Connection c: connections) {
            actores.put(c.uid, new Actor(c.uid));
            
            
        }
        
    }
    public String parseScenario(){
        String map = "";
        for (Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue(); 
           map+=a.uid+","+a.posX+","+a.posY+"*";
        }
        map = map.substring(0, map.length()-1);
        return map;
    }

    /*
     * En adelante le pondremos tambien el tipo de heroe que crearemos. De momento solo tenmos uno
     * 
     */
    
    public void addHeroe(int uid /*, HeroType type*/) {
       actores.put(uid, new Actor());
    }

    public void moveTo(int uid, int angle) {
        angle -= 90; //<- Este factor hay que cambiarlo,
        float x, y;
        Actor a = actores.get(uid);
        double speed = a.speed;
        y = a.posY+(float) (Math.sin(Math.toRadians(angle))*speed);
        x = a.posX+(float) (Math.cos(Math.toRadians(angle))*speed);
        if (!checkCollision(uid,x, y)){
            a.moveTo(x,y);
        }
    }
    
    public void moveToTarget(int uid, int t_uid){
        Actor a1,a2;
        a1 = actores.get(uid);
        a2 = actores.get(t_uid);
        moveTo(uid, (int)(Math.atan2(a2.posY-a1.posY, a2.posX-a1.posX)*180/Math.PI));
    }
    
    public void attack(int uid, int range){
        /* Funcion de ataqueProvisional*/
        Actor attacker = actores.get(uid);
        for(Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue();
            if (Math.abs(attacker.getPos()[0]-a.getPos()[0])< range && Math.abs(attacker.getPos()[1]-a.getPos()[1])< range){
                //Los parametros 10,10 son el area que definimos para el ataque
                //TODO: Poner el range del ataque en el Actor
                if(a.isAttacked(attacker,0)==1){ //0 es ataque basico
                    //Tratar muerte
                }
            }
        }
        
    }
    public boolean checkCollision(int uid, float x,float y){
        Actor a;
        for (Map.Entry entry : actores.entrySet()) {
            a = (Actor)entry.getValue();
            if (a.uid!= uid &&(Math.abs(a.posX-x) < 2 && Math.abs(a.posY-y) < 2)){
                return true;
                
            }
        }
        return false;
        
    }

    /**
     * Param se refiere a que tipo de busqueda tiene que realizar.
     * 
     * @param uid
     * @param dist 
     * @param PARAM
     * @return 
     */
    private static final int LOWEST_HEALTH=1;
    public int lookForNearbyHero(int uid, int dist, int PARAM) {
        
        ArrayList<Integer> lista = new ArrayList<>();
        double x = actores.get(uid).posX;
        double y = actores.get(uid).posY;

        Actor a;
        for (Map.Entry actor : actores.entrySet()){
            a = (Actor)actor.getValue();
            if (Math.abs(a.posX-x)<dist && Math.abs(a.posY-y)<dist && a.uid != uid && a.isHero()) lista.add(uid);
            
            //Empezamos la busqueda de parametros dentro de los actores que estan en rango
            int health=Integer.MAX_VALUE, a_health;
            int f_uid=0;
            
            if (PARAM == LOWEST_HEALTH){ //Retorna el actor con menos vida de los que estan en rango
                for (int i = 0; i < lista.size(); i++) {
                    a_health = actores.get(lista.get(i)).health;
                    if (health > a_health && a_health > 0){
                        f_uid = lista.get(i);
                        health = actores.get(uid).health;
                    }
                    return f_uid;
                }
            } 
            
            else{ // Retorna un actor aleatorio de los que estan en rango
                return lista.get(((int)Math.random()*10)%(lista.size()-1));
            }
        }
        return -1;
    }

    void addMonster(Actor c_creature) {
        actores.put(c_creature.uid, c_creature);
    }
    
}
