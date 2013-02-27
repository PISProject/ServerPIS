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
    
    public static final String IP = "localhost";
    public static final int PORT = 5050;
    
    private DataInputStream in;
    private DataOutputStream out;
    
    private ProtocoloCliente protocol;
    
    private Game game;
    
    public Cliente() { //En android: public Cliente(Context context);
                        //para poder utilizar: Toast.makeText(a.getBaseContext(),"No se ha podido empezar la partida", Toast.LENGTH_SHORT); 
        try {
            Socket socket = new Socket(IP, PORT);
            
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
            protocol = new ProtocoloCliente(this);
            
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
    
    public boolean startGame() {
        try {
            out.writeUTF(Protocol.JOIN_QUEUE);
            String en = in.readUTF();
            protocol.parse(en);
        } catch (Exception ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
        return false;
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
             res = in.readUTF().equals("1");
             if(res){
                 ThreadGame threadGame = new ThreadGame(in);
                 threadGame.start();
             }
         } catch (Exception ex) {
             //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
         } 
    }
    
    public void goTo(float[] pos){
        try {
            String res = "";
            res += Protocol.INGAME_MOVE_TO+"|"; //Comanda para ProtocolGame (significa ir a una posicion)
            for (int i = 0; i < pos.length; i++) {
                res+=pos[i]+(i < pos.length-1?",":"");
            }
            out.writeUTF(res);
        } catch (Exception ex) {
        }
    }
    
    public void close() {
        try {
            out.writeUTF(Protocol.CLOSE); //La función '0' cierra la conexión con el servidor de forma segura
        } catch (Exception ex) {
        }
    }
    
    
    public static void main(String[] args) {
        final Cliente c = new Cliente();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                 boolean a = c.startGame();
                if(a) {
                    System.out.println("Ya puedo empezar la partida");
                }
            }
        });
        t.start();
        Scanner sc = new Scanner(System.in);
        sc.next();
        c.stopWaiting();
        
        c.close(); //Hay que hacer siempre el close por parte del cliente.
    }
}
