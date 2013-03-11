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
    private int uid;
    private int posX;
    private int posY;
    
    private int spawnX;
    private int spawnY;
    
    public Actor(){
        this.posX = 0;
        this.posY=0;
        
        this.spawnX = 0;
        this.spawnY = 0;
    }
    
    private Integer [] getPos(){
        Integer [] i = new Integer[2];
        i[0] = posX;
        i[1] = posY;
        return i;
    }
    
    private Integer [] getSpawn(){
        Integer [] i = new Integer[2];
        i[0] = spawnX;
        i[1] = spawnY;
        return i;
    }
}
