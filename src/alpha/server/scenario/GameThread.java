/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.scenario;

import alpha.server.connection.Connection;

/**
 *
 * @author kirtash
 */
public class GameThread extends Thread{
    private enum GameState{STARTING,RUNNING,FINISHING}
    private Connection [] players;
    private Scenario scenario;
    private String map;
    
    public GameThread(Connection[] c) {
        this.scenario = new Scenario(0,1,2,3);
        this.players = c;
        startGame();
    }

    private void startGame() {
        for (int i = 0; i < 4; i++) {
            players[i].uid= i;
            
        }
    
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
