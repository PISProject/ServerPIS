/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.connection;

import alpha.server.scenario.Actor;
import alpha.server.scenario.Scenario;
import java.net.Socket;



/**
 *
 * @author zenbook
 */
public class Connection extends Thread{
    
    private Socket client;
    private Scenario scenario;
    private int uid;
    
    
    
    public Connection(Socket client, ThreadGroup threads){
        this.client = client;
    }

    @Override
    public void run() {
    }

    public void setScenario(Scenario game) {
        this.scenario = game;
    }

}
