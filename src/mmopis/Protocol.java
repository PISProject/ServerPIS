/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 * 
 * 
 * @author zenbook
 */
public abstract class Protocol {
    protected Connection connection;
    
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
    
    public abstract void getInfo(String func, String[] args);
    
}
