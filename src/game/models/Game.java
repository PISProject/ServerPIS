/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.models;

import game.monsters.Monster;

/**
 * La clase game es la superclase que contiene todos los tipos de partidas.
 * 
 * En un principio los parametros que asume son tiempo estimado de partida, 
 * @author kirtash
 */
public class Game {
    
    public int estimatedTime;
    public int scenario;
    public String [] monsters;
    public String name;
    public int id;
    public int numplayers;
    public int n_hordes;
    

    public Game() {
    }
    
    
    
    
}
