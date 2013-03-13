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
                    quitQueue(cliente);
                    break;

                case "2": // JOIN QUEUE
                    joinQueue(cliente);
                    break;
                default:
                    //throw new AssertionError();
                    break;
            }
    }
    
    //OUT FUNCTIONS
    
    void notifyGameStarting(Connection c){
        if (c.state == Connection.ConnectionState.WAITING_QUEUE){
            c.pushToClient(true);
            //Cambio de estado de la conexion.
            c.state = Connection.ConnectionState.IN_GAME;
        }
        //
        System.out.println("MISSMATCH: El cliente no esta esperando esto");
        //No deberia de llegar aqui
    }
    
    //IN FUNCTIONS

    private void quitQueue(Connection cliente) {
        if (cliente.state == Connection.ConnectionState.OUT_GAME){
            if (!server.quitQueue(cliente)){
                cliente.pushToClient(false);
            }
            cliente.pushToClient(true);
        }
        System.out.println("MISSMATCH: El cliente no esta esperando esto");
        //No deberia de llegar aqui
    }

    private void joinQueue(Connection cliente) {
        if (cliente.state == Connection.ConnectionState.OUT_GAME){
            server.joinQueue(cliente);
            cliente.pushToClient(true);
        }
        //
        System.out.println("MISSMATCH: El cliente no esta esperando esto");
        //No deberia de llegar aqui
    }
    
}
