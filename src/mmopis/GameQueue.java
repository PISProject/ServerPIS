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
    ArrayList <Connection> waitingList;
    
    public Connection [] join(Connection p){
        waitingList.add(p);
        if(waitingList.size()>=2){
            Connection [] a = new Connection[2];
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
