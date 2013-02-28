/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import clientepis.Cliente;
import clientepis.Game;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GameMain {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Cliente c = new Cliente();
        Game game = c.startGame();
        JFrame frame = new JFrame();
        Actor[] actors = new Actor[game.getPlayers().length];
        for (int i = 0; i < actors.length; i++) {
            if(i%2==0) {
                actors[i] = new Hero();
            }else {
                actors[i] = new Villain();
            }
            actors[i].setPosition((int)game.getPlayers()[i].pos[0],(int)game.getPlayers()[i].pos[1]);
        }
        Board board = new Board(actors, c, game);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(board);
        frame.add(board);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        c.readyToStart();
    }
}