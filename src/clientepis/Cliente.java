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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zenbook
 */
public class Cliente {
    
    public static final String IP = "localhost";
    public static final int PORT = 5050;
    
    public DataInputStream in;
    public DataOutputStream out;
    
    public Cliente() { //En android: public Cliente(Activity a);
                        //para poder utilizar: Toast.makeText(a.getBaseContext(),"No se ha podido empezar la partida", Toast.LENGTH_SHORT); 
        try {
            Socket socket = new Socket(IP, PORT);
            
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
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
            out.writeUTF("1"); //1 significa empezar partida
            return in.readUTF().equals("1");
        } catch (IOException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
        return false;
    }
    
    public void stopWaiting() {
        try {
            out.writeUTF("2"); //2 significa dejar de esperar en la lista de espera
        } catch (IOException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
    }
    
    public void readyToStart() {
        
        try {
             boolean res;
             out.writeUTF("I'm ready to start the game");
             res = in.readUTF().equals("1");
             if(res){
                 ThreadGame threadGame = new ThreadGame(in);
                 threadGame.start();
             }
         } catch (IOException ex) {
             //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
         } 
    }
    
    public void goTo(float[] pos){
        try {
            String res = "";
            res += "1|"; //Comanda para ProtocolGame (significa ir a una posicion)
            for (int i = 0; i < pos.length; i++) {
                res+=pos[i]+(i < pos.length-1?",":"");
            }
            out.writeUTF(res);
        } catch (IOException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
    }
    
    
    public static void main(String[] args) {
        
    }
}
