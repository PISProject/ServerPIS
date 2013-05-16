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

import java.util.ArrayList;

/**
 * 
 * @author PabloMartinez
 */
public class Horde {
    public ArrayList<String> list;
    public int time;
    public Horde(){
        list = new ArrayList<>();
    }
    public boolean hasNext(){
        return list.size()>0;
    }
    
    public String getNextMonster(){
        return list.remove(0);
        
    }
}
