/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Albert
 */
public class ThreadGame extends Thread{
    private DataInputStream in;
    public ThreadGame (DataInputStream in){
        this.in = in;
    }
    
    @Override
    public void run() {
        while(true){
            try {
                
                System.out.println(in.readUTF());
            } catch (IOException ex) {
                
            }
        }
        
        
    }
}
