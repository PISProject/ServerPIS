/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.main;

import alpha.server.connection.*;
import alpha.server.scenario.Scenario;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author kirtash
 */
public class Server {
    public int connectionUid;
    public int PORT = 5050;
    ArrayList<Connection> clients;
    ConnectionListener listener;
    Scenario game;
    
    public Server(){
        this.connectionUid = 0;
        this.clients = new ArrayList<>();
        this.listener = new ConnectionListener();
        listener.start();
        this.game = new Scenario();
    }
    
    private void addConnection(Connection connection) {
        clients.add(connection);
        connection.setScenario(game);
        game.addHeroe(connectionUid);
        connectionUid+=1;
    }

    
    
    
    /**
     * CONNECTION LISTENER: Escolta i gestiona las conexions al servidor
     */
    public class ConnectionListener extends Thread{
        public ServerSocket ss;
        public ThreadGroup threadGroup;
    
        public ConnectionListener(){ 
            try {
                ss = new ServerSocket(PORT);
            } catch (IOException ex) { // Catch IO Exception
                System.err.println("Could not create server socket!");
                System.exit(1);
            }
            threadGroup = new ThreadGroup("Connections");
        }
    
    @Override
    public void run() {
        try {
            while(true){
                Socket socket = ss.accept();
                Connection connection = new Connection(socket,threadGroup);
                addConnection(connection);
                connection.start();
                
            }
        } catch (IOException ex) {
            System.err.println("IOException::while creating a new clientSocket");
        }
        
    }       
    
    }
}
