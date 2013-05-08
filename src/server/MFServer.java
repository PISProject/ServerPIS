/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import connections.Connection;
import database.MySQLConnection;
import game.GameEngine;
import game.models.Game;
import game.models.Games;
import game.monsters.Monsters;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author kirtash
 */
public class MFServer {
    public static void main(String[] args) {
       SERVER = new MFServer();
    }
    
    public static final boolean DEBUG_MONSTERS = true;
    public static final boolean DEBUG_SERVER = true;
    public static final boolean DEBUG_CONNECTIONS = true;
    public static final boolean DEBUG_MYSQL = true;
    public static final boolean DEBUG_GAMES = true;
    public static final boolean DEBUG_XML = true;
    public static final boolean DEBUG_SCENARIO = true;
    
    
    private static final String MONSTERS_PATH = "/src/resources/monsters/monsters.xml";
    private static final String GAMES_PATH = "/src/resources/games/games.xml";
    public static MFServer SERVER;
    public static boolean ACCEPT_CONNECTIONS = false;

    public static int game_id =0;
    //INET Att
    public int PORT = 5050;
    public ConnectionListener listener;
    public ThreadGroup threadGroup;
    //
    
    //Controlador de la base de datos
    public LoginManager login;
    
    //Listas//
    public ConcurrentHashMap<Integer,GameEngine> games;
    public ConcurrentHashMap<Integer,Connection> clients;
    private ConcurrentHashMap<Integer,GameQueue> queues;
    public Monsters monsters;
    public Games game_models;
    private XMLParser xmlParser;

    public int connectionUid=0; //Provisional mientras no esta implementado el Login
   
    // Buffer de strings para la interficie grafica
    
    /**
     *
     */
    public MFServer(){
        System.out.println("||==========================================||");
        System.out.println("||=============== MFServer =================||");
        System.out.println("||==========================================||");
        System.out.println("||Project developed by                      ||");
        System.out.println("||**Aaron Negrin                            ||");
        System.out.println("||**Albert Folch                            ||");
        System.out.println("||**Pablo Martinez                          ||");
        System.out.println("||**Xavi Moreno                             ||");
        System.out.println("||==========================================||");      
        System.out.println(""); 
  
        
        games = new ConcurrentHashMap<>();
        clients = new ConcurrentHashMap<>();

        
        //Intentamos crear el connectionListener
        try{
            listener = new ConnectionListener();
        }catch(IOException ex){
            //No ha podido ser
            if(MFServer.DEBUG_SERVER){ 
               System.out.println("==> [SERVER] Listener could not be created! Leaving system [EXIT]");
            }
            
            System.exit(1);
        }
        
        try {
            //Intentamos crear la base de datos
            if(MFServer.DEBUG_SERVER){
                System.out.print("==> [SERVER] Connecting to database ...");
            }
            login = new LoginManager(new MySQLConnection());
            System.out.println("[DONE]");
        } catch (SQLException ex) {
            if(MFServer.DEBUG_SERVER){
                System.out.println("[X] :: SQL Exception :: Closing server");
                //System.exit(1);
            }
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            if(MFServer.DEBUG_SERVER){
                System.out.println("[X] :: MySQL Driver not found! :: Closing server");
                //System.exit(1);
            }
        }
        threadGroup = new ThreadGroup("g");
        monsters = new Monsters();
        try {
            System.out.print("==> [SERVER] Creating XML Parser ..");
            xmlParser = new XMLParser();
            System.out.println("[DONE]");
            try{
                System.out.print("==> [SERVER] Loading monsters ..");
                monsters = xmlParser.parseMonsterList(MONSTERS_PATH);
                System.out.println("[DONE]");
            } catch( IOException | ParserConfigurationException | SAXException e){
                if (MFServer.DEBUG_XML){
                    System.out.println("[X] :: Closing server");
                    System.exit(1);
                }
            }
            try{
                System.out.print("==> [SERVER] Loading games ..");
                game_models = new Games();
                xmlParser.parseGameList(GAMES_PATH);
                System.out.println(" [DONE]");
            } catch (IOException | ParserConfigurationException | SAXException e){
                if (MFServer.DEBUG_XML){
                    System.out.println("[X] :: Closing server");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (ParserConfigurationException ex) {
            System.out.println("[X] :: Cannot create parser");
        }
        
        System.out.print("==> [SERVER] Creating game queues ..");
        queues = new ConcurrentHashMap<>();
        Game g;
        for (int i: Games.GAME_LIST.keySet()) {
            g = Games.GAME_LIST.get(i);
            queues.put(g.id, new GameQueue(g.id,g.numplayers));
        }
        System.out.println("[DONE]");
        startServer();
        
    }
    
    public void addPlayer(Connection con){
        clients.put(con.uid,con);
    }
    
    public void joinQueue(int id, Connection con) {
        queues.get(id).join(con);
    }
    
    public void createGame(int game_type, Connection [] g){
        game_id++;
        games.put(game_id, new GameEngine(game_id, g, game_models.getGame(game_type)));
    }

    private void startServer() {
        ACCEPT_CONNECTIONS = true;

        listener.start();
    }
    
    public boolean quitQueue(int game_type, Connection con){
        return queues.get(game_type).quit(con);
    }
    
    
    
    public void onDisconnectClient(Connection con) {
        clients.remove(con.uid);
        if(MFServer.DEBUG_SERVER){
            System.out.println("==> [SERVER] Client "+con.uid+" left.");
        }
    }

    boolean isClientOnline(int id) {
        return (clients.get(id) != null);
    }
    
    public void endGame(int uid){
        games.remove(uid);
    }
    
    
    
    
    
    
    
    
    
    
    
   
    /**
     * ConnectionListener: Clase interna que actua como Thread para escuchar las conexiones entrantes.
     */
    public class ConnectionListener extends Thread{
         public ServerSocket ss;


         public ConnectionListener() throws IOException{ 
            ss = new ServerSocket(PORT);
         }
         
     @Override
     public void run() {
         try {
            if (MFServer.DEBUG_SERVER){
                     System.out.println("==> [SERVER] Waiting connection...");
            }
             while(ACCEPT_CONNECTIONS){

                 
                 Socket socket = ss.accept();
                 new Connection(socket, login);
                 
                if (MFServer.DEBUG_SERVER){
                     System.out.println("==> [SERVER] New incoming connection detected.");
                 }
             }
         } catch (IOException ex) {
            if (MFServer.DEBUG_SERVER){
                System.out.println("==> [SERVER] Error while creating a new client socket. Listener it's now down.");
            }
         }

        }       

     }
}
