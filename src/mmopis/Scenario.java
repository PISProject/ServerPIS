/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.util.ArrayList;

/**
 *
 * @author kirtash
 */
public class Scenario {
    private static final float POSXINIC = 10;
    private static final float POSYINIC = 10;
    
    private ArrayList<Actor> actors;
    private ArrayList<Item> items; //TODO
    public Scenario(Actor[] m_actors){
        actors = new ArrayList();
        for (Actor a: m_actors){
            actors.add(a);
            //SET STARTING POSITIONS
            
        }
    }

    public synchronized String getMap() {
        String map = "";
        for (int i = 0; i < actors.size(); i++) {
            Actor s = actors.get(i);
            map+=s.uid+","+s.posX+","+s.posY+((i<actors.size()-1)?"*":"");
        }
        return map;
    }
}
