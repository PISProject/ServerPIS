/*
 * 
 */
package mmopis;

/**
 *
 * @author kirtash
 */
public class Summoner {
    
    public static int NEXT_ID = 0;
    
    public int summonerId;
    public String summonerName;
    
    public float[] pos;
    
    public Summoner(int playerid){
        this.summonerId = playerid;
        pos = new float[2];
    }
    
    public synchronized static int getNextId() {
        NEXT_ID++;
        return NEXT_ID;
    }
    
    
}
