/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import game.Actor;
import game.Scenario;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kirtash
 */
public class MonsterTest extends Thread{


    private enum MonsterState {LOOKING_FOR_TARGET, FOLLOWING_TARGET, WALKING_AROUND};
    
    // PARAMETROS FIJOS
    private int uid;
    
    private final int HP = 100;
    private final int ATTACK_DAMAGE=10;
    private final int SPEED = 1;
    private final int MODEL = 1;
    
    // --
    
    // PARAMETROS DE PARTIDA
    private Scenario scenario;
    
    
    // PARAMETROS DE COMPORTAMIENTO
    private MonsterState state;
    private int target; //uid del target
    private boolean alive;
    private double randomMovementAngle;
    private double stateChangeChance = 0.01; //Cada segundo
    //
    
    
    
    public MonsterTest(){
    }
    
    public Actor createMonster(int UID, Scenario scenario){ //Este podria ser un metodo abstracto
        this.uid = UID;
        this.scenario = scenario;
        alive = true;
        state = MonsterState.WALKING_AROUND;
        this.start();     
        return new Actor(uid, ATTACK_DAMAGE, HP, SPEED);
    }

    
    
    //////////////////// IA del monstruo ////////////////////////
    @Override
    public void run() {
        int clock;
        while(alive){
            System.err.println("ESTOY VIVO!");
            clock = 0;
            if (clock == 29 && state == MonsterState.WALKING_AROUND){
                randomMovementAngle = Math.random()*180;
                
            }
            if (clock == 29 && Math.random() < stateChangeChance && state != MonsterState.LOOKING_FOR_TARGET) {
                state = (state == MonsterState.FOLLOWING_TARGET)? MonsterState.WALKING_AROUND: MonsterState.LOOKING_FOR_TARGET;
            }
            switch(state){
                case LOOKING_FOR_TARGET:
                    target = scenario.lookForNearbyHero(uid, 3, 0);
                    state = MonsterState.FOLLOWING_TARGET;
                    break;
                case WALKING_AROUND:
                    scenario.moveTo(uid, (int)randomMovementAngle);
                case FOLLOWING_TARGET:
                    scenario.moveToTarget(uid, target);
                    
            }
            //
            clock++;
            clock %= 30;
            //
            
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MonsterTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /////////////////////////////////////////////////////////////////
    
}
