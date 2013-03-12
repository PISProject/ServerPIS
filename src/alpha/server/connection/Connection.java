/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.connection;

import alpha.server.main.Server;
import alpha.server.scenario.Scenario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.Socket;



/**
 *
 * @author zenbook
 */
public class Connection extends Thread{
    private enum ConnectionState{NOT_LOGGED,OUT_GAME,WAITING_QUEUE,IN_GAME};
    private ConnectionState state;
    private Server server;
    private Socket client;
    private Scenario scenario;
    public int uid;
    private DataInputStream in;
    private DataOutputStream out;
    
    //CONSTRUCTOR+RUN
    public Connection(Server server, Socket client, ThreadGroup threads){
        this.client = client;
        this.server = server;
        
    }
    @Override
    public void run() {
        while (true){
            try {
                String entrada = in.readUTF();
                if (state == ConnectionState.IN_GAME){
                    ProtocolGame.parse(this,entrada);
                }
                
            } catch (IOException ex) {
                System.err.println("IOException::Entrada de datos");
            }
        }
    }
    //---------------
    
    public void setScenario(Scenario game) {
        this.scenario = game;
    }
    
    public void pushScenarioToClient(String message){
        try {
            out.writeUTF(message);
        } catch (IOException ex) {
            System.err.println("IOException:: Could not push scenario!");
        }
    }
    
    public void startGame(){
        GameUpdate g = new GameUpdate();
        g.start();
    }

    
    //OUT_GAME Actions
    
    void joinQueue(){
        server.joinQueue(this);
    }
    
    void quitQueue(){
        server.quitQueue(this);
    }
    //IN_GAME Actions
    void moveTo(int x, int y) {
        scenario.moveTo(uid,x,y);
    }
    
    
    
    
    
    
    
    
    /**
     * Class GAMEUPDATE
     */
    private class GameUpdate extends Thread{
        @Override
        public void run() {
            while (client.isConnected()){
                pushScenarioToClient(scenario.parseScenario());

                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    System.err.println("GameUpdateThread Interrupted!");
                }
            }

        }
        
    }
}
