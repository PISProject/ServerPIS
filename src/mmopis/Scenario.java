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

    String getMap() {
        String map = "["+actors.length+"]";
        for (Summoner i : actors){
            map+=i.summonerId+","+i.posX+","+i.posY+"|";
        }
        return map;
    }
}
