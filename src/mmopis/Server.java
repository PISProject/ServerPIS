/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author zenbook
 */
public class Server {
    public Server(){
        ConnectionListener comInterface = new ConnectionListener ();
        
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.print("Starting server...");
        Server server = new Server();
        System.out.println("[Done]");
        System.out.println("Listening connections:");
    }
}
