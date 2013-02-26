/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
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
        
        if(splitted.length==1) {
            getInfo(func,null);
            return;
        }else{
            splitted = splitted[1].split("[,]");
            getInfo(func,splitted);
        }
    }
    
    public abstract void getInfo(String func, String[] args);
    
}
