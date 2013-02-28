/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

/**
 *
 * @author zenbook
 */
public class Game {
    private Player[] players;
    private int myId; //Esta es la ID que el server le proporciona al cliente para que el cliente sepa que jugador es.
    
    public Game(int id) {
        this.myId = id;
    }
    
    public Player getMyPlayer() {
        boolean trobat = false;
        int i = 0;
        Player p = null;
        while(!trobat && i<players.length) {
            if(players[i].id == myId) {
                p = players[i];
                trobat = true;
            }
            i++;
        }
        return p;
    }
    
    public int getMyID() {
        return myId;
    }
    
    public Player[] getPlayers() {
        return players;
    }
    
    public Player[] getOtherPlayers() {
        Player[] pls = new Player[players.length-1];
        int j = 0;
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if(player.id != myId) {
                pls[j] = player;
                j++;
            }
        }
        return pls;
    }
    
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    @Override
    public String toString() {
        String res = "Game{" + "players={";
        for (int i = 0; i < players.length; i++) {
            res+=players[i]+(i<players.length-1?",":"");
        }
        res+="}}";
        return res;
    }
    
    
}
