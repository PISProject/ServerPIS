/*******************************************************************************
 * Machango Fight, the Massive Multiplayer Online.
 * Server Application
 * 
 * Curso 2012-2013
 * 
 * Este software ha sido desarrollado integramente para la asignatura 'Projecte
 * Integrat de Software' en la Universidad de Barcelona por los estudiantes
 * Pablo Martínez Martínez, Albert Folch, Xavi Moreno y Aaron Negrín.
 * 
 ******************************************************************************/

package game.monsters;

import java.util.HashMap;

/**
 *
 * @author PabloMartinez
 */
public class Monsters{
    public static HashMap<String,MonsterModel> MONSTER_LIST;
    
    public Monsters(){
        MONSTER_LIST = new HashMap<>();
    }
    
    public static MonsterModel getMonsterModel(String name){
        return MONSTER_LIST.get(name);
    }
}
