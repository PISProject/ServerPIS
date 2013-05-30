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

package connections;

import game.GameEngine;
import static java.lang.Thread.sleep;
import server.MFServer;

/**
 *
 * @author PabloMartinez
 */
    public class Streaming extends Thread{
        private long STREAMING_PING = 100; // Intervalo de tiempo entre cada envio
        private int checkConnected=0;
        private GameEngine game;
        
        public Streaming(GameEngine game){
            this.game = game;
        }
        
        @Override
        public void run() {
            while(game.state != GameEngine.GameState.FINISHED){
                // Si ningun player esta online el thread muere
                checkConnected =(checkConnected+1)%100;
                //Cada 100 iteraciones comprobamos que haya players online
                if (checkConnected == 99){
                    int ammount=0;
                    for (Connection p: game.players){
                        if (!(p.state == Connection.ConnectionState.IN_GAME)) ammount+=1;
                    }
                    if (ammount==game.players.length){
                        if (MFServer.DEBUG_GAMES){
                            System.err.println("==> GAME "+game.game_id+": Not players connected... Closing game");
                        }
                        game.state = GameEngine.GameState.FINISHED;
                    }
                }
                // Parseamos el escenario...
                String s = "0|"+game.scenario.parseScenario();
                
                // ...y lo enviamos a todas las conexiones
                for (Connection p: game.players) {
                    if(p.state == Connection.ConnectionState.IN_GAME) p.pushMapToClient(s);
                }

                // El threa d se va a dormir durante un tiempo de refresco = t;
                try {
                    sleep(STREAMING_PING);
                } catch (InterruptedException ex) {
                    if (MFServer.DEBUG_GAMES){
                        System.err.println("==> GAME "+game.game_id+": Streaming sleep interrupted.");
                    }
                }
            }
            
//            
//            TODO: Lo que enviamos cuando el juego acaba.
//            
            String s = game.end_game_string;
            for (Connection c : game.players){
                c.gameEnd(s);
            }
        }

    public void startStreaming() {
        this.start();
    }
        
    }