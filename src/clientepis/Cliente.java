/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author zenbook
 */
public class Cliente {
    
    public static final String IP = "80.28.175.213";
    public static final int PORT = 5050;
    
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    
    private ProtocoloCliente protocol;
    
    private Game game;
    
    public Cliente() { //En android: public Cliente(Context context);
                        //para poder utilizar: Toast.makeText(a.getBaseContext(),"No se ha podido empezar la partida", Toast.LENGTH_SHORT); 
        try {
            socket = new Socket(IP, PORT);
            
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            protocol = new ProtocoloCliente();
            
        } catch (UnknownHostException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        } catch (IOException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
    }
    /**
     * Mètode que se queda esperando a que le responda el servidor.
     * Si devuelve 'true' es que puede empezar la partida.
     * Si devuelve 'false' no podrá empezar la partida.
     * 
     * @return boolean
     */
    
    public Game startGame() {
        try {
            out.writeUTF(Protocol.JOIN_QUEUE);
            String en = in.readUTF(); //Por aquí recibiré algo así: 0x01|<id>&1,10.0,10.0*2,10.0,10.0
            String[] func = protocol.parserInstruction(en);
            switch (func[0]) {
                case Protocol.APPROVED:
                    return null;
                case Protocol.READY_TO_START_GAME:
                    func = func[1].split("[&]");
                    int id = Integer.parseInt(func[0]);
                    game = new Game(id);
                    protocol.parserGame(func[1], game); //Cambia el valor de game
                    return game;
            }
        } catch (Exception ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
        return null;
    }
    
    public void stopWaiting() {
        try {
            out.writeUTF(Protocol.QUIT_QUEUE); //2 significa dejar de esperar en la lista de espera
        } catch (Exception ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
    }
    
    public void readyToStart() {
        
        try {
             boolean res;
             out.writeUTF(Protocol.READY_TO_START_GAME);
             res = in.readUTF().equals(Protocol.NOTIFY_GAME_STARTING);
             if(res){
                 System.out.println("Ahora si que empieza la partida");
                 ThreadGame threadGame = new ThreadGame(in, game);
                 threadGame.start();
             }
         } catch (Exception ex) {
             //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
         } 
    }
    
    public void goTo(final float[] pos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                String res = "";
                res += Protocol.INGAME_MOVE_TO+"|"; //Comanda para ProtocolGame (significa ir a una posicion)
                for (int i = 0; i < pos.length; i++) {
                    res+=pos[i]+(i < pos.length-1?",":"");
                }
                synchronized(out) {
                    out.writeUTF(res);
                }
                } catch (Exception ex) { }
            }
        }).start();
        
    }
    
    public void close() {
        try {
            out.writeUTF(Protocol.CLOSE); //La función '0' cierra la conexión con el servidor de forma segura
            out.close();
            in.close();
            socket.close();
            
        } catch (Exception ex) {
        }
    }
    
    
    public static void main(String[] args) {
        final Cliente c = new Cliente();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Game game = c.startGame();
                if(game!=null) {
                    System.out.println("Ya puedo empezar la partida");
                    System.out.println("Cargando: "+game);
                }
            }
        });
        t.start();
        Scanner sc = new Scanner(System.in);
        sc.next();
        c.readyToStart();
        sc.next();
        float[] a = {45.67f,32.5f};
        c.goTo(a);
        sc.next();
        c.stopWaiting();
        
        c.close(); //Hay que hacer siempre el close por parte del cliente.
    }
}
