/*******************************************************************************
 * Machango Fight, the Massive Multiplayer Online.
 * Server Application
 * 
 * Curso 2012-2013
 * 
 * Este software ha sido desarrollado integramente para la asignatura 'Projecte
 * Integrat de Software' en la Universidad de Barcelona por los estudiantes
 * Pablo Martínez Martínez, Albert Folch, Xavi Moreno y Aaron Negrín.
 * 
 ******************************************************************************/

package server;

import connections.Connection;
import java.util.ArrayList;


/**
 *
 * @author PabloMartinez
 */
class GameQueue {
    public int game_type;
    public int game_size;
    private final ArrayList<Connection> players = new ArrayList<>();
    
    public GameQueue(int game_type, int game_size){
        this.game_size = game_size;
        this.game_type = game_type;
    }
    
    // Metodo que junta a los cuatro players. Es un metodo sincronizado para que
    // la lista no pueda ser modificada en la ejecucion del metodo.
    public synchronized void join(Connection uid){
        players.add(uid);
        if (players.size()>=game_size){
            Connection [] p = new Connection[game_size];
            for (int i = 0; i < game_size; i++) {
                p[i]= players.remove(0);
            }
            startGame(p);
        }
    }

    boolean quit(Connection con) {
        synchronized(players){
            return players.remove(con);
        }
    }

    boolean isConnectionInQueue(Connection con) {
        return players.contains(con);
    }
    
    private void startGame(Connection[] game) {
        MFServer.SERVER.createGame(game_type,game);
     }   
}
