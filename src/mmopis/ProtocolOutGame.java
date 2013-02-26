/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mmopis;

/**
 *
 * @author kirtash
 */
public class ProtocolOutGame extends Protocol{
    public ProtocolOutGame(Connection con){
        super(con);
    }

    @Override
    public void getInfo(String func, String[] args) {
            switch (func) {
                case "1": //Case1 Join Game queue
                    connection.joinQueue();
                    break;
                case "2": //Exit Queue
                    connection.exitQueue();
                    //connection.pushToClient("exit");
                    //Hacer eso, para que el read que espera que le
                default:
                    //throw new AssertionError();
            }
    }
}
