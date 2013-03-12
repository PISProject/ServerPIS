/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.scenario;

/**
 *
 * @author kirtash
 */
public class Actor {
    public int uid;
    public int posX;
    public int posY;
    
    
    //Stats
    public int attackDamage;
    public int defense;
    public int healthMax;
    public int health;
    
    private int spawnX;
    private int spawnY;
    
    public Actor(){
        this.posX = 0;
        this.posY=0;
        
        this.spawnX = 0;
        this.spawnY = 0;
    }
    
    public Integer [] getPos(){
        Integer [] i = new Integer[2];
        i[0] = posX;
        i[1] = posY;
        return i;
    }
    
    public Integer [] getSpawn(){
        Integer [] i = new Integer[2];
        i[0] = spawnX;
        i[1] = spawnY;
        return i;
    }

    void moveTo(int x, int y) {
        this.posX = x;
        this.posY = y;
    }
    
    int healthChange(int hp){
        this.health+=hp;
        
        if(this.health>=this.healthMax){ // When the player it's healed
            this.health = this.healthMax;
        } 
        
        if(this.health<=0){ // When the player recieves damage
            return 0; //Muerto
        }
        return 1;
    }
    public int isAttacked(Actor attacker, int attackType){
        if (attackType == 0){
            return this.healthChange(attacker.attackDamage);
        }
        return 0;
    }
}