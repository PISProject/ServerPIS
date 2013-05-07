/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

/**
 *
 * @author kirtash
 */
public class MonsterModel {
    public String name;
    public int hp;
    public int attack_damage;
    public double speed;
    public int model;
    public double changedir_prob;
    
    
    // PARAMETROS DE COMPORTAMIENTO

    public int target; //uid del target
    public double rand_movedir;
    public double stchange_rate; //Cada segundo
    
    
    public MonsterModel() {
        //---------
    }
    
    
  
}

