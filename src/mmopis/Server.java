/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.util.ArrayList;

/**
 *
 * @author zenbook
 */
public class Server {
    private static final int PORT = 5050; // PORT IN WICH WE ARE RUNNING
    
    private ArrayList<Connection> con_clients;
    private ArrayList<Game> active_games;
    private GameQueue queue;
    
    
    public Server(){
        //INIT lists
        con_clients = new ArrayList();
        active_games = new ArrayList();
        queue = new GameQueue();
        
        //INIT server listener
        System.out.print("Starting to listen connections...");
        ConnectionListener listener = new ConnectionListener (this,PORT);
        listener.start();
        System.out.println("[DONE]");
        
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print("Starting server...");
        Server server = new Server();
        System.out.println("[Done]");
    }

    public void addConnection(Connection connection) {
        con_clients.add(connection);
    }
    
    public synchronized void joinQueue(Connection p){ //debe estar sincronizado, porque pueden llamarlos varios clientes a la vez desde Connetion
        synchronized(queue) {
            Connection[] a = queue.join(p);
            if(a!= null){
                startGame(a);
            } 
        }
    }

    private void startGame(Connection[] a) {
        Game game = new Game(a[0],a[1]);
        active_games.add(game);
        for (Connection i : a) {
            i.startGame(game);
        }
    }

    public synchronized void quitQueue(Connection aThis) {
        synchronized(queue) {
            queue.exit(aThis);
        }
    }
}
