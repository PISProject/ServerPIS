/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import connections.Connection;
import connections.Streaming;
import game.models.Game;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import game.monsters.MonsterTest;
import server.MFServer;

/**
 *
 * @author kirtash
 */
public class GameEngine extends Thread{
    public enum GameState {LOADING, RUNNING, FINISHED};
    public Scenario scenario;
    public GameState state;
    public Game game;
    public Connection [] players;
    public Scenario s;
    Streaming streaming;
    int ready = 0;
    Timer clock;
    public GameEngine(Connection[] game, Game t_game) {
        
        scenario = new Scenario(game);
        
        //Inicializamos la lista de players
        players = new Connection[game.length];
        
        // Creamos un player por cada conexion, y le asignamos una referencia de
        // la partida a cada una de ellas.
        String info = new String();
        players = game;
        for (Connection c: game) {
            c.setGame(this);
            c.setScenario(scenario);
            info+=c.uid+","+c.name+"*";
        }
        // Notificamos a todas las conexiones que se ha creado un juego y que tienen
        // que empezar a cargar
        for(Connection c: game){
            c.notifyGameFound(info);
        }
        streaming = new Streaming(this);
        // GameThread acaba, hasta que todas las conexiones esten listas.
        
        //this.start();
    }

    
    // Cuando una conexion esta lista para inciar la partida, notifica al servidor.
    public synchronized void connectionIsReady(Connection aThis) {
        ready++;
        if (MFServer.DEBUG_CONNECTIONS || MFServer.DEBUG_GAMES){
                System.out.println("==> Connection "+aThis.uid+" is ready to start.");
        }
        if (ready == players.length){
            for (Connection p:players){
                p.startGame();
            }
            startGameThread();
        }
    }

    public void startGameThread() {
        state = GameState.RUNNING;
        if (MFServer.DEBUG_GAMES){
                System.out.println("==> GAME "+/*this.uid+*/": Starts streaming");
        }
        streaming.start(); // Aqui empieza a correr el Streaming
        
        scenario.addMonster(new MonsterTest().createMonster(100,scenario));
        //clock = new Timer();
        
    }   
    
    
    /*
    public void disconnect(Connection aThis) {
        for (Player p:players){
            if (aThis.equals(p.con)){
                p.connected = false;
            }
        }
        System.err.println("Player "+aThis.uid+" disconnected!");
    }*/

    
    // La clase Streaming es la que enviara constantemente el mapa a todos los
    // jugadores de la partida, lo hara incondicionalmente cada 100ms.

    @Override
    public void run() { // Este run se encargara de gstionar los cambios en el juego
        if (scenario.monsterCount == 0){
            scenario.addMonster(new MonsterTest().createMonster(100,scenario));
            
            /*try {
                MonsterTest m = (MonsterTest)Class.forName(s).newInstance();
                
            } catch (ClassNotFoundException ex) {
            } catch (InstantiationException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }

}
