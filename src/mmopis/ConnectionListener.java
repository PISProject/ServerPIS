/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zenbook
 */
public class ConnectionListener extends Thread {
    public static final String IP = "localhost";
    public static final int PORT = 5050;
    public ServerSocket ss;
    public Server server;
    public ThreadGroup threadGroup;
    
    public ConnectionListener (Server server){
        try {
            ss = new ServerSocket(PORT);
            this.server = server;
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
                Connection connection = new Connection(socket,threadGroup,server);
                server.addConnection(connection);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
