/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.scenario;

import alpha.server.connection.Connection;
import alpha.server.connection.ProtocolGame;

/**
 *
 * @author kirtash
 */
public class GameThread extends Thread{



    public enum GameState{STARTING,RUNNING,FINISHING}
    public GameState gamestate;
    private Connection [] players;
    private Scenario scenario;
    public String map;
    
    public GameThread(Connection [] c) {
        this.scenario = new Scenario(0,1,2,3);
        this.players = c;
        startGame();
    }

    private void startGame() {
        ProtocolGame proto = new ProtocolGame(this);
        for (int i = 0; i < 4; i++) {
            players[i].uid= i;
            players[i].protocolGame=proto;
            
        }
    
    }

    public void moveTo(int uid, int x, int y) {
        scenario.moveTo(uid, x, y);
    }    
    
    public void attack(int uid) {
        scenario.attack(uid);
    }
    
    @Override
    public void run() {
        map = scenario.parseScenario();
        try {
            sleep(100);
        } catch (InterruptedException ex) {
            System.err.println("InterruptedException:: GameThread :: run()");
        }
    }
    
    
    
    
}
