/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.Connection;
import database.MySQLConnection;
import game.GameEngine;
import game.models.Game;
import game.models.GameTest;
import game.monsters.Monsters;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author kirtash
 */
public class MFServer {
    public static void main(String[] args) {
       SERVER = new MFServer();
    }
    
    public static final boolean DEBUG_MONSTERS = true;
    public static final boolean DEBUG_SERVER = true;
    public static final boolean DEBUG_CONNECTIONS = true;
    public static final boolean DEBUG_MYSQL = true;
    public static final boolean DEBUG_GAMES = true;
    public static final boolean DEBUG_XML = true;
    public static final boolean DEBUG_SCENARIO = true;
    
    public static MFServer SERVER;
    public static boolean ACCEPT_CONNECTIONS = false;

    //INET Att
    public int PORT = 5050;
    public ConnectionListener listener;
    public ThreadGroup threadGroup;
    //
    
    //Controlador de la base de datos
    public LoginManager login;
    
    //Listas//
    public ArrayList<GameEngine> games;
    public ConcurrentHashMap<Integer,Connection> clients;

    
    private GameQueue queue;
    public int connectionUid=0; //Provisional mientras no esta implementado el Login
   
    // Buffer de strings para la interficie grafica
    public ServerAPI serverAPI;
    
    public MFServer(){
        serverAPI = new ServerAPI();
        serverAPI.print("as");
        /*System.err.println("===================SERVER DEVELOPED BY");
        games = new ArrayList<>();
        clients = new ConcurrentHashMap<>();
        queue = new GameQueue();
        
        //Intentamos crear el connectionListener
        try{
            listener = new ConnectionListener();
        }catch(IOException ex){
            //No ha podido ser
            if(MFServer.DEBUG_SERVER){
                System.err.println("==> [SERVER] Listener could not be created! Leaving system [EXIT]");
            }
            
            System.exit(1);
        }
        
        try {
            //Intentamos crear la base de datos
            if(MFServer.DEBUG_SERVER){
                System.err.print("==> [SERVER] Trying to create database ...");
            }
            login = new LoginManager(new MySQLConnection());
        } catch (SQLException ex) {
            if(MFServer.DEBUG_SERVER){
                System.err.println("[X] :: SQL Exception :: Closing server");
                System.exit(1);
            }
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            if(MFServer.DEBUG_SERVER){
                System.err.println("[X] :: MySQL Driver not found! :: Closing server");
                System.exit(1);
            }
        }
        threadGroup = new ThreadGroup("g");
        Monsters m = new Monsters();
        try{
            m.readFromXML();
        } catch( IOException | ParserConfigurationException | SAXException e){
            if (MFServer.DEBUG_XML)
                System.err.println("==>[XML] Cannot load monsters! :: Closing server!");
                System.exit(1);
        }
        startServer();*/
        
    }
    
    public void addPlayer(Connection con){
        clients.put(con.uid,con);
    }
    
    public void joinQueue(Connection aThis) {
        Connection [] game = queue.join(aThis);
        if (game != null){
            if (MFServer.DEBUG_GAMES){
                System.err.println("==> [SERVER]Starting new game!");
            }
            startGame(game);
        }
    }  
    
     private void startGame(Connection[] game) {
         games.add(new GameEngine(game, (Game) (new GameTest()))); //Provisional, en adelante los usuarios podran escoger la partida
     }   

    private void startServer() {
        ACCEPT_CONNECTIONS = true;

        listener.start();
    }
    
    public boolean quitQueue(Connection con){
        return queue.quit(con);
    }
    public void onDisconnectClient(Connection con) {
        if (queue.isConnectionInQueue(con)){
            queue.quit(con);
        }
        clients.remove(con.uid);
        if(MFServer.DEBUG_SERVER){
            System.err.println("==> [SERVER] Client "+con.uid+" left.");
        }
    }

    boolean isClientOnline(int id) {
        return (clients.get(id) != null);
    }
    
    
    
    
    
    
    
    
    
    
    
   
    /**
     * ConnectionListener: Clase interna que actua como Thread para escuchar las conexiones entrantes.
     */
    public class ConnectionListener extends Thread{
         public ServerSocket ss;


         public ConnectionListener() throws IOException{ 
            ss = new ServerSocket(PORT);
         }

     @Override
     public void run() {
         try {
            if (MFServer.DEBUG_SERVER){
                     System.err.println("==> [SERVER] Waiting connection...");
            }
             while(true){

                 
                 Socket socket = ss.accept();
                 new Connection(socket, login);
                 
                if (MFServer.DEBUG_SERVER){
                     System.err.println("==> [SERVER] New incoming connection detected.");
                 }
             }
         } catch (IOException ex) {
            if (MFServer.DEBUG_SERVER){
                System.err.println("==> [SERVER] Error while creating a new client socket. Listener it's now down.");
            }
         }

        }       

     }
}
