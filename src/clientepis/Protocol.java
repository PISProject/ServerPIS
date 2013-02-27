/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;


/**
 * 
 * 
 * @author zenbook
 */
public class Protocol {
    
    //////////////////
    // OUTPUT
    //////////////////
    // Funciones de estado 0x00 - 0x0F
    public static final String CLOSE = "0x00";
    public static final String READY_TO_START_GAME = "0x01";
    public static final String JOIN_QUEUE = "0x02";
    public static final String QUIT_QUEUE = "0x03";
    
    
    // Funciones de interaccion del jugador 0x10-0x5F
    public static final String INGAME_MOVE_TO = "0x10";

    //////////////////
    //INPUT
    //////////////////
    // Notificaciones de estado 
    public static final String NOTIFY_GAME_STARTING = "0x60";
    public static final String APPROVED = "0x04";
    public static final String DENIED = "0x05";    
}
