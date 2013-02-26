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
    private Connection[] players;
    
    public Game(Connection p1, Connection p2){
        players = new Connection[2];
        players[0] = p1;
        players[1] = p2;
    }
    
    
    
    
}
