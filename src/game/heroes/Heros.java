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

package game.heroes;

import java.util.HashMap;

public class Heros{
    public static HashMap<String,HeroModel> HEROS_LIST;
    
    public Heros(){
        HEROS_LIST = new HashMap<>();
    }
    
    public static HeroModel getMonsterModel(String name){
        return HEROS_LIST.get(name);
    }
}