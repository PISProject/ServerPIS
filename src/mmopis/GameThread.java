/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class with all items, and status ingame.
 * @author kirtash
 */
enum GameStat{WAITING_CONNECTIONS,INIT,RUNNING,FINISHED};

public class GameThread extends Thread{
    private Connection[] connections;
    private Scenario scenario;
    private GameStat gameStat;
    public static int ready;
    
    
    /// THREAD para update
    private class GameUpdate extends Thread{
        private Connection c;
        private Scenario s;
        
        public GameUpdate(Connection c, Scenario s){
            this.c = c;
            this.s = s;
        }

        @Override
        public void run() {
            try {
                c.pushToClient(s.getMap());
            } catch (IOException ex) {
                //TODO
                System.err.println("IO Exception::GameUpdate");
            }
        }
        
    }
    public GameThread(Connection[] players){
        this.connections = players;
        Summoner [] summoners = new Summoner[players.length];
        int i = 0;
        for (Connection c: players){
            summoners[i] = c.summoner;
            i++;
        }
        this.scenario = new Scenario(summoners);
        this.gameStat = GameStat.WAITING_CONNECTIONS;
        this.ready=0;
        this.start();
    }

    public void setReady(int palyerid) {
       this.ready+=1;
    }

    @Override
    public void run() {
        for (Connection i:connections){
            i.startGame(this,scenario.getInit());
        }
        while(gameStat != GameStat.FINISHED){
            if (gameStat == GameStat.WAITING_CONNECTIONS){
                if  (this.ready==connections.length){
                    this.gameStat = GameStat.RUNNING;
                    for ( Connection i : connections){
                        i.notifyGameStarting();
                    }
                }
                else{
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException ex) {
                        //TODO
                    }
                }
            }
            else if (gameStat == GameStat.RUNNING){
                GameUpdate [] g = new GameUpdate[2]; // el 2 es provisional
                int tmp = 0;
                for(Connection i : connections){
                       g[tmp] = new GameUpdate(i, scenario);
                       tmp++;
                    }
                while(gameStat == GameStat.RUNNING){
                    for (GameUpdate i: g){
                        i.start();
                    }

                }
            }
        }
    }
}
    
    
    
    
    
