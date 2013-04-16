/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import connections.Connection;
import java.util.Timer;

/**
 *
 * @author kirtash
 */
public class GameEngine{
    public enum GameState {LOADING, RUNNING, FINISHED};
    private Scenario scenario;
    public GameState state;
    Player [] players;
    Scenario s;
    Streaming streaming;
    int ready = 0;
    Timer clock;
    public GameEngine(Connection[] game) {
        
        scenario = new Scenario(game);
        
        //Inicializamos la lista de players
        players = new Player[game.length];
        
        // Creamos un player por cada conexion, y le asignamos una referencia de
        // la partida a cada una de ellas.
        for (int i = 0; i < game.length; i++) {
            game[i].setGame(this);
            game[i].setScenario(scenario);
            players[i] = new Player(game[i]);
        }
        // Notificamos a todas las conexiones que se ha creado un juego y que tienen
        // que empezar a cargar
        for(Connection c: game){
            c.notifyGameStarting();
        }
        
        streaming = new Streaming();
        // GameThread acaba, hasta que todas las conexiones esten listas.
    }

    
    // Cuando una conexion esta lista para inciar la partida, notifica al servidor.
    public synchronized void connectionIsReady(Connection aThis) {
        for (Player p: players) {
            if (p.uid == aThis.uid){
                p.ready = true;
            }
        }
        ready++;
        if (ready == players.length){
            for (Player p:players){
                p.con.startGame();
            }
            startGameThread();
        }
    }

    public void startGameThread() {
        state = GameState.RUNNING;
        streaming.start(); // Aqui empieza a correr el GameThread
        clock = new Timer();
    }   

    public void disconnect(Connection aThis) {
        for (Player p:players){
            if (aThis.equals(p.con)){
                p.connected = false;
            }
        }
        System.err.println("Player "+aThis.uid+" disconnected!");
    }
    
    
    public class Player{
        private Connection con;
        private int uid;
        private boolean ready;
        private boolean connected;
        
        public Player(Connection c){
            this.connected = true;
            this.con = c;
            this.uid = con.uid;
        }
    }

    // La clase Streaming es la que enviara constantemente el mapa a todos los
    // jugadores de la partida, lo hara incondicionalmente cada 100ms.
    public class Streaming extends Thread{
        private long STREAMING_PING = 100; // Intervalo de tiempo entre cada envio

        @Override
        public void run() {
            while(state != GameState.FINISHED){
                // Si ningun player esta online el thread muere
                if(players.length == 0){
                    System.err.println("No players connected");
                    System.out.println("Closing game");
                    state = GameState.FINISHED;
                }
                
                // Parseamos el escenario...
                String s = scenario.parseScenario();
                
                // ...y lo enviamos a todas las conexiones
                for (Player p: players) {
                    if(p.connected) p.con.pushMapToClient(s);
                }

                // El thread se va a dormir durante un tiempo de refresco = t;
                try {
                    sleep(STREAMING_PING);
                } catch (InterruptedException ex) {
                    System.err.println("Interrupted!");
                }
            }
        }
        
    }
}
