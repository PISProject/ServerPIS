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

package game;

import connections.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author PabloMartinez
 */
public class Scenario {
    public int monsterCount;
    public ConcurrentHashMap<Integer, Actor> actores;
    public GameEngine eng;
    public ConcurrentLinkedQueue<Attack> attackPool;
    public int radius;
    
    public Scenario(GameEngine eng, Connection [] connections, int radius){
        monsterCount = 0;
        actores = new ConcurrentHashMap<>();
        attackPool = new ConcurrentLinkedQueue<>();
        this.eng = eng;
        this.radius = radius;
        for (Connection c: connections) {
         
            actores.put(c.uid, new Actor(c.uid, c.name, radius));
            
            
        }
        
    }   
    public String parseScenario(){
        String map = "";
        for (Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue(); 
           if (a.health>0) map+=a.uid+","+a.name+","+a.model+","+a.health+","+a.posX+","+a.posY+"*";
        }
        map+= "/";
        if (!attackPool.isEmpty()){
            int j = attackPool.size();
            for(int i = 0; i <j; i++){
                Attack pro = attackPool.poll();
                map+=pro.type+","+pro.caster.uid+"*";
            }
           
        }
        return map;
    }

    /*
     * En adelante le pondremos tambien el tipo de heroe que crearemos. De momento solo tenmos uno
     * 
     */
    

    public int moveTo(int uid, int angle) throws NullPointerException{
        float x, y;
        Actor a = actores.get(uid);
        if (a == null){ return -2;}
        double speed = a.speed;
        y = a.posY+(float) (Math.sin(Math.toRadians(angle))*speed);
        x = a.posX+(float) (Math.cos(Math.toRadians(angle))*speed);
       if (!checkCollision(uid,x, y)){
            a.moveTo(angle,x,y);
            return 0;
        }
       return -1;
    }
    
    public synchronized void attack(Attack attack){
        /* Funcion de ataqueProvisional*/
        attackPool.add(attack);
        Actor attacker = (attack.caster);
        for(Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue();
            
//            Con este if evitamos el fuego amigo
//            if (attack.caster.type != attacker.type){
                if ( a.uid != attacker.uid &&(Math.abs(attack.center[0]-a.getPos()[0])< attack.range && Math.abs(attack.center[1]-a.getPos()[1])< attack.range)){
                    if(a.health > 0 && a.isAttacked(attack)==0){ //0 es ataque basico
                        /* Aqui se trata la muerte del personaje*/

                        //attacker.killed_creatures++;
                        attack.caster.killed_creatures++;
                        onDie(a);
                    }
//                }
            }
        }
        
    }

    public boolean checkCollision(int uid, float x,float y){
        Actor a;
//        Comprobamos que este dentro del escenario
        if (Math.sqrt((x*x)+(y*y))>radius) {
            return true;
        }
        
//        Comprobamos que no este chocando con otro actor
        for (Map.Entry entry : actores.entrySet()) {
            a = (Actor)entry.getValue();
            if (a.health >0 && a.uid!= uid &&(Math.abs(a.posX-x) < 2 && Math.abs(a.posY-y) < 2)){
                return true;
                
            }
        }
        return false;
        
    }

    void addMonster(Actor c_creature) {
        actores.put(c_creature.uid, c_creature);
    }
    
    /*???*/
    void killAll() {
        //for(Actor)
    }

    private void onDie(Actor a) {
        eng.onDeath(a);
    }

    public String getScores() {
        String s="";
        for (Map.Entry ent : actores.entrySet()){
            Actor a = (Actor)ent.getValue();
            if (a.isHero()){
                s += a.name + "," + a.deaths + "," + a.killed_creatures + "*";
            }
        }
        return s;
    }
}
