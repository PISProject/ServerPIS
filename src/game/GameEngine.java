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
package game;

import connections.Connection;
import connections.Streaming;
import game.models.Game;
import game.models.Horde;
import game.monsters.Monster;
import game.monsters.Monsters;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import server.MFServer;

/**
 * @author PabloMartinez
 */
public class GameEngine{
    private final long TIME_TO_RESPAWN= 30000;
    public enum GameState {LOADING, RUNNING, FINISHED};
    public int game_id;
    public long time_limit;
    public int monster_id = 1000;
    public Scenario scenario;
    public GameState state;
    public Game game;
    public Connection [] players;
    private ConcurrentHashMap<Integer, Monster> monsters;
    private ArrayList<Integer> dead_players;
    private int hordeCount;
    public Scenario s;
    Streaming streaming;
    int ready = 0;
    private int total_monsters;
    private boolean victory;
    public String end_game_string;
    
    /* El reloj de la partida */
    private Timer clock;
    
    
    public GameEngine(int id, Connection[] game, Game t_game) {
        dead_players = new ArrayList<>();
        time_limit = t_game.estimatedTime;
        hordeCount = 0;
        total_monsters = 0;
        monsters = new ConcurrentHashMap<>();
        this.game = t_game;
        this.game_id = id;
        scenario = new Scenario(this, game);
        victory = false;
        
        //Inicializamos la lista de players
        players = new Connection[game.length];
        
        // Creamos un player por cada conexion, y le asignamos una referencia de
        // la partida a cada una de ellas.
        String info = new String();
        players = game;
        for (Connection c: game) {
            c.setGame(this);
            c.setScenario(scenario);
            
            info+=c.uid+","+c.name+/*",1"+*/"*"; //TODO: Este 1 representa el Modelo, ahora asignamos el mismo modelo a todo el mundo
        
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
//        
//        THIS IS A HARDCODED FUNCTION!!!
//        
        
        System.out.println("==> [GAME ENGINE] Client "+uid+" respawns.");
//        Lo unico que hacemos es volver a darle vida
        (scenario.actores.get(uid)).health = 100;
    }

    public void startGameThread() {
        //==> Programamos las hordas
        clock = new Timer();
        for (int i = 0; i < game.n_hordes; i++) {
            total_monsters += game.hordes.get(i).list.size();
            clock.schedule(new TimerTask() {

                @Override
                public void run() {
                    summonHorde();
                }
            }, game.hordes.get(i).time);
        }

        //==> Programamos el final de partida
        clock.schedule(new TimerTask() {

            @Override
            public void run() {
                endGame();
            }
        }, time_limit);
        
        state = GameState.RUNNING;
        if (MFServer.DEBUG_GAMES){
                System.out.println("==> GAME "+this.game_id+": Starts streaming");
        }
        streaming.startStreaming();// Aqui empieza a correr el Streaming
        //clock = new Timer();
        
    }
    
    public void summonHorde(){
        if (MFServer.DEBUG_GAMES) System.out.println("==> [GAME] Summonning "+hordeCount+" horde.");
        Horde horde = game.hordes.get(hordeCount);
        hordeCount++;
        for (String monstername: horde.list){
            if (MFServer.DEBUG_GAMES) System.out.println("==> [GAME] Summoning new monster of type ->"+monstername);
            Monster m = new Monster();
            monster_id++;
            Actor a = m.createMonster(monster_id, scenario, Monsters.getMonsterModel(monstername));
            monsters.put(monster_id,m);
            scenario.addMonster(a);
            m.start();
        }
    }
    void onDeath(Actor a){
        a.deaths++;
        System.out.println(a.uid+" is dead.");
        if (!a.isHero()){
            total_monsters -=1;
            Monster m = monsters.get(a.uid);
            if (m!=null) m.monsterDeath();
            
//            Si todos los monstruos han muerto acaba
            if (total_monsters == 0){
                clock.cancel();
                clock = new Timer();
                victory = true;
                
                // Programamos el final de partida para dentro de 5 segundos
                clock.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        endGame();
                    }
                }, 5000);
            }
            return;
        }
        dead_players.add(a.uid);
        
//        Si todos los players han muerto acaba
        if (game.numplayers == dead_players.size()){
            clock.cancel();
            clock = new Timer();
            victory = false;
            
            clock.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        endGame();
                    }
                }, 5000);
            return;
        }
        
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            
            @Override
            public void run() {
                respawn(dead_players.get(0));
                dead_players.remove(0);
            }
        }, 20000);
    }
    
//    void onPlayerDeath(int uid) {
//        /* Notificamos al cliente que ha muerto*/
//        
//        // TODO
//        
//        /* Programamos su reaparicion */
//       
//        scenario.actores.remove(uid);
//    }
//    
    public void endGame(/* Aqui iran los parametros que indicaran como ha acabado la partida*/){
        if(MFServer.DEBUG_GAMES) System.out.println("==> [GAME] Ending game");
        /* TODO Enviar informacion al cliente */
        clock.cancel();
        if (!victory) destroyMonsters();
        MFServer.SERVER.endGame(game_id);
        
        end_game_string = "1|"+((victory) ? "1/": "0/")+ scenario.getScores();
                  
        this.state = GameState.FINISHED;
    }
    
    private void destroyMonsters() {
        for (Map.Entry m: monsters.entrySet()){
            ((Monster)m.getValue()).monsterDeath();
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



}
