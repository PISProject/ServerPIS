/*******************************************************************************
 * Machango Fight, the Massive Multiplayer Online.
 * Server Application
 * 
 * Curso 2012-2013
 * 
 * Este software ha sido desarrollado integramente para la asignatura 'Projecte
 * Integrat de Software' en la Universidad de Barcelona por los estudiantes
 * Pablo Martínez Martínez, Albert Folch, Xavi Moreno y Aaron Negrín.
 * 
 ******************************************************************************/

package connections;

import game.Attack;
import game.GameEngine;
import game.Scenario;
import game.models.Games;
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
    
    
    private int game_type_joining;
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
                 System.out.println("==> [CONNECTION] Cannot create streams to "+name);
            }
        }
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void run() {
        while (state != ConnectionState.DISCONNECTED){
            try {
                String entrada = in.readUTF();
;                protocol.parse(entrada);
                
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
                System.out.println("==> [CONNECTION] Couldn't reach client "+uid);
            }
        }
    }
    public void write(String s) {
        try{
            out.writeUTF(s);
        } catch (IOException ex) {
            disconnect();
           if(MFServer.DEBUG_CONNECTIONS){
                System.out.println("==> [CONNECTION]  Couldn't reach "+name);}
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
                    System.out.println("==> [CONNECTION] '"+name+"' logged succesfully");
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
    void joinQueue(int game_type) {
        this.game_type_joining = game_type;
        MFServer.SERVER.joinQueue(game_type_joining, this);
    }

    void quitQueue() {
        if(MFServer.DEBUG_CONNECTIONS){
            System.out.print("==> [CONNECTION] '"+name+"' is trying to left queue.");
        }
        if (MFServer.SERVER.quitQueue(game_type_joining,this)){
            this.state = ConnectionState.OUT_GAME;
            if(MFServer.DEBUG_CONNECTIONS){
                    System.out.println("[DONE]");
            }
            write("1");
            return;
        }
        if(MFServer.DEBUG_CONNECTIONS){
                    System.out.println("[X]");
        }
        write("0");
    }
    
    //OUTGOING Messages
    void getScenarios() {
        String s = Games.parseGames();
        write(s);
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    // QUEUE Methods
    ////////////////////////////////////////////////////////////////////////////
    
    // OUTGOING Messages
    public void notifyGameFound(String infoPlayers) {
        if(MFServer.DEBUG_CONNECTIONS){
            System.out.println("==> [CONNECTION] Notifying '"+name+"' game found.");
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
    
    public void attack(){
        scenario.attack(new Attack(scenario.actores.get(uid), 1));
    }
    ///////
    // GENERAL Methods
    
    public boolean isConnected(){
        return socket.isConnected();
    }
    
    public void disconnect(){
        
        if(MFServer.DEBUG_CONNECTIONS){
            System.out.print("==> [CONNECTION] '"+name+"' disconnected, trying to close socket...");
        }
        try {
            socket.close();
            if(MFServer.DEBUG_CONNECTIONS){
                System.out.println("[DONE]");
            }
        } catch (IOException ex) {
            if(MFServer.DEBUG_CONNECTIONS){
                System.out.println("[X]");
            }
        }
        switch (state){
            case OUT_GAME:
                MFServer.SERVER.onDisconnectClient(this);
                break;
            case LOADING:
                game.connectionIsReady(this);
                MFServer.SERVER.onDisconnectClient(this);
            case QUEUE:
                MFServer.SERVER.quitQueue(game_type_joining, this);
                MFServer.SERVER.onDisconnectClient(this);
                break;

            case IN_GAME:
                //game.disconnect(this);
                MFServer.SERVER.onDisconnectClient(this);
        }
        state = ConnectionState.DISCONNECTED;
    }
    
    public void gameEnd(String s){
        write(s);
        state = ConnectionState.OUT_GAME;
    }

}   
