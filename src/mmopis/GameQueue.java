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
    ArrayList<Connection> waitingList;
    
    public GameQueue() {
        waitingList = new ArrayList<>();
    }
    
    public synchronized Connection [] join(Connection p){
        waitingList.add(p);
        if(waitingList.size()>=2){
            Connection [] a = new Connection[2];
            for (int i = 0; i < a.length; i++) {
                a[i] = waitingList.get(0);
                waitingList.remove(0);
            }
            return a;
        }
        return null;
    }
    public void exit(Connection p){
        waitingList.remove(p);
    }

}
