/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

import mmopis.*;

/**
 * 
 * 
 * @author zenbook
 */
public abstract class Protocol {
    protected Cliente cliente;
    
    public Protocol(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public void parse(String s){
        // We assume that info was correctly sent.
        String[] splitted;
        splitted = s.split("[|]");
        String func = splitted[0];
        
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
