/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

/**
 *
 * @author zenbook
 */
public class ProtocoloCliente{

    
    public ProtocoloCliente() {
    }
    
    public String[] parserInstruction(String s){
        // We assume that info was correctly sent.
        return s.split("[|]");
    }
    
    public void parserGame(String string, Game game) { //1,10.0,10.0*2,10.0,10.0
        String[] splitted = string.split("[*]");
        String s;
        String[] player;
        Player p;
        int id;
        float[] pos;
        Player[] players = new Player[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            s = splitted[i];
            player = s.split("[,]");
            id = Integer.parseInt(player[0]);
            pos = new float[2];
            pos[0] = Float.parseFloat(player[1]);
            pos[1] = Float.parseFloat(player[2]);
            p = new Player(id, pos);
            players[i] = p;
        }
        synchronized(game) {
            game.setPlayers(players);
        }
    }
    
}
