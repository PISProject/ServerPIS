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

package game;

/**
 *
 * @author PabloMartinez
 */
public class Attack {
    public int type;
    public int range;
    public int caster;
    public Attack(int caster, int type, int range){
       this.caster = caster;
        this.type = type;
        this.range = range;
    }
}
