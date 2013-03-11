/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.connection;

import java.net.Socket;

/**
 *
 * @author kirtash
 */
public class ProtocolGame {
     public static void parse(Connection cliente,String s){
        // We assume that info was correctly sent.
        String[] splitted;
        splitted = s.split("[|]");
        String func = splitted[0];
        if(func.equals("0")) { //La función '0' está definida para cualquier tipo de protocolo.
                                //Es la función que permite cerrar la conexión.
             return;
        }
        
        if(splitted.length==1) {
            getInfo(cliente,func,null);
        }else{
            splitted = splitted[1].split("[,]");
            getInfo(cliente,func,splitted);
        }
    }
     
     
    public static void getInfo(Connection cliente, String func, String[] args) {
            switch (func) {
                case "0x02": //Case1 Join Game queue
                    cliente.startGame();
                    break;
                case "0x03": //MoveTo
                    cliente.moveTo(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
                    //connection.pushToClient("exit");
                    //Hacer eso, para que el read que espera que le
                default:
                    //throw new AssertionError();
            }
    }
}
