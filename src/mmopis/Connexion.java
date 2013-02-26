/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zenbook
 */
public class Connexion extends Thread{
    
    public DataInputStream in;
    public DataOutputStream out;
    public ProtocolGame protocol;
    
    
    public Connexion(Socket client, ThreadGroup threads){
        super(threads,"threadConnection");
        try {
            
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            protocol = new ProtocolGame(this);
            this.start();
            
            System.out.println("ASDASDASDASDASDASDASD");
        } catch (IOException ex) {
            System.err.println("I/O Exception");
        }
        
    }

    @Override
    public void run() {
        while(true){
            try {
                String entrada = in.readUTF();
                
            } catch (IOException ex) {
                Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void pushToClient(String message){
        try {
            out.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
