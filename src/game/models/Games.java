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

package game.models;

import java.util.HashMap;

/**
 *
 * @author PabloMartinez
 */
public class Games {
    public static HashMap<Integer,Game> GAME_LIST;
    
    public Games(){
        GAME_LIST = new HashMap<>();
    }
    
    public Game getGame(int i){
        return GAME_LIST.get(i);
    }
}
