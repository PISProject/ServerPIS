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
public abstract class Protocol {
    
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
    
    protected Cliente cliente;
    
    public Protocol(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void parse(String s){
        // We assume that info was correctly sent.
        String[] splitted;
        splitted = s.split("[|]");
        String func = splitted[0];
        
        if(splitted.length==1) {
            getInfo(func,null);
        }else{
            splitted = splitted[1].split("[,]");
            getInfo(func,splitted);
        }
    }
    
    public abstract void getInfo(String func, String[] args);
    
}
