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
 * @author PabloMartinez
 */
public class Game {
    
    public int radius;
    public int estimatedTime;
    public int scenario;
    public String [] monsters;
    public String name;
    public int game_type;
    public int numplayers;
    public int n_hordes;
    public ArrayList<Horde> hordes;

    public Game() {
        hordes = new ArrayList<>();
    }
    
    
    
    
}
