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
}
