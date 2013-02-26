/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
enum Status{NOT_LOGGED, WAITING_QUEUE, OUT_GAME, IN_GAME,DISCONNECTED};

/**
 *
 * @author zenbook
 */
public class Connection extends Thread{
    
    public DataInputStream in;
    public DataOutputStream out;
    public ProtocolGame protocol;
    public Game game;
    public Server server;

    public Status status;
    
    
    
    public Connection(Socket client, ThreadGroup threads, Server server){
        super(threads,"threadConnection");
        try {
            this.status = Status.NOT_LOGGED;
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
            this.protocol = new ProtocolGame(this);
            this.server = server;
            
        } catch (IOException ex) {
            System.err.println("I/O Exception");
        }
        
    }

    @Override
    public void run() {
        while(!(status == Status.DISCONNECTED)){
            if (status == Status.OUT_GAME){
                try {
                    String entrada = in.readUTF();
                    
                 } catch (IOException ex) {
                     System.err.println("I/O Error");
                 }
            }
            else if(status == Status.NOT_LOGGED);
            
            else if(status == Status.IN_GAME){
                
            }
            else if(status == Status.OUT_GAME){
                
            }
        }
    }
    public void pushToClient(String message){
        try {
            out.writeUTF(message);
        } catch (IOException ex) {
            System.err.println("HHAHHAHAH");
        }
        
    }
    
    /**
     * Cambia de estado el hilo.
     * @param s
     */
    public void stateChange(Status s){
        this.status = s;
    }
    
    public void joinQueue(){
        stateChange(Status.WAITING_QUEUE);
        server.joinQueue(this);
    }
}
