/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.connection;

import alpha.server.main.Server;

/**
 *
 * @author kirtash
 */
public class Protocol {
    private Server server;
    
    public Protocol(Server server){
        this.server = server;
    }
     public void parse(Connection cliente,String s){
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
     
     
    public void getInfo(Connection cliente, String func, String[] args) {
            switch (func) {
                case "1": // QUIT QUEUE
                    if (!server.quitQueue(cliente)){
                        cliente.pushToClient(false);
                    }
                    break;
                case "2": // JOIN QUEUE
                    server.joinQueue(cliente);
                default:
                    //throw new AssertionError();
            }
    }
}
