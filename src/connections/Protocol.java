/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connections;

/**
 *
 * @author kirtash
 */
public class Protocol {
    public static final String DISCONNECT = "0";
    public static final String JOIN_QUEUE = "1";
    public static final String QUIT_QUEUE = "2";
    public static final String GAME_FOUND = "3";
    public static final String READY_TO_START = "4";
    
    private Connection client;

    
    public Protocol(Connection client) {
        this.client = client;
    }

    ////////////////////////////////////////////////////////////////////////////
    // INPUT FUNCTIONS
    void parse(String entrada) {
        // We assume that info was correctly sent.
        String[] splitted;
        splitted = entrada.split("[|]");
        String func = splitted[0];
        if(func.equals("0")) { //La función '0' está definida para cualquier tipo de protocolo.
                                //Es la función que permite cerrar la conexión.
             return;
        }
        
        if(splitted.length==1) {
            ejecutaFuncion(func,null);
        }else{
            splitted = splitted[1].split("[,]");
            ejecutaFuncion(func,splitted);
        }
    }
    void ejecutaFuncion(String func, String [] args){
        if (client.state == Connection.ConnectionState.OUT_GAME){
            switch (func) {
                case JOIN_QUEUE:
                        client.joinQueue();
                    break;
                case QUIT_QUEUE:
                        client.quitQueue();
            }
        }
        else if (client.state == Connection.ConnectionState.LOADING)
            switch (func) {
                case READY_TO_START:
                    client.notifyGameReady();
            }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
    ///OUTPUT FUNCTIONS
    ////////////////////////////////////////////////////////////////////////////
    
}
