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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kirtash
 */
public class MFServer {
    public static void main(String[] args) {
       SERVER = new MFServer();
    }
    
    public static MFServer SERVER;


    //INET Att
    public int PORT = 5050;
    public ConnectionListener listener;
    public ThreadGroup threadGroup;
    //
    
    //Controlador de la base de datos
    public MySQLConnection db;
    
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
            db = new MySQLConnection();
        } catch (SQLException ex) {
            System.err.println("Cannot establish connection with database.");
            
        } catch (ClassNotFoundException ex) {
        }
        threadGroup = new ThreadGroup("g");

        startServer();
        
    }
    
    public void addConnection(Socket socket){ // Metodo a modificar cuando implementemos el login.
        Connection c = new Connection(socket);
        c.uid = connectionUid;
        connectionUid++;
        clients.put(c.uid,c);
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
        listener.start();
    }    
    
    private void quitQueue(Connection con){
        queue.quit(con);
    }
    public void onDisconnectClient(Connection con) {
        if (queue.isConnectionInQueue(con)){
            queue.quit(con);
        }
        clients.remove(con.uid);
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
                 addConnection(socket);                
                 System.err.println("New incoming connection detected.");
             }
         } catch (IOException ex) {
             System.err.println("IOException::while creating a new clientSocket");
         }

        }       

     }
}
