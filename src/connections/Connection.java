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
import server.LoginManager;
import server.MFServer;

/**
 *
 * @author kirtash
 */
public class Connection extends Thread{



 
    public enum ConnectionState{NOT_LOGGED,OUT_GAME,QUEUE,LOADING, READY, IN_GAME,DISCONNECTED};
    public ConnectionState state;
    public int uid;
    public String name;
    
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
            if(MFServer.DEBUG_CONNECTIONS){
                 System.err.println("==> Cannot create streams in connection "+this.uid);
            }
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
            if(MFServer.DEBUG_CONNECTIONS){
                System.err.println("==> Couldn't reach client "+uid);
            }
        }
    }
    public void write(String s) {
        try{
            out.writeUTF(s);
        } catch (IOException ex) {
            disconnect();
           if(MFServer.DEBUG_CONNECTIONS){
                System.err.println("==> Couldn't reach client "+uid);}
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    ////////////////////////////////////////////////////////////////////////////
    // NOT_LOGGED Methods
    ////////////////////////////////////////////////////////////////////////////

    /*
     * En adelante añadiremos la opcion de que el jugador pudiese estar en una
     * partida
     */
    void login(String user, String password){
        this.uid = login.login(user, password);
        this.name = login.getPlayerName(uid);
        /*
         * El cliente tiene que leer para Integer.parseInt(in.readUTF())>0 y <0.
         */
        switch (uid){
            case -2:
                write("-2");
                disconnect();
                break;
            case -1:
                write("-1");
                break;
            default:
                MFServer.SERVER.addPlayer(this);
                this.state = ConnectionState.OUT_GAME;
                write(""+uid); //Enviamos la uid al client
                
                //Debugging
                if(MFServer.DEBUG_CONNECTIONS){
                    System.err.println("==> Client "+uid+" logged succesfully");
                }
                break;               
                
        }
    }
    
    public void register(String username, String password, String email){
        
        if (!username.equals("") && !password.equals("")){
            write(Math.abs(login.register(username,password,email))+"");
            // 0-> Todo ha ido bien
            // -1-> Player en uso
            // -2-> Fallo de conexion con la base de datos
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // OUT_GAME Methods
    ////////////////////////////////////////////////////////////////////////////
    void joinQueue() {
        MFServer.SERVER.joinQueue(this);
    }

    void quitQueue() {
        if(MFServer.DEBUG_CONNECTIONS){
            System.err.print("==> Client "+uid+" is trying to left queue.");
        }
        if (MFServer.SERVER.quitQueue(this)){
            this.state = ConnectionState.OUT_GAME;
            if(MFServer.DEBUG_CONNECTIONS){
                    System.err.println("[DONE]");
            }
            write("1");
            return;
        }
        if(MFServer.DEBUG_CONNECTIONS){
                    System.err.println("[X]");
        }
        write("0");
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // QUEUE Methods
    ////////////////////////////////////////////////////////////////////////////
    
    // OUTGOING Messages
    public void notifyGameFound(String infoPlayers) {
        if(MFServer.DEBUG_CONNECTIONS){
            System.err.println("==> Notifying connection "+uid+" game found.");
        }
        this.state = ConnectionState.LOADING;
        //write(protocol.GAME_FOUND);
        write(infoPlayers); //Provisional
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // LOADING Methods
    ////////////////////////////////////////////////////////////////////////////
    
    // INCOMING Messages
    public void connectionIsReady(){
        game.connectionIsReady(this);
    }
    
    // OUTGOING Messages
    public void startGame() {
        write("1"); // ?? para que el cliente sepa que esta empezando la partida
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
        
        if(MFServer.DEBUG_CONNECTIONS){
            System.err.print("==> Client '"+uid+"' disconnected, trying to close socket...");
        }
        try {
            socket.close();
            if(MFServer.DEBUG_CONNECTIONS){
                System.err.println("[DONE]");
            }
        } catch (IOException ex) {
            if(MFServer.DEBUG_CONNECTIONS){
                System.err.println("[X]");
            }
        }
        switch (state){
            case OUT_GAME:
                MFServer.SERVER.onDisconnectClient(this);
                break;
            case QUEUE:
                MFServer.SERVER.onDisconnectClient(this);
                break;
            case LOADING:
                game.connectionIsReady(this);
                MFServer.SERVER.onDisconnectClient(this);
            case IN_GAME:
                //game.disconnect(this);
                MFServer.SERVER.onDisconnectClient(this);
        }
        state = ConnectionState.DISCONNECTED;
    }

}   
