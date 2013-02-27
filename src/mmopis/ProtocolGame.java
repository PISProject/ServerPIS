/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

/**
 *
 * @author zenbook
 */
public class ProtocolGame{
    // Funciones de estado 0x00 - 0x0F
    private static final String READY= "0x01";
    
    // Funciones de interaccion del jugador 0x10-0x5F
    private static final String MOVE_TO= "0x10";
            
    private Connection connection;
    public ProtocolGame(Connection con) {
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
    
    
    public void getInfo(String command, String[] args){
        switch (command) {
            case READY:
                imReady();
            case MOVE_TO: //Caso goTo(float x, float y), recibe 2 argumentos de tipo float.
                        //Example: 1|4.67,356.4
                goTo(Float.parseFloat(args[0]),Float.parseFloat(args[1]));
                break;
            default:
                //throw new AssertionError();
        }
    }
    
    private void goTo(float posX, float posY) { //no hace falta pasar una idPlayer, ya tenemos una instancia de la conexión del cliente.
        
        
    }

    private void imReady() {
        connection.imReady();
    }

    private void close() {
        connection.close();
    }
}
  
