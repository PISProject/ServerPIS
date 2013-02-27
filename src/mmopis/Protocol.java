/*
 * This class contains the protocol of communication betwen the Client and the Server when it's logged.
 */
package mmopis;

/**
 * 
 * 
 * @author zenbook
 */
public class Protocol {
    //////////////////
    // INPUT
    //////////////////
    // Funciones de estado 0x00 - 0x0F
    public static final String CLOSE = "0x00";
    public static final String READY_TO_START_GAME = "0x01";
    public static final String JOIN_QUEUE = "0x02";
    public static final String QUIT_QUEUE = "0x03";
    
    
    // Funciones de interaccion del jugador 0x10-0x5F
    public static final String INGAME_MOVE_TO = "0x10";

    //////////////////
    //OUTPUT
    //////////////////
    // Notificaciones de estado 
    public static final String NOTIFY_GAME_STARTING = "0x60";
    
    
    private Connection connection;
    
    public Protocol(Connection con) {
        this.connection = con;
    }
    
    public void parse(String s){
        // We assume that info was correctly sent.
        String[] splitted;
        splitted = s.split("[|]");
        String func = splitted[0];
        if(func.equals("0")) { //La función '0' está definida para cualquier tipo de protocolo.
                                //Es la función que permite cerrar la conexión.
             close();
             return;
        }
        
        if(splitted.length==1) {
            getInfo(func,null);
        }else{
            splitted = splitted[1].split("[,]");
            getInfo(func,splitted);
        }
    }
    
    public void close() {
        connection.close();
    }
    
    public void getInfo(String func, String[] args) {
            switch (func) {
                case JOIN_QUEUE: //Case1 Join Game queue
                    connection.joinQueue();
                    break;
                case QUIT_QUEUE: //Exit Queue
                    connection.quitQueue();
                    //connection.pushToClient("exit");
                    //Hacer eso, para que el read que espera que le
                default:
                    //throw new AssertionError();
            }
    }
    
}
