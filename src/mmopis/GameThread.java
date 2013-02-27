/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class with all items, and status ingame.
 * @author kirtash
 */
enum GameStat{WAITING_CONNECTIONS,INIT,RUNNING,FINISHED};

public class GameThread extends Thread{
    private Connection[] players;
    private GameStat gameStat;
    private int ready;
    
    public GameThread(Connection[] players){
        this.players = players;
        this.gameStat = GameStat.WAITING_CONNECTIONS;
        this.ready=0;
        this.start();
    }

    void setReady(int palyerid) {
       this.ready+=1;
    }

    @Override
    public void run() {
        while(gameStat != GameStat.FINISHED){
            if (gameStat == GameStat.WAITING_CONNECTIONS){
                if  (this.ready==players.length){
                    this.gameStat = GameStat.RUNNING;
                    for ( Connection i : players){
                        i.notifyGameStarting();
                    }
                
                }
                else{
                    try {
                        this.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            else if (gameStat == GameStat.RUNNING){
                for(Connection i : players){
                    i.send(game.getMap());
                }
            }
        }
    }
    
    
    
    
    
}
