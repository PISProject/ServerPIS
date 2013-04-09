/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.Connection;
import game.GameEngine;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
    
    //Listas//
    public ArrayList<GameEngine> games;
    public ArrayList<Connection> clients;

    
    public GameQueue queue;
    public int connectionUid=0; //Provisional mientras no esta implementado el Login
   

    
    public MFServer(){
        listener = new ConnectionListener();
        threadGroup = new ThreadGroup("g");
        games = new ArrayList<>();
        clients = new ArrayList<>();
        queue = new GameQueue();
        startServer();
        
    }
    
    public void addConnection(Socket socket){ // Metodo a modificar cuando implementemos el login.
        Connection c = new Connection(socket);
        c.uid = connectionUid;
        connectionUid++;
        clients.add(c);
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
    
    
    
    
    
    
    
    
    
    
    
    
    
   
    /**
     * ConnectionListener: Clase interna que actua como Thread para escuchar las conexiones entrantes.
     */
    public class ConnectionListener extends Thread{
         public ServerSocket ss;


         public ConnectionListener(){ 
             try {
                 ss = new ServerSocket(PORT);
             } catch (IOException ex) { // Catch IO Exception
                 System.err.println("Could not create server socket!");
                 System.exit(1);
             }
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
