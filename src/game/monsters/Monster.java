/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import game.Actor;
import game.Scenario;
import static java.lang.Thread.sleep;
import server.MFServer;

/**
 *
 * @author kirtash
 */
public class Monster extends Thread{

    
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
        return new Actor(UID, attack_damage, hp, speed);
    }
    public void kill(){
        alive = false;
    }
    
    private void attack(){
        scenario.attack(uid, attack_range);
    }
    private boolean isTargetInAttackRange(){
        Actor a_this = scenario.actores.get(uid);
        Actor a = scenario.actores.get(target);
        // Retorna un boleano de si el target entra en el rango de ataque.
        return (Math.abs(a.posX-a_this.posX) < attack_range && Math.abs(a.posY-a_this.posY) < attack_range);
    }
    
    private void lookForATarget(){
        /*target = scenario.lookForNearbyHero(uid,10);*/
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
                    case WALKING_AROUND:
                        if (randnum<changedir_prob){
                            rand_movedir = Math.random()*180;
                            if (MFServer.DEBUG_MONSTERS){
                                System.err.println("==> [MONSTER "+uid+"] New direction: "+rand_movedir);
                            }
                        }
                        scenario.moveTo(uid, (int)rand_movedir);
                        break;
                    case FOLLOWING_TARGET:
                        scenario.moveToTarget(uid, target);
                        break;

                }
                //
                clock++;
                clock %= 101;
                //

                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    /////////////////////////////////////////////////////////////////
}
