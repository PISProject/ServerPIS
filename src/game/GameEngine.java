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
    
    /* El reloj de la partida */
    private Timer clock;
    
    
    public GameEngine(int id, Connection[] game, Game t_game) {
        dead_players = new ArrayList<>();
        time_limit = t_game.estimatedTime;
        hordeCount = 0;
        monsters = new ConcurrentHashMap<>();
        this.game = t_game;
        this.game_id = id;
        scenario = new Scenario(this, game);
        
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
        System.out.println("==> [GAME ENGINE] Client "+uid+" respawns.");
        /* Notificamos al cliente que vuelve a vivir */
        
        // TODO
        
        /* Lo volvemos a colocar en el escenario*/
        String name = scenario.actores.remove(uid).name;
        scenario.actores.put(uid, new Actor(uid, name));
    }

    public void startGameThread() {
        //==> Programamos las hordas
        clock = new Timer();
        for (int i = 0; i < game.n_hordes; i++) {
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
            if (MFServer.DEBUG_GAMES) System.out.println("==> [GAME] Summoning new monster of type ->"+s);
            Monster m = new Monster();
            monster_id++;
            Actor a = m.createMonster(monster_id, scenario, Monsters.getMonsterModel(monstername));
            monsters.put(monster_id,m);
            scenario.addMonster(a);
            m.start();
        }
    }
    void onDeath(Actor a){
        System.out.println(a.uid+" is dead.");
        if (!a.isHero()){
            Monster m = monsters.get(a.uid);
            if (m!=null) m.monsterDeath();
            return;
        }
        dead_players.add(a.uid);
        System.out.println("Scheduling a new RESPAWN task");
        
        
//        scenario.actores.remove(uid);
//        monsters.remove(uid);
        
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
        this.state = GameState.FINISHED;
        clock.cancel();
        destroyMonsters();
        MFServer.SERVER.endGame(game_id);
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
