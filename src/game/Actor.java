/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author kirtash
 */
public class Actor {
    
    public enum ActorType{HERO, VILLAIN};
    public ActorType type;
    public int uid;
    
    //Actor location
    public float spawnX;
    public float spawnY;  
    public float posX;
    public float posY;
    
    //LookType
    public int model;
    
    //Stats
    public int attackDamage;
    public int defense;
    public int healthMax;
    public int health;
    public double speed = 1;

    

    
    public Actor(){
        this.posX = (float)Math.random()*10;
        this.posY= (float)Math.random()*10;
        this.spawnX = 0;
        this.spawnY = 0;
    }
    
    // Constructor provisional para heroes
    public Actor(int uid){
        this.type = ActorType.HERO;
        this.uid = uid;
        
        this.posX = (float)Math.random()*20;
        this.posY= (float)Math.random()*20;
    }
    
    // Constructor provisional para enemigos
    public Actor(int uid, int hp, int attack, double speed){
        this.type = ActorType.VILLAIN;
        this.speed = speed;
        this.attackDamage = attack;
        this.healthMax = hp;
        this.health = hp;
        this.uid = uid;
        this.posX = (float)Math.random()*20;
        this.posY= (float)Math.random()*20;
        
        this.spawnX = 0;
        this.spawnY = 0;
    }
    
    public Float [] getPos(){
        Float [] i = new Float[2];
        i[0] = posX;
        i[1] = posY;
        return i;
    }
    
    public Float [] getSpawn(){
        Float [] i = new Float[2];
        i[0] = spawnX;
        i[1] = spawnY;
        return i;
    }

    void moveTo(float x, float y) {
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
    
    public boolean isHero(){
        return (type == ActorType.HERO);
    }    
}
