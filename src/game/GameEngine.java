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
import java.util.Timer;
import java.util.TimerTask;
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
    private ArrayList<Monster> monsters;
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
        monsters = new ArrayList<>();
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
        /* Notificamos al cliente que vuelve a vivir */
        
        // ToDo
        
        /* Lo volvemos a colocar en el escenario*/
        scenario.addHeroe(uid);
    }

    public void startGameThread() {
        //==> Programamos las hordas
        clock = new Timer();
        System.out.println(game.n_hordes);
        for (int i = 0; i < game.n_hordes; i++) {
            System.err.println("Yo");
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
                System.out.println("==> GAME "+/*this.uid+*/": Starts streaming");
        }
        streaming.start(); // Aqui empieza a correr el Streaming
        //clock = new Timer();
        
    }
    
    public void summonHorde(){
        if (MFServer.DEBUG_GAMES) System.out.println("==> [GAME] Summonning "+hordeCount+" horde.");
        Horde horde = game.hordes.get(hordeCount);
        hordeCount++;
        for (String monstername: horde.list){
            if (MFServer.DEBUG_GAMES) System.out.println("==> [GAME] Summoning new monster of type ->"+s);
            Monster m = new Monster();
            Actor a = m.createMonster(monster_id++, scenario, Monsters.getMonsterModel(monstername));
            scenario.addMonster(a);
            m.start();
        }
    }
    
    void onPlayerDeath(int uid) {
        /* Notificamos al cliente que ha muerto*/
        
        // TODO
        
        /* Programamos su reaparicion */
        dead_players.add(uid);
        clock.schedule(new TimerTask() {
            
            @Override
            public void run() {
                respawn(dead_players.get(0));
            }
        }, null);
    }
    
    public void endGame(/* Aqui iran los parametros que indicaran como ha acabado la partida*/){
        if(MFServer.SERVER.DEBUG_GAMES) System.out.println("==> [GAME] Ending game");
        clock.cancel();
        destroyMonsters();
        MFServer.SERVER.endGame(game_id);
    }
    
    private void destroyMonsters() {
        for (Monster m: monsters){
            m.monsterDeath();
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
