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
    
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private ProtocolOutGame protocolOutGame;
    private ProtocolGame protocolGame;
    private ProtocolLogin protocolLogin;
    private Game game;
    private Server server;

    public Status status;
    
    
    
    public Connection(Socket client, ThreadGroup threads, Server server){
        super(threads,"threadConnection");
        try {
            this.status = Status.OUT_GAME; //Status.NOT_LOGGED; -> Lo dejo como OUT_GAME para hacer pruebas sin logar-se.
            this.client = client;
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
            this.protocolGame = new ProtocolGame(this);
            this.protocolOutGame = new ProtocolOutGame(this);
            this.protocolLogin = new ProtocolLogin(this);
            this.server = server;
            
        } catch (IOException ex) {
            System.err.println("I/O Exception");
        }
    }

    @Override
    public void run() {
        while(status != Status.DISCONNECTED){
            try {
                String entrada = in.readUTF();
                if (status == Status.OUT_GAME){
                    protocolOutGame.parse(entrada);
                }else if(status == Status.NOT_LOGGED){
                    protocolLogin.parse(entrada);
                }else if(status == Status.IN_GAME){
                    protocolGame.parse(entrada);
                }else if(status == Status.LOADING){

                }
            } catch (IOException ex) {
                System.err.println("IO Exception");
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
    public synchronized void stateChange(Status s){
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
            stateChange(Status.IN_GAME);
            //stateChange(Status.LOADING); <- esto es lo correcto
        } catch (IOException ex) {
        }
    }
    
   
    /**
     * Función close, cierra el socket y deja al cliente como desconectado
     * 
     */
    
    public void close() {
        try {
            stateChange(Status.DISCONNECTED);
            in.close();
            out.close();
            client.close();
        } catch (IOException ex) {
            System.err.println("Connection is already close.");
        }
    }
}
