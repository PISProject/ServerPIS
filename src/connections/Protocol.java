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
    public static final String MOVE_TO = "5";
    public static final String LOGIN = "6";
    public static final String REGISTER = "7";
    
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
        if (func.equals("0")) client.disconnect();
        if (client.state == Connection.ConnectionState.NOT_LOGGED){
            switch (func) {
                case LOGIN:
                    client.login(args[0],args[1]);
                    break;
                case REGISTER:
                    client.register(args[0], args[1], args[2]);
            }
        }
        else if (client.state == Connection.ConnectionState.OUT_GAME){
            switch (func) {
                case JOIN_QUEUE:
                    client.joinQueue(Integer.parseInt(args[0]));
                    break;
                case QUIT_QUEUE:
                    client.quitQueue();
                    break;
            }
        }
        else if (client.state == Connection.ConnectionState.LOADING){
            switch (func) {
                case READY_TO_START:
                    client.connectionIsReady();
                    break;
            }
        }
        else if (client.state == Connection.ConnectionState.IN_GAME){
            switch (func) {
                case MOVE_TO:
                    client.moveTo(Integer.parseInt(args[0]));
                    break;
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    
    
    ///OUTPUT FUNCTIONS
    ////////////////////////////////////////////////////////////////////////////
    
}
