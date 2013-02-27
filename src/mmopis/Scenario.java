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
    private Summoner [] actors;
    public Scenario(Summoner[] summoners){
        actors = summoners;
    }
    public String getInit(){
        return "";
    }

    public String getMap() {
        String map = "["+actors.length+"]";
        for (Summoner i : actors){
            map+=i.summonerId+","+i.pos[0]+","+i.pos[1]+"|";
        }
        return map;
    }
}
