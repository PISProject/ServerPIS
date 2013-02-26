/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author zenbook
 */
public class ProtocolGame {

    public ProtocolGame(Connexion aThis) {
    
    }
    
    public void getInfo(String command, String[] args){
            switch (command) {
                case "1": //Caso goTo(float x, float y), recibe 2 argumentos de tipo float.
                    
                    goTo(args[0],Float.parseFloat(args[1]),Float.parseFloat(args[1]));
                    System.out.println("Hola");
                    System.out.println("Hola");
                    System.out.println("Hola");
                    System.out.println("Hola");
                    System.out.println("Hola");
                    break;
                default:
                    throw new AssertionError();
            }
            
        }
    private void goTo(String idPlayer, float posX, float posY) {
        
    }
}
  