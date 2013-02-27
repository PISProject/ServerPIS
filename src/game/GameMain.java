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
        Hero hero = new Hero();
        hero.setPosition((int)game.getPlayers()[0].pos[0],(int)game.getPlayers()[0].pos[1]);
        Villain villain = new Villain();
        villain.setPosition((int)game.getPlayers()[1].pos[0],(int)game.getPlayers()[1].pos[1]);
        Board board = new Board(hero, villain, c, game);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(board);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        c.readyToStart();
    }
}