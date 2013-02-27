/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Map {
    private int mapWidth, mapHeight, tileWidth, tileHeight;
    private int[] map = 
        { 0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0,
          1,1,1,1,1,1,1,1,1,1,
          0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0,
          0,0,0,0,0,0,0,0,0,0 
        };
    private Image[] tileImage;

    public Map(int mapWidth, int mapHeight){
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.tileImage = new Image[2];
        this.tileImage[1] = new ImageIcon("held.gif").getImage();
        this.tileImage[0] = new ImageIcon("held.gif").getImage();
        this.tileWidth = this.tileImage[0].getWidth(null);
        this.tileHeight = this.tileImage[0].getHeight(null);
    }

    public int getMapWidthInTiles(){
        return this.mapWidth;
    }

    public int getMapHeightInTiles(){
        return this.mapHeight;
    }

    public int getTileWidth(){
        return this.tileWidth;
    }

    public int getTileHeight(){
        return this.tileHeight;
    }

    public Image getTileImage(int index){
        return this.tileImage[map[index]];
    }
}
