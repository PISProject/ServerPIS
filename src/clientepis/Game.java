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
    private int myId;
    
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
        }
        return p;
    }
    
    public Player[] getPlayers() {
        return players;
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
