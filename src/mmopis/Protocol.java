/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author kirtash
 */
public class Protocol {
    public Protocol(Connection con){
        
    }
    public void parse(String s){
        // We assume that info was correctly sent.
        String [] splitted;
        splitted = s.split("|");
        String func = splitted[0];
        splitted = splitted[1].split(",");
        getInfo(func,splitted);
    }

    private void getInfo(String func, String[] args) {
            switch (func) {
                case "1": //Case1 Join Game queue
                    
                    break;
                default:
                    throw new AssertionError();
            }
    }
}
