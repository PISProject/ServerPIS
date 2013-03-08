/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



/**
 *
 * @author zenbook
 */
public class Connection extends Thread{
    
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private Protocol protocolOutGame;
    private ProtocolGame protocolGame;
    private ProtocolLogin protocolLogin;
    public Summoner summoner;
    private GameThread game;
    private Server server;

    public Constants.ClientStatus status;
    
    
    
    public Connection(Socket client, ThreadGroup threads, Server server){
        super(threads,"threadConnection");
        try {
            this.status = Constants.ClientStatus.OUT_GAME; //Status.NOT_LOGGED; -> Lo dejo como OUT_GAME para hacer pruebas sin logar-se.
            this.client = client;
            this.in = new DataInputStream(client.getInputStream());
            this.out = new DataOutputStream(client.getOutputStream());
            this.protocolGame = new ProtocolGame(this);
            this.protocolOutGame = new Protocol(this);
            this.protocolLogin = new ProtocolLogin(this);
            this.server = server;
            this.summoner = new Summoner(Summoner.getNextId());
            
        } catch (IOException ex) {
            System.err.println("I/O Exception");
        }
    }

    @Override
    public void run() {
        while(status != Constants.ClientStatus.DISCONNECTED){
            try {
                String entrada = in.readUTF();
                if (status == Constants.ClientStatus.OUT_GAME || status == Constants.ClientStatus.WAITING_QUEUE){
                    protocolOutGame.parse(entrada);
                }else if(status == Constants.ClientStatus.NOT_LOGGED){
                    protocolLogin.parse(entrada);
                }else if(status == Constants.ClientStatus.IN_GAME){
                    protocolGame.parse(entrada);
                }
            } catch (IOException ex) {
                this.close();
                System.err.println("IO Exception :: Client disconnected!!");
            }
        }
    }
    public void pushMapUpdate(String message)throws IOException{
        out.flush(); //Limpiamos lo que haya en el socket para que no se forme una cola
        out.writeUTF(message);
    }
    
    public void pushToClient(String message) throws IOException{
        //System.out.println("x: "+summoner.pos[0]+";"+"y: "+summoner.pos[1]+" :: Connection.java");
        try {
            out.writeUTF(message);
        } catch (IOException ex) {
            System.err.println("I/O Exception :: Not here!");
            this.close();
        }
    }
    
    /**
     * Cambia de estado el hilo.
     * @param s
     */
    public synchronized void stateChange(Constants.ClientStatus s){
        this.status = s;
    }
    
    public void joinQueue(){
        stateChange(Constants.ClientStatus.WAITING_QUEUE);
        server.joinQueue(this);
    }

    public void quitQueue() {
        stateChange(Constants.ClientStatus.OUT_GAME);
        server.quitQueue(this);
        try {
            pushToClient(Protocol.APPROVED);
        } catch (IOException ex) {
            System.err.println("IO Exception");
        }
    }

    public void startGame(GameThread game, String status) {
        try {
            this.game = game;
            stateChange(Constants.ClientStatus.IN_GAME);
            System.out.println("Connection.java: "+Protocol.READY_TO_START_GAME+"|"+summoner.summonerId+"&"+status); //Le paso la id que le asocio a ese player para que sepa desde cliente que player es el suyo.
            pushToClient(Protocol.READY_TO_START_GAME+"|"+summoner.summonerId+"&"+status);//Siempre hay que hacer los cambios del server y después notificarselos al cliente.
                                //NUNCA AL REVES!
        } catch (IOException ex) {
        }
    }
    
   
    /**
     * Función close, cierra el socket y deja al cliente como desconectado
     * 
     */
    
    public void close() {
        try {
            in.close();
            out.close();
            client.close();
            stateChange(Constants.ClientStatus.DISCONNECTED);
        } catch (IOException ex) {
            System.err.println("Connection is already close.");
        }
    }

    
    public void imReadyToStartGame() {
        synchronized(game){
            game.setReady(summoner.summonerId);
        }
    }

    // OUTPUT FUNCTIONS
    public void notifyGameStarting() {
        stateChange(Constants.ClientStatus.IN_GAME);
        try {
            pushToClient(Protocol.NOTIFY_GAME_STARTING);
        } catch (IOException ex) {
            //TODO
        }
    }

}
