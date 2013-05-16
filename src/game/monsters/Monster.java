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

package game.monsters;

import game.Actor;
import game.Scenario;
import static java.lang.Thread.sleep;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import server.MFServer;

/**
 *
 * @author PabloMartinez
 */
public class Monster extends Thread{
    private static long EXHAUST_TIME = 1000;

    
    public Monster(){
        //---- Para poder cargar el monstruo hay que utilizar el metodo createMonster(-,-,-)
    }

    private enum MonsterState {LOOKING_FOR_TARGET, FOLLOWING_TARGET, WALKING_AROUND, DEAD,};
    
    private boolean created = false;
    // PARAMETROS FIJOS
    private int uid;
    
    private int hp;
    private int attack_damage;
    private double speed;
    private int model;
    private double changedir_prob;
    private double changetarget_prob;
    private double attack_exhaust;
    private double attack_range;
    private boolean exhausted;
    private Timer exhaustion;
    // --
    
    // PARAMETROS DE PARTIDA
    private Scenario scenario;
    
    
    // PARAMETROS DE COMPORTAMIENTO
    private MonsterState defaultstate= MonsterState.WALKING_AROUND;
    private MonsterState state;

    private int target; //uid del target
    private boolean alive;
    private double rand_movedir;
    private double stchange_rate;
    //
    
    public Actor createMonster(int UID, Scenario scenario, MonsterModel model){ //Este podria ser un metodo abstracto
        
        // Model stats
        this.hp = model.hp;
        this.attack_damage = model.attack_damage;
        this.speed = model.speed;
        this.changedir_prob = model.changedir_prob;
        this.rand_movedir = model.rand_movedir;
        this.stchange_rate = model.stchange_rate;
        // -- 
        
        this.uid = UID;
        this.scenario = scenario;
        alive = true;
        this.created = true;
        state = defaultstate;    
        exhaustion = new Timer();
        return new Actor(UID, attack_damage, hp, speed);
        
    }
    
    /*
     * addExhaust() -- Añadimos el tiempo de exhaust, necesario entre ataque y
     * ataque.
     */
    public void addExhaust(){
        exhaustion.schedule(new TimerTask() {

            @Override
            public void run() {
                exhausted = false;
            }
        }, EXHAUST_TIME);
    }
    
    /*
     * monsterDeath() -- Gestion de lo que pasa cuando un monstruo muere.
     */
    public void monsterDeath(){
        alive = false;
    }
    
    /*
     * attack() -- El monstruo ataca, comprobamos que haya pasado el intervalo
     * de exhaust.
     */
    private boolean attack(){
        if (!exhausted){
            scenario.attack(uid, attack_range);
            exhausted = true;
            return exhausted;
        }
        return false;
    }
    private boolean isTargetInAttackRange(){
        Actor a_this = scenario.actores.get(uid);
        Actor a = scenario.actores.get(target);
        // Retorna un boleano de si el target entra en el rango de ataque.
        return (Math.abs(a.posX-a_this.posX) < attack_range && Math.abs(a.posY-a_this.posY) < attack_range);
    }
    
    public int moveToTarget(int uid, int t_uid){
        Actor a1,a2;
        a1 = scenario.actores.get(uid);
        a2 = scenario.actores.get(t_uid);
        int angle =(int) Math.toDegrees(Math.atan2((a2.posY-a1.posY),(a2.posX-a1.posX)));
        angle = (360-angle)%360;
        return scenario.moveTo(uid, angle-90);
        
    }
 
    private void attackTarget(){
        if (isInRange(scenario.actores.get(uid), scenario.actores.get(target), (int)attack_range));
    }
    
    private void lookForATarget(){
        target = lookForNearbyHero(uid,10);
        if (target != -1){
          state = MonsterState.FOLLOWING_TARGET;
          
          
          if (MFServer.DEBUG_MONSTERS){
              System.err.println("==> [MONSTER "+uid+"] Target found! starting to follow "+target);
          }
          
        }
        else{
            state =MonsterState.WALKING_AROUND;
            if (MFServer.DEBUG_MONSTERS){
            System.err.println("==> [MONSTER "+uid+"] no target found. Stay in randomMove "+target);
          }
        }      
    }
    
    /*
     * lookForNearbyHero(uid1,dist) -- Buscamos heroes que se encuentren a una
     * distancia < 'dist'.
     */
    private int lookForNearbyHero(int uid, int dist) {
        /* Buscamos el mas cercano */
        ConcurrentHashMap<Integer,Actor> a = scenario.actores;
        Actor me = a.get(uid);
        for(Map.Entry m : a.entrySet()){
            if (!((int)m.getKey()== uid) && ((Actor)m.getValue()).isHero() && isInRange(me,(Actor) m.getValue(),dist)){
                return (int)m.getKey();
            }
        }
        return -1;
    }
    
    /*
     * isInRange(a1,a2,range) -- Comprueba si dos actores se encuentran, o no,
     * a una distancia <= range.
     */
    private boolean isInRange(Actor a1, Actor a2, int range){
        return (Math.abs(a1.posX-a2.posX) < range && Math.abs(a1.posY-a2.posY)< range);
    }
    
    
    //////////////////// IA del monstruo ////////////////////////
    @Override
    public void run() {
        int clock;
        clock = 0;
        double randnum;
        if (created){
            while(alive){
                randnum = Math.random();
                // Gestion de cambio de estado
                if (randnum < stchange_rate && state != MonsterState.LOOKING_FOR_TARGET){
                    state = (state == MonsterState.LOOKING_FOR_TARGET)? MonsterState.WALKING_AROUND : MonsterState.LOOKING_FOR_TARGET;
                    if (MFServer.DEBUG_MONSTERS){

                        System.err.print("==> [MONSTER "+uid+"] Switching state");
                    }
                }

                switch(state){
                    case LOOKING_FOR_TARGET:
                        lookForATarget();
                        break;
                    case WALKING_AROUND:/*
                        if (randnum<changedir_prob){
                            rand_movedir = Math.random()*360;
                            if (MFServer.DEBUG_MONSTERS){
                                System.err.println("==> [MONSTER "+uid+"] New direction: "+rand_movedir);
                            }
                        }
                        scenario.moveTo(uid, (int)rand_movedir);*/
                        break;
                    case FOLLOWING_TARGET:
                        // Si colisionamos y estamos en rango de ataque con el objetivo atacamos
                        int st = moveToTarget(uid, target);
                        /*if(st == -1 && isTargetInAttackRange()) {
                            attack();
                        }
                        // Si hay colision 
                        else if (st == 0){
                            state = MonsterState.WALKING_AROUND;
                        }*/
                        
                        break;

                }
                //
                clock++;
                clock %= 101;
                //

                try {
                    /* El tiempo de actuacion del Thread es igual al delay entre
                     * las conexiones, para que no se aprecie.
                     */
                    sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////
}
