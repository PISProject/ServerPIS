/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

/**
 *
 * @author zenbook
 */
public class Player {
    public float[] pos;
    //private float rot;
    public int vida;
    public int id;
    
    public Player(int id, float[] pos) {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Player{" + "pos={" + pos[0] + ","+pos[1]+"}, vida=" + vida + ", id=" + id + '}';
    }
    
}
