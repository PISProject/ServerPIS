/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.main;

import alpha.server.connection.Connection;
import java.util.ArrayList;


/**
 *
 * @author kirtash
 */
public class GameQueue {
    private ArrayList<Connection> queue;
    
    protected GameQueue() {
        queue = new ArrayList<>();
}
    /**
     * Nos permite entrar en la cola de juego.
     * @param con
     * @return 
     */
    public Connection [] join(Connection con){ 
        queue.add(con);
        if(queue.size()>=4){
            Connection [] game = new Connection[4];
            for (int i = 0; i <= 4; i++) {
                game[i] = queue.remove(1);
            }
            return game;
        }
        return null;
    }

    boolean quitQueue(Connection c) {
        if (queue.contains(c)){
            queue.remove(c);
            return true; //Ha podido dejar la cola
        }
        return false; //No ha podido dejar la cola
    }
    
}
