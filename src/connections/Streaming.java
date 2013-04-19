/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import game.GameEngine;
import static java.lang.Thread.sleep;

/**
 *
 * @author kirtash
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
                        System.err.println("No players connected");
                        System.out.println("Closing game");
                        game.state = GameEngine.GameState.FINISHED;
                    }
                }
                // Parseamos el escenario...
                String s = game.scenario.parseScenario();
                
                // ...y lo enviamos a todas las conexiones
                for (Connection p: game.players) {
                    if(p.state == Connection.ConnectionState.IN_GAME) p.pushMapToClient(s);
                }

                // El threa d se va a dormir durante un tiempo de refresco = t;
                try {
                    sleep(STREAMING_PING);
                } catch (InterruptedException ex) {
                    System.err.println("Interrupted!");
                }
            }
        }

    public void startStreaming() {
        this.start();
    }
        
    }