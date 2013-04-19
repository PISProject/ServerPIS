/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monsters;

import game.Actor;

/**
 *
 * @author kirtash
 */
public abstract class Monster{
    public enum MonsterState {STANDING,ATTACKING,DEAD};
    public MonsterState state;
    public int uid;
    public int model;
    public int hp;
    public int attack;
    
    public Actor createMonster(){
        return new Actor(uid, hp, attack, 0);
    }
    public void attack(){
        
    }
    public abstract void onDie();
    public abstract void spawn();
    
}
