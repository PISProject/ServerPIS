/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.models;

/**
 *
 * @author kirtash
 */
public class GameTest extends Game{
    private static final String [] monsters = {"MonsterTest", "MonsterTest", "MonsterTest"};
    private static final int estimatedTime = 100;
    private static final int scenario = 2;
    
    public GameTest(){
        super(monsters,estimatedTime,scenario);
    }
    
}
