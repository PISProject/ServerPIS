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
                case "1": //Case1 Join Game queue
                    connection.joinQueue();
                    break;
                case "2": //Exit Queue
                    connection.exitQueue();
                    //connection.pushToClient("exit");
                    //Hacer eso, para que el read que espera que le
                default:
                    //throw new AssertionError();
            }
    }
    
}
