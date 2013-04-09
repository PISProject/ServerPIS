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
    private GameEngine game;
    private Scenario scenario;
    private Protocol protocol;
    private DataInputStream in;
    private DataOutputStream out;

    public Connection(Socket socket) {
        state = ConnectionState.OUT_GAME;
        protocol = new Protocol(this);
        try{
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        }catch(IOException i){
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
            System.err.println("IOException::Couldnt write in client");
        }
    }
    public void write(String s) {
        try{
            out.writeUTF(s);
        } catch (IOException ex) {
            System.err.println("IOException::Couldnt write in client");
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    ////////////////////
    // OUT_GAME Methods
    ////////////////////
    void joinQueue() {
        MFServer.SERVER.joinQueue(this);
    }

    void quitQueue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void notifyGameStarting() {
        this.state = ConnectionState.LOADING;
        write(protocol.GAME_FOUND);
    }
    ////////////////////
    // LOADING Methods
    ////////////////////////////////////////////////////////////////////////////
    
    public void setGame(GameEngine aThis) {
        this.game = aThis;
    }
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public void pushMapToClient(String s) {
        write(s);
    }
    public void notifyGameReady(){
        game.connectionIsReady(this);
    }
    public void startGame() {
        this.state = ConnectionState.IN_GAME;
    }
}   
