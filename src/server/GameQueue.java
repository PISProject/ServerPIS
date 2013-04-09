/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.Connection;
import java.util.ArrayList;

/**
 *
 * @author kirtash
 */
class GameQueue {
    private ArrayList<Connection> players;
    
    public GameQueue(){
        players = new ArrayList<>();
    }
    
    // Metodo que junta a los cuatro players. Es un metodo sincronizado para que
    // la lista no pueda ser modificada en la ejecucion del metodo.
    public synchronized Connection [] join(Connection uid){
       players.add(uid);
       if (players.size()>=4){
           Connection [] p = new Connection[4];
           for (int i = 0; i < 4; i++) {
               p[i]= players.remove(0);
           }
           return p;
       }
       return null;
    }
}
