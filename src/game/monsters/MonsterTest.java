/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import game.Actor;
import game.Scenario;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.MFServer;

/**
 *
 * @author kirtash
 */
public class MonsterTest extends Thread{


    private enum MonsterState {LOOKING_FOR_TARGET, FOLLOWING_TARGET, WALKING_AROUND};
    
    // PARAMETROS FIJOS
    private int uid;
    
    private final int hp = 100;
    private final int attack_damage=10;
    private final double speed = 0.3;
    private final int model = 1;
    private double changedir_prob = 0.333;
    
    // --
    
    // PARAMETROS DE PARTIDA
    private Scenario scenario;
    
    
    // PARAMETROS DE COMPORTAMIENTO
    private MonsterState defaultstate= MonsterState.WALKING_AROUND;
    private MonsterState state;

    private int target; //uid del target
    private boolean alive;
    private double rand_movedir;
    private double stchange_rate = 0.01; //Cada segundo
    //
    
    
    
    public MonsterTest(){
    }
    
    public Actor createMonster(int UID, Scenario scenario){ //Este podria ser un metodo abstracto
        this.uid = UID;
        this.scenario = scenario;
        alive = true;
        state = defaultstate;
        this.start();     
        return new Actor(uid, attack_damage, hp, speed);
    }

    
    
    //////////////////// IA del monstruo ////////////////////////
    @Override
    public void run() {
        int clock;
        clock = 0;
        double randnum;
        while(alive){
            randnum = Math.random();
            
            // Gestion de cambio de estado
            if (randnum < stchange_rate && state != MonsterState.LOOKING_FOR_TARGET){
                state = (state == MonsterState.FOLLOWING_TARGET)? MonsterState.WALKING_AROUND : MonsterState.FOLLOWING_TARGET;
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
                Logger.getLogger(MonsterTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /////////////////////////////////////////////////////////////////
   
    private void lookForATarget(){
        target = scenario.lookForNearbyHero(uid, 10, 0);
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
}
