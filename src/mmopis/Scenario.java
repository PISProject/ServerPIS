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
        return getMap(); //Por ahora e un getMap, pero si implementamos diferentes players, habrá que hacer un parser diferente.
    }

    public String getMap() {
        String map = actors.length+"&";
        for (Summoner i : actors){
            map+=i.summonerId+","+i.pos[0]+","+i.pos[1]+"*";
        }
        return map;
    }
}
