/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import java.util.ArrayList;

/**
 *
 * @author kirtash
 */
public class GameQueue implements Runnable{
    ArrayList <Player> lista;
    public void join(Player p){
        lista.add(p);
    }
    public void exit(Player p){
        lista.remove(p);
    }

    @Override
    public void run() {
        if (lista.size()>2){
            
        }
    }

}
