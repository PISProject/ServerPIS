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
enum Status{NOT_LOGGED, WAITING_QUEUE, OUT_GAME, IN_GAME,DISCONNECTED, LOADING};

/**
 *
 * @author zenbook
 */
public class Connection extends Thread{
    
    private DataInputStream in;
    private DataOutputStream out;
    private Protocol protocol;
    private ProtocolGame protocolGame;
    private ProtocolLogin protocolLogin;
    private Game game;
    private Server server;

    public Status status;
    
    
    
    public Connection(Socket client, ThreadGroup threads, Server server){
        super(threads,"threadConnection");
        try {
            this.status = Status.NOT_LOGGED;
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
            this.protocolGame = new ProtocolGame(this);
            this.protocol = new Protocol(this);
            this.protocolLogin = new ProtocolLogin(this);
            this.server = server;
            
        } catch (IOException ex) {
            System.err.println("I/O Exception");
        }
        
    }

    @Override
    public void run() {
        while(!(status == Status.DISCONNECTED)){
            String entrada = null;
            try {
                entrada = in.readUTF();
            } catch (IOException ex) {
                System.err.println("IO Exception");
            }
            if (status == Status.OUT_GAME){
                protocol.parse(entrada);
            }
            else if(status == Status.NOT_LOGGED){
                protocolLogin.parse(entrada);
            }
            else if(status == Status.IN_GAME){
                protocolGame.parse(entrada);
            }
            else if(status == Status.LOADING){
                
            }
        }
    }
    public void pushToClient(String message) throws IOException{
        try {
            out.writeUTF(message);
        } catch (IOException ex) {
            System.err.println("I/O Exception");
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

    public void exitQueue() {
        stateChange(Status.OUT_GAME);
        try {
            pushToClient("0");
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
        server.quitQueue(this);
    }

    public void startGame(Game game) {
        try {
            pushToClient("1");
            stateChange(Status.LOADING);
        } catch (IOException ex) {
        }
        
    }
}
