package clientepis;

/**
 *
 * @author zenbook
 */
public class Cliente {
    
    private Protocol protocol;
    private Game game;
    
    public Cliente() { //En android: public Cliente(Activity a);
                        //para poder utilizar: Toast.makeText(a.getBaseContext(),"No se ha podido empezar la partida", Toast.LENGTH_SHORT); 
        protocol = new Protocol();
    }
    /**
     * Mètode que se queda esperando a que le responda el servidor.
     * Si devuelve 'true' es que puede empezar la partida.
     * Si devuelve 'false' no podrá empezar la partida.
     * 
     * @return boolean
     */
    
    public Game startGame() {
        return protocol.joinQueue();
    }
    
    public void stopWaiting() {
        protocol.quitQueue();
    }
    
    public void readyToStart() {
        boolean res;
        res = protocol.readyToStart();
        if(res){
            System.out.println("Ahora si que empieza la partida");
            ThreadGame threadGame = new ThreadGame(protocol, game);
            threadGame.start();
        }
    }
    
    public void moveTo(int angulo){
        protocol.moveTo(angulo);
    }
    
    public void close() {
        protocol.close();
    }
    
    
    public static void main(String[] args) {
        /*TODO here*/
        
    }
}
