/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Albert
 */
public class ThreadGame extends Thread{
    private DataInputStream in;
    private Game game;
    private ProtocoloCliente p;
    
    public ThreadGame (DataInputStream in, Game game){
        this.in = in;
        this.game = game;
        p = new ProtocoloCliente();
    }
    
    @Override
    public void run() {
        while(true){
            try {
                String res = in.readUTF();
                synchronized(game) {
                    p.parserGame(res, game);
                }
            } catch (IOException ex) {
                
            }
        }
        
        
    }
}
