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
   
    public static final int PLAYERS_PER_MAP = 4; //Solo hay que cambiar esto si queremos que en una partida haya más players
    
    private ArrayList<Connection> waitingList;
    
    public GameQueue() {
        waitingList = new ArrayList<>();
    }
    
    public synchronized Connection [] join(Connection p){
        waitingList.add(p);
        if(waitingList.size()>=PLAYERS_PER_MAP){
            Connection [] a = new Connection[PLAYERS_PER_MAP];
            for (int i = 0; i < a.length; i++) {
                a[i] = waitingList.get(0);
                waitingList.remove(0);
                if(!a[i].isAlive()) { //En caso que uno de los threads esté muerto, no se considera que puede empezarse la partida.
                    return null;
                }
            }
            return a;
        }
        return null;
    }
    public void exit(Connection p){
        waitingList.remove(p);
    }

}
