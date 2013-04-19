/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.models;

import monsters.Monster;

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
    
    
    public Game(String [] monsters, int scenario, int estimatedTime){
        this.monsters = monsters;
        this.scenario = scenario;
        this.estimatedTime = estimatedTime;
    }
    
    
    
    
}
