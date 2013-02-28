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
    private Actor[] actors;
    private Cliente c;
    private Game game;

    public Board(Actor[] actors, Cliente c, Game game) {
        this.actors = actors;
        this.c = c;
        this.game = game;

        setBackground(Color.BLACK);
    }

    @Override
    public void addNotify(){
        super.addNotify();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                synchronized(game){
                    //System.out.println(game);
                    for (int i = 0; i < actors.length; i++) {
                        actors[i].setPosition((int)game.getPlayers()[i].pos[0],(int)game.getPlayers()[i].pos[1]);
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

        // DRAW ACTORS
        for (int i = 0; i < actors.length; i++) {
            Actor actor = actors[i];
            g2d.drawImage(actor.getImage(), actor.getX(), actor.getY(), this);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) { }

    @Override
    public void keyPressed(KeyEvent ke) {
        Player p = game.getMyPlayer();
        if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
            float[] pos = {p.pos[0], p.pos[1]+20};
            c.goTo(pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_UP) {
            float[] pos = {p.pos[0], p.pos[1]-20};
            c.goTo(pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
            float[] pos = {p.pos[0]-20, p.pos[1]};
            c.goTo(pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            float[] pos = {p.pos[0]+20, p.pos[1]};
            c.goTo(pos);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {}
}
