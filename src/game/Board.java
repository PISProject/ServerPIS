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
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class Board extends JPanel implements KeyListener{
    private int posX, posY;
    private Hero hero;
    private Villain villain;
    private Map map;
    private Cliente c;
    private Game game;

    public Board(Hero hero, Villain villain, Map map, Cliente c, Game game) {
        this.hero = hero;
        this.villain = villain;
        this.map = map;
        this.c = c;
        this.game = game;

        addKeyListener(this);
        setBackground(Color.BLACK);
        //setDoubleBuffered(true);
    }

    @Override
    public void addNotify(){
        super.addNotify();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                synchronized(game){
                    //System.out.println(game);
                    hero.setPosition((int)game.getPlayers()[0].pos[0],(int)game.getPlayers()[0].pos[1]);
                    villain.setPosition((int)game.getPlayers()[1].pos[0],(int)game.getPlayers()[1].pos[1]);
                    repaint();
                }
            }
        };
        Timer t = new Timer();
        t.schedule(task, 0, 100);
    }

    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;


        // DRAW HERO
        g2d.drawImage(hero.getImage(), hero.getX(), hero.getY(), this);
        
        g2d.drawImage(villain.getImage(), villain.getX(), villain.getY(), this);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        System.out.println("HOLA");
        Player p = game.getMyPlayer();
        if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
            float[] pos = {p.pos[0], p.pos[1]+1};
            c.goTo(pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_UP) {
            float[] pos = {p.pos[0], p.pos[1]-1};
            c.goTo(pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
            float[] pos = {p.pos[0]-1, p.pos[1]};
            c.goTo(pos);
        }
        if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            float[] pos = {p.pos[0]+1, p.pos[1]};
            c.goTo(pos);
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        
    }
}
