/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.main;

import alpha.server.connection.*;
import alpha.server.scenario.GameThread;
import alpha.server.scenario.Scenario;
import com.sun.xml.internal.ws.transport.http.server.ServerAdapterList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author kirtash
 */
public class Server {
    //INET Att
    public int PORT = 5050;
    public ConnectionListener listener;
    public ThreadGroup threadGroup;
    //
    
    //Listas//
    public ArrayList<GameThread> games;
    public ArrayList<Connection> clients;

    
    public GameQueue queue;
    public int connectionUid;
    
    private final Protocol protocol;
    
    public static void main(String [] args){
        Server s = new Server();
        s.serverStart();
    }
    
    public Server(){
        this.protocol = new Protocol(this);
        this.threadGroup = new ThreadGroup("Connections");
        this.queue = new GameQueue();
        this.connectionUid = 0;
        this.clients = new ArrayList<>();
        this.listener = new ConnectionListener();
        this.games = new ArrayList<>();
    }
    
    /* SELF functions */
    
    private void serverStart(){
        System.out.println("ServerStarting!");
        this.listener.start(); // Start listening connections
    }
    
    private void addConnection(Socket socket) {
        Connection connection = new Connection(protocol, socket, threadGroup);
        connection.uid = connectionUid;
        connectionUid++;
        clients.add(connection);
    }
    
    private void startGame(Connection[] c) {
        GameThread thread = new GameThread(c);
        games.add(thread);
    }
    
    
    
    /* CONNECTION functions */
    public void joinQueue(Connection connection){
        Connection [] c = queue.join(connection);
        if (c!= null){
            this.startGame(c);
        }
    }

    public boolean quitQueue(Connection aThis) {
        return queue.quitQueue(aThis);
    }    
    
    
    /**
     * CONNECTION LISTENER: Escolta i gestiona las conexions al servidor
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
                Socket socket = ss.accept();
                addConnection(socket);                
            }
        } catch (IOException ex) {
            System.err.println("IOException::while creating a new clientSocket");
        }
        
    }       
    
    }
}
