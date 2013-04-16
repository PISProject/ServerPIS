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
import java.util.logging.Level;
import java.util.logging.Logger;
import server.LoginManager;
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
    private LoginManager login;

    public Connection(Socket socket, LoginManager login) {
        this.login = login;
        this.socket = socket;
        this.state = ConnectionState.NOT_LOGGED;
        this.protocol = new Protocol(this);
        try{
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.start();
        }catch(IOException i){
            disconnect();
            System.err.println("IOException::Error creating IO Streams");
        }
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        while (state != ConnectionState.DISCONNECTED){
            try {
                String entrada = in.readUTF();
                protocol.parse(entrada);
                
            } catch (IOException ex) {
                disconnect();
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
            disconnect();
            System.err.println("IOException::Couldnt write in client");
        }
    }
    public void write(String s) {
        try{
            out.writeUTF(s);
        } catch (IOException ex) {
            disconnect();
            System.err.println("IOException::Couldnt write in client");
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////
    // NOT_LOGGED Methods
    ////////////////////////////////////////////////////////////////////////////
    void login(String user, String password){
        this.uid = login.login(user, password);
        switch (uid){
            case -2:
                try {
                    out.writeUTF("2");
                } catch (IOException ex) {
                }
                disconnect();
                break;
            case -1:
                try {
                    out.writeUTF("2");
                } catch (IOException ex) {
                }
                disconnect();
                break;
            default:
                MFServer.SERVER.addPlayer(this);
               this.state = ConnectionState.OUT_GAME;
               System.err.println("Logged succesfully");
               try {
                   out.writeUTF("0"); // <- provisional, habra que poner la uid aqui
               } catch (IOException ex) {
                   disconnect();
               }
               break;               
                
        }
        System.err.println("Couldnt log");

    }
    
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
    
    public void moveTo(int angle){
        scenario.moveTo(this.uid,angle);
    }
    
    ///////
    // GENERAL Methods
    
    public boolean isConnected(){
        return socket.isConnected();
    }
    
    public void disconnect(){
        
        System.out.println("Client "+uid+" disconnected");
        state = ConnectionState.DISCONNECTED;
        try {
            socket.close();
        } catch (IOException ex) {
            System.err.println("Cannot close the connection socket.");
        }
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
