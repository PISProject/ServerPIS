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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author PabloMartinez
 */
public class Games {
    public static ConcurrentHashMap<Integer,Game> GAME_LIST;
    
    public Games(){
        GAME_LIST = new ConcurrentHashMap<>();
    }
    
    public Game getGame(int i){
        return GAME_LIST.get(i);
    }

    public static String parseGames() {
        String s = new String();
        for(Map.Entry ent: GAME_LIST.entrySet()){
            Game g = (Game)ent.getValue();
            s+= g.scenario+","+g.numplayers+"*";
        }
        return s;
    }
}
