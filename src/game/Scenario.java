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
    
    public Scenario(GameEngine eng, Connection [] connections){
        monsterCount = 0;
        actores = new ConcurrentHashMap<>();
        attackPool = new ConcurrentLinkedQueue<>();
        this.eng = eng;
        for (Connection c: connections) {
            actores.put(c.uid, new Actor(c.uid));
            
            
        }
        
    }
    public String parseScenario(){
        String map = "";
        for (Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue(); 
           map+=a.uid+","+a.model+","+a.health+","+a.posX+","+a.posY+"*";
        }
        map+= "/";
        if (!attackPool.isEmpty()){
            int j = attackPool.size();
            for(int i = 0; i <j; i++){
                Attack pro = attackPool.poll();
                map+=pro.type+","+pro.caster+"*";
                System.out.println(pro.type+","+pro.caster);
            }
           
        }
        return map;
    }

    /*
     * En adelante le pondremos tambien el tipo de heroe que crearemos. De momento solo tenmos uno
     * 
     */
    
    public void addHeroe(int uid /*, HeroType type*/) {
       actores.put(uid, new Actor());
    }

    public int moveTo(int uid, int angle) {
        float x, y;
        Actor a = actores.get(uid);
        double speed = a.speed;
        y = a.posY+(float) (Math.sin(Math.toRadians(angle))*speed);
        x = a.posX+(float) (Math.cos(Math.toRadians(angle))*speed);
       if (!checkCollision(uid,x, y)){
            a.moveTo(x,y);
            return 0;
        }
       return -1;
    }
    
    public void attack(Attack attack){
        /* Funcion de ataqueProvisional*/
        System.out.println("ATAQUE");
        attackPool.add(attack);
        Actor attacker = actores.get(attack.caster);
        for(Map.Entry actor : actores.entrySet()) {
            Actor a = (Actor)actor.getValue();
            if (Math.abs(attacker.getPos()[0]-a.getPos()[0])< attack.range && Math.abs(attacker.getPos()[1]-a.getPos()[1])< attack.range){
                
                if(a.isAttacked(attacker,0)==1){ //0 es ataque basico
                    /* Aqui se trata la muerte del personaje*/
                    onDie(a);
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

    void addMonster(Actor c_creature) {
        actores.put(c_creature.uid, c_creature);
    }
    
    /*???*/
    void killAll() {
        //for(Actor)
    }

    private void onDie(Actor a) {
    if (a.isHero()){
        eng.onPlayerDeath(a.uid);
        }
    else {
        eng.onMonsterDeath(a.uid);
    }
    actores.remove(a.uid);
    }
}
