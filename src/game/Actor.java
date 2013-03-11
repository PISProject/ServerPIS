/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import clientepis.Player;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Actor{
    private float[] pos;
    private Image image;
    private enum actorAnimation{STANDING, MOVING, ATTACKING, CASTING_SPELL1, CASTING_SPELL2, CASTNG_SPELL3}
    public Actor(String pathImage){
        image = new ImageIcon(pathImage).getImage();
    }

    public void setPosition(float[] pos){
        this.pos = pos;
    }

    public float[] getPosition(){
        return this.pos;
    }

    public float getX(){
        return getPosition()[0];
    }

    public float getY(){
        return getPosition()[1];
    }

    public Image getImage(){
        return this.image;
    }
}
