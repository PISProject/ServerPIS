/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game.monsters;

import java.util.HashMap;

/**
 *
 * @author kirtash
 */
public class Monsters{
    HashMap<String,String[]> monster_loader = new HashMap<>();
    public static HashMap<String,MonsterModel> MONSTER_LIST;
    
    public Monsters(){
        MONSTER_LIST = new HashMap<>();
    }
    
    public static MonsterModel getMonsterModel(String name){
        return MONSTER_LIST.get(name);
    }
}
