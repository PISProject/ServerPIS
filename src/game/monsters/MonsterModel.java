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

/**
 *
 * @author PabloMartinez
 */
public class MonsterModel {
    public String name;
    public int hp;
    public int attack_damage;
    public double speed;
    public int model;
    public double changedir_prob;
    
    
    // PARAMETROS DE COMPORTAMIENTO

    public int target; //uid del target
    public double rand_movedir;
    public double stchange_rate; //Cada segundo
    
    
    public MonsterModel() {
        //---------
    }
    
    
  
}

