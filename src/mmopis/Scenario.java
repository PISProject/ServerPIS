/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author kirtash
 */
public class Scenario {
    private static final float POSXINIC = 10;
    private static final float POSYINIC = 10;
    
    private Summoner [] actors;
    public Scenario(Summoner[] summoners){
        actors = summoners;
        for (Summoner s: actors){
            s.pos[0] = POSXINIC;
            s.pos[1] = POSYINIC;
        }
    }
    public String getInit(){
        String map = "";
        for (int i = 0; i < actors.length; i++) {
            Summoner s = actors[i];
            map+=s.summonerId+","+s.pos[0]+","+s.pos[1]+((i<actors.length-1)?"*":"");
        }
        return map;
    }

    public String getMap() {
        String map = "";
        for (int i = 0; i < actors.length; i++) {
            Summoner s = actors[i];
            map+=s.summonerId+","+s.pos[0]+","+s.pos[1]+((i<actors.length-1)?"*":"");
        }
        return map;
    }
}
