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
    public float[] center;
    public int range;
    public Actor caster;
    public Attack(Actor a, int a_type){
//      Calculamos el centro del ataque
        center = new float[2];
        center[0] =(a.posX+(float) (Math.sin(Math.toRadians(a.lookangle))*2));
        center[1] =(a.posY+(float) (Math.cos(Math.toRadians(a.lookangle))*2));
//        center[0] = a.posX;
//        center[1] = a.posY;
        
//      Rango provisional
        range = 3;
        type = a_type; // No es util de momento
        caster = a;
        
    }
}
