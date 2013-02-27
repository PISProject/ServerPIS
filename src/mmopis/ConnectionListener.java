/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author zenbook
 */
public class ConnectionListener extends Thread {
    public int PORT;
    public ServerSocket ss;
    public Server server;
    public ThreadGroup threadGroup;
    
    public ConnectionListener (Server server, int port){
        try {
            ss = new ServerSocket(port);
            this.server = server;
            this.PORT = port;
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
                connection.start();
                server.addConnection(connection);
            }
        } catch (IOException ex) {
            //Logger.getLogger(ConnectionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
