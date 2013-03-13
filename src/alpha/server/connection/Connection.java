/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.connection;

import alpha.server.main.*;
import alpha.server.scenario.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author zenbook
 */
public class Connection extends Thread{



    public enum ConnectionState{NOT_LOGGED,OUT_GAME,WAITING_QUEUE,IN_GAME,DISCONNECTED};
    public ConnectionState state;
    public ProtocolGame protocolGame;
    public Protocol protocolServer;
    private Socket client;
    public int uid;
    private DataInputStream in;
    private DataOutputStream out;
    
    //CONSTRUCTOR+RUN
    public Connection(Protocol protocolServer, Socket client, ThreadGroup threads){
        this.client = client;
        this.protocolServer = protocolServer;
        try{
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
        }catch(IOException i){
            System.err.println("IOException::Error creating IO Streams");
        }
    }
    @Override
    public void run() {
        while (state != ConnectionState.DISCONNECTED){
            try {
                String entrada = in.readUTF();
                if (state == ConnectionState.OUT_GAME){
                    protocolServer.parse(this,entrada);
                }
                if (state == ConnectionState.IN_GAME){
                    protocolGame.parse(uid, entrada);
                }
            } catch (IOException ex) {
                System.err.println("IOException::Entrada de datos");
            }
        }
    }
    //---------------
    public void pushToClient(boolean b) {
        try {
            out.writeBoolean(b);
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    

   
    
    
    
    
    /**
     * Class GAMEUPDATE
     */
    
    private class GameUpdate extends Thread{
        @Override
        public void run() {
            while (state == ConnectionState.IN_GAME){
                pushScenarioToClient(protocolGame.getMap());

                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    System.err.println("GameUpdateThread Interrupted!");
                }
            }

        }
        
    }
}
