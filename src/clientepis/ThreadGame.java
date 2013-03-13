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
    private Protocol protocol;
    private Game game;
    
    public ThreadGame (Protocol protocol, Game game){
        this.protocol = protocol;
        this.game = game;
    }
    
    @Override
    public void run() {
        while(true){
            protocol.readMap(game);
        }
        
        
    }
}
