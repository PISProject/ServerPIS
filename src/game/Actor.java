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

/**
 *
 * @author PabloMartinez
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
    public double lookangle;
    
    //LookType
    public int model;
    
    //Stats
    public int attackDamage;
    public int defense;
    public int healthMax;
    public int health;
    public double speed = 1;
    
    public double attack_range;

//    Contamos los monstruos que mata
    public int killed_creatures;

    
    public Actor(){
        this.posX = (float)Math.random()*10;
        this.posY= (float)Math.random()*10;
        this.spawnX = 0;
        this.spawnY = 0;
    }
//    
//    // Constructor provisional para heroes
    public Actor(int uid){
        this.type = ActorType.HERO;
        this.uid = uid;
        this.health = 100;
        
        this.attackDamage = 10;
        this.posX = (float)Math.random()*20;
        this.posY= (float)Math.random()*20;
    }
    
//    // Constructor provisional para enemigos
//    public Actor(int uid, int hp, int attack, double speed){
//        this.type = ActorType.VILLAIN;
//        this.speed = speed;
//        this.attackDamage = attack;
//        
//        
//        this.healthMax = hp;
//        this.health = hp;
//        this.uid = uid;
//        this.posX = (float)Math.random()*20;
//        this.posY= (float)Math.random()*20;
//        
//        this.spawnX = 0;
//        this.spawnY = 0;
//    }
    
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

    void moveTo(double look, float x, float y) {
        this.lookangle = look;
        this.posX = x;
        this.posY = y;
    }
    
    int healthChange(int damage){
        this.health-=damage;
        System.err.println(this.health);
//        if(this.health>=this.healthMax){ // When the player it's healed
//            this.health = this.healthMax;
//        } 
        
        if(this.health<=0){ // When the player recieves damage
            return 0; //Muerto
        }
        return 1;
    }
    
    public int isAttacked(Attack a){
        return this.healthChange(a.caster.attackDamage);

    }
    
    public boolean isHero(){
        return (type == ActorType.HERO);
    }    
}
