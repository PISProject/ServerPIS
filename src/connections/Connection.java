/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

import game.GameEngine;
import game.Scenario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import server.MFServer;

/**
 *
 * @author kirtash
 */
public class Connection extends Thread{



 
    public enum ConnectionState{NOT_LOGGED,OUT_GAME,QUEUE,LOADING,IN_GAME,DISCONNECTED};
    public ConnectionState state;
    public int uid;
    private Socket socket;
    private GameEngine game;
    private Scenario scenario;
    private Protocol protocol;
    private DataInputStream in;
    private DataOutputStream out;

    public Connection(Socket socket) {
        this.socket = socket;
        state = ConnectionState.OUT_GAME;
        protocol = new Protocol(this);
        try{
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException i){
            onDisconnect();
            System.err.println("IOException::Error creating IO Streams");
        }
        this.start();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        while (state != ConnectionState.DISCONNECTED){
            try {
                String entrada = in.readUTF();
                protocol.parse(entrada);
                
            } catch (IOException ex) {
                onDisconnect();
                System.err.println("IOException::Entrada de datos");
                state = ConnectionState.DISCONNECTED;
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    // WRITE Methods
    ////////////////////////////////////////////////////////////////////////////
    public void write(boolean b) {
        try {
            out.writeBoolean(b);
        } catch (IOException ex) {
            onDisconnect();
            System.err.println("IOException::Couldnt write in client");
        }
    }
    public void write(String s) {
        try{
            out.writeUTF(s);
        } catch (IOException ex) {
            onDisconnect();
            System.err.println("IOException::Couldnt write in client");
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
    ////////////////////////////////////////////////////////////////////////////
    // OUT_GAME Methods
    ////////////////////////////////////////////////////////////////////////////
    void joinQueue() {
        MFServer.SERVER.joinQueue(this);
    }

    void quitQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // QUEUE Methods
    ////////////////////////////////////////////////////////////////////////////
    
    // OUTGOING Messages
    public void notifyGameStarting() {
        this.state = ConnectionState.LOADING;
        //write(protocol.GAME_FOUND);
        write(""+this.uid); //Provisional
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // LOADING Methods
    ////////////////////////////////////////////////////////////////////////////
    
    // INCOMING Messages
    public void notifyGameAvalible(){
        game.connectionIsReady(this);
    }
    
    // OUTGOING Messages
    public void startGame() {
        this.state = ConnectionState.IN_GAME;
    }
    
    // Others
    public void setGame(GameEngine aThis) {
        this.game = aThis;
    }
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
    
    //////////
    // IN_GAME Methods
    ////////////////////////////////////////////////////////////////////////////
    public void pushMapToClient(String s) {
        write(s);
    }
    
    public void moveTo(float x, float y){
        scenario.moveTo(this.uid,x,y);
    }
    
    ///////
    // GENERAL Methods
    
    public boolean isConnected(){
        return socket.isConnected();
    }
    
    public void onDisconnect(){
        switch (state){
            case OUT_GAME:
                MFServer.SERVER.onDisconnectClient(this);
                break;
            case QUEUE:
                MFServer.SERVER.onDisconnectClient(this);
                break;
            case IN_GAME:
                game.disconnect(this);
        }
    }

}   
