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
public class GameQueue{
    ArrayList <Player> waitingList;
    
    public Player [] join(Player p){
        waitingList.add(p);
        if(waitingList.size()>=2){
            Player [] a = new Player[2];
            a[0] = waitingList.get(0);
            waitingList.remove(0);
            a[1] = waitingList.get(1);
            waitingList.remove(2);
            return a;
        }
        return null;
    }
    public void exit(Player p){
        waitingList.remove(p);
    }

}
