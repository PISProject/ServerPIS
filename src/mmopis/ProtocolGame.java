/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author zenbook
 */
public class ProtocolGame extends Protocol{
    
    public ProtocolGame(Connection con) {
        super(con);
    }
    
    @Override
    public void getInfo(String command, String[] args){
        switch (command) {
            case "1": //Caso goTo(float x, float y), recibe 2 argumentos de tipo float.
                        //Example: 1|4.67,356.4
                goTo(args[0],Float.parseFloat(args[1]),Float.parseFloat(args[2]));
                break;
            default:
                //throw new AssertionError();
        }
    }
    
    private void goTo(String idPlayer, float posX, float posY) {
        
        
    }
}
  