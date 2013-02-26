/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 * Class with all items, and status ingame.
 * @author kirtash
 */
public class Game {
    private Player[] players;
    
    public Game(Player p1, Player p2){
        players = new Player[2];
        players[0] = p1;
        players[1] = p2;
    }
    
    
    
    
}
