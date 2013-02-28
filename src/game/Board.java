/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import clientepis.Cliente;
import clientepis.Game;
import clientepis.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class Board extends JPanel implements KeyListener {
    private Villain[] villains;
    private Hero hero;
    private Cliente c;
    private Game game;

    public Board(Hero h, Villain[] villains, Cliente c, Game game) {
        this.villains = villains;
        this.c = c;
        this.game = game;
        this.hero = h;

        setBackground(Color.BLACK);
    }

    @Override
    public void addNotify(){
        super.addNotify();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                synchronized(game){
                    Player[] otherPlayers = game.getOtherPlayers();
                    /*Sólo refresco la posición de los demás players*/
                    for (int i = 0; i < otherPlayers.length; i++) {
                        Player player = otherPlayers[i];
                        villains[i].setPosition(player.pos);
                    }
                    repaint();
                }
            }
        };
        Timer t = new Timer();
        t.schedule(task, 0, 100);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

        /*Pongo en pantalla a mis contrincantes*/
        for (int i = 0; i < villains.length; i++) {
            Actor actor = villains[i];
            g2d.drawImage(actor.getImage(), (int)actor.getX(), (int)actor.getY(), this);
        }
        
        /*Dibujo en pantalla a mi héroe*/
        g2d.drawImage(hero.getImage(), (int)hero.getX(), (int)hero.getY(), this);
    }

    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        /*Aquí refresco mi posición y se la envio al servidor*/
        Player p = game.getMyPlayer();
        if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
            p.pos[1] += 20;
            hero.setPosition(p.pos);
            c.goTo(p.pos);
        }
        else if(ke.getKeyCode() == KeyEvent.VK_UP) {
            p.pos[1] -= 20;
            hero.setPosition(p.pos);
            c.goTo(p.pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
            p.pos[0] -= 20;
            hero.setPosition(p.pos);
            c.goTo(p.pos);
        }
        else if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            p.pos[0] += 20;
            hero.setPosition(p.pos);
            c.goTo(p.pos);
        }
    }
    @Override
    public void keyReleased(KeyEvent ke) {}
}
