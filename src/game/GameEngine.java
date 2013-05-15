/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import connections.Connection;
import connections.Streaming;
import game.models.Game;
import game.models.Horde;
import game.monsters.Monster;
import game.monsters.Monsters;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import server.MFServer;

public class GameEngine{


    public enum GameState {LOADING, RUNNING, FINISHED};
    public int game_id;
    public int monster_id = 1000;
    public Scenario scenario;
    public GameState state;
    public Game game;
    public Connection [] players;
    private ArrayList<Monster> monsters;
    private int hordeCount;
    public Scenario s;
    Streaming streaming;
    int ready = 0;
    private Timer clock;
    public GameEngine(int id, Connection[] game, Game t_game) {
        System.out.println(t_game);
        hordeCount = 0;
        monsters = new ArrayList<>();
        this.game = t_game;
        this.game_id = id;
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
            
            info+=c.uid+","+c.name+/*","+c.player_model+*/"*";
        
        }
        // Notificamos a todas las conexiones que se ha creado un juego y que tienen
        // que empezar a cargar
        for(Connection c: game){
            c.notifyGameFound(info);
        }
        
        
        streaming = new Streaming(this);
        // GameThread acaba, hasta que todas las conexiones esten listas.
        
       
    }

    
    // Cuando una conexion est        clock.a lista para inciar la partida, notifica al servidor.
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
    
    public void respawn(int uid){
        scenario.addHeroe(uid);
    }

    public void startGameThread() {
        // Creamos los timers para las hordas.
        clock = new Timer();
        for (int i = 0; i < game.n_hordes; i++) {
            clock.schedule(new TimerTask() {

                @Override
                public void run() {
                    summonHorde();
                }
            }, game.hordes.get(i).time);
        }
        state = GameState.RUNNING;
        if (MFServer.DEBUG_GAMES){
                System.out.println("==> GAME "+/*this.uid+*/": Starts streaming");
        }
        streaming.start(); // Aqui empieza a correr el Streaming
        //clock = new Timer();
        
    }
    
    public void playerDead(int id){
        new RespawnTask(clock, id);
    }
    
    public void summonHorde(){
        if (MFServer.SERVER.DEBUG_GAMES){
            System.out.println("==> [GAME] Summonning "+hordeCount+" horde.");
        }
        Horde horde = game.hordes.get(hordeCount);
        hordeCount++;
        while(horde.hasNext()){
            Monster m = new Monster();
            Actor a = m.createMonster(monster_id++, scenario, Monsters.getMonsterModel(horde.getNextMonster()));
            scenario.addMonster(a);
            m.start();
        }
    }
    
    public void endGame(){
        clock.cancel();
        destroyMonsters();
        MFServer.SERVER.endGame(game_id);
    }
    
    private void destroyMonsters() {
        for (Monster m: monsters){
            m.kill();
        }
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


    public class RespawnTask extends TimerTask{
        private int uid;
        private long TIME_TO_RESPAWN;
        public RespawnTask(Timer clock, int id){
            this.uid = id;
            clock.schedule(this, TIME_TO_RESPAWN);
        }

        @Override
        public void run() {
            respawn(uid);
        }
    }
}
