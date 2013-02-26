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
public class ComInterface extends Thread {
    public static final String IP = "localhost";
    public static final int PORT = 17594;
    public ServerSocket ss;
    public ThreadGroup threadGroup;
    
    public ComInterface (){
        try {
            ss = new ServerSocket(PORT);
        } catch (IOException ex) { // Catch IO Exception
            System.err.println("Could not create server socket!");
        }
        threadGroup = new ThreadGroup("threadsConnections");
        this.start();
    }
    
    
    

    
    @Override
    public void run() {
        try {
            
            while(true){
                Socket socket = ss.accept();
                Connexion connection = new Connexion(socket,threadGroup);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ComInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
