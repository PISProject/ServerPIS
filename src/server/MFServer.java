/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.Connection;
import database.MySQLConnection;
import game.GameEngine;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kirtash
 */
public class MFServer {
    public static void main(String[] args) {
       SERVER = new MFServer();
    }
    
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
    public HashMap<Integer,Connection> clients;

    
    public GameQueue queue;
    public int connectionUid=0; //Provisional mientras no esta implementado el Login
   

    
    public MFServer(){
        games = new ArrayList<>();
        clients = new HashMap<>();
        queue = new GameQueue();
        
        //Intentamos crear el connectionListener
        try{
            listener = new ConnectionListener();
        }catch(IOException ex){
            //No ha podido ser
            System.err.println("Listener could not be created!");
            System.exit(1);
        }
        
        try {
            //Intentamos crear la base de datos
            login = new LoginManager(new MySQLConnection());
        } catch (SQLException ex) {
            System.err.println("Cannot establish connection with database.");
            
        } catch (ClassNotFoundException ex) {
        }
        threadGroup = new ThreadGroup("g");
        
        startServer();
        
    }
    
    public void addPlayer(Connection con){
        clients.put(con.uid,con);
    }
    
    public void joinQueue(Connection aThis) {
        System.err.println("Player "+aThis.uid+" joined queue");
        Connection [] game = queue.join(aThis);
        if (game != null){
            startGame(game);
            System.err.println("Starting New Game!");
        }
    }  
    
     private void startGame(Connection[] game) {
         games.add(new GameEngine(game));
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
        System.out.println(clients.size());
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
             while(true){
                 System.err.println("Waiting connection...");
                 Socket socket = ss.accept();
                 new Connection(socket, login);
                 System.err.println("New incoming connection detected.");
             }
         } catch (IOException ex) {
             System.err.println("IOException::while creating a new clientSocket");
         }

        }       

     }
}
