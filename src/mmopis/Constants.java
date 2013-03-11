/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author kirtash
 */
public class Constants {
    // Server
    public static final int PORT = 5050;
    
    //GameThread
    public static final int UPDATETIME = 100;
    
    enum ClientStatus{NOT_LOGGED, WAITING_QUEUE, OUT_GAME, IN_GAME,DISCONNECTED};
    
    
    //Tipos de entes
    public static final int ACTOR = 0;
    public static final int ENEMIGO = 1;
    public static final int HECHIZO = 2;
    
    //Tipos de outfits
    public static final int SORCERER = 0;
    public static final int DRUID = 1;
    public static final int KNIGHT = 2;
    public static final int MONK = 3;
    
    //Tipos de monstruos
    public static final int ENEMY = 4;
}
