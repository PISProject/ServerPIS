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
