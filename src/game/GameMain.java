/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import clientepis.Cliente;
import clientepis.Game;
import clientepis.Player;
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
        Villain[] villains = new Villain[game.getOtherPlayers().length];
        
        /*Defino la posición de mi player, en este caso es un héroe*/
        Hero hero = new Hero();
        hero.setPosition(game.getMyPlayer().pos);
        
        /*Defino las posiciones de los demás players que son villanos desde mi punto de vista*/
        for (int i = 0; i < villains.length; i++) {
            Player p = game.getOtherPlayers()[i];
            villains[i] = new Villain();
            villains[i].setPosition(p.pos);
        }
        
        Board board = new Board(hero, villains, c, game);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addKeyListener(board);
        frame.add(board);
        frame.setSize(800, 600);
        frame.setVisible(true);
        
        c.readyToStart();
    }
}