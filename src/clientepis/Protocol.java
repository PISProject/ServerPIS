/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clientepis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 
 * 
 * @author zenbook
 */
public class Protocol {
    
    
    //////////////////
    //IP & PORT
    //////////////////
    public static final String IP = "localhost";
    public static final int PORT = 5050;
    
    
    /*
    //////////////////
    // OUTPUT
    //////////////////
    // Funciones de estado 0x00 - 0x0F
    public static final String CLOSE = "0x00";
    public static final String READY_TO_START_GAME = "0x01";
    public static final String JOIN_QUEUE = "0x02";
    public static final String QUIT_QUEUE = "0x03";
    
    
    // Funciones de interaccion del jugador 0x10-0x5F
    public static final String INGAME_MOVE_TO = "0x10";

    //////////////////
    //INPUT
    //////////////////
    // Notificaciones de estado 
    public static final String NOTIFY_GAME_STARTING = "0x60";
    public static final String APPROVED = "0x04";
    public static final String DENIED = "0x05";    
    */
    
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    
    
    public Protocol() { //En caso de android poner: public Protocol(Activity a);
        try {
            socket = new Socket(IP, PORT);
            
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            
        } catch (UnknownHostException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        } catch (IOException ex) {
            //Toast.makeText(a.getBaseContext(),<message>, Toast.LENGTH_SHORT);
        }
    }

    /**
     * Esta función envía el server que quiere empezar una partida.
     * Una vez se puede empezar la partida, éste crea una instancia de
     * Game, dónde debe cargar las instancias de los Players de la partida.
     * 
     * @return 
     */
    public Game joinQueue() {
        //Enviar al server que quiero empezar
        //TODO here
        
        //Esperar con un read lo que me contestará el server
        //TODO here
        
        //Parsear lo que me envía y meterlo en un objecto Game.
        //TODO here
        
        return null;
    }

    /**
     * 
     * Esta es la única función que puede llamar el cliente mientras espera a conectarse.
     * Ésta envía al servidor que quiere dejar de esperar y lo que le devuelva el servidor
     * será leído desde la función 'joinQueue'
     */
    public void quitQueue() {
        //Enviar al server que quiero empezar
        //TODO
        
        //Lo que me tenga que devolver, lo recibiré en la función 'joinQueue()', porque
        //es la función que está esperando.
    }
    
    
    /**
     * Esta función le comunica al server que ya está preparado para empear la partida.
     * Devolverá un 'true' si puede empezar la partida, en caso contrario, 'false'
     * @return 
     */
    public boolean readyToStart() {
        //Enviar al server que ya está listo para empezar a jugar
        //TODO here
        
        //Esperar con un read
        
        //Entender lo que me ha enviado, si me ha dicho que puedo empezar, devuelvo true
        //sino un 'false'
        
        return false;
    }

    /**
     * Esta función leerá el estado del juego y lo introducirá en el objeto Game.
     * 
     * 
     * @param game 
     */
    public void readMap(Game game) {
        //Leer lo que me dice el server
        //TODO  here
        
        //Parsear lo que me diga y meterlo en el game object.
        //En la clase ProtocoloCliente hay un parser del game, que hice en su momento.
        //Míralo, a ver si puedes reutilizarlo.
        //TODO here
    }

    /**
     * Función que le envía el server el ángulo al cual se moverá.
     * 
     * @param angulo 
     */
    public void moveTo(int angulo) {
        //Preparar para enviar al server (no hace falta enviar la uid)
        //TODO here
        
        //Enviar al server
        //TODO here
    }

    /**
     * Función que se llama cuando se quiere cerrar una conexión de forma segura.
     * 
     */
    
    public void close() {
        try {
            //Enviar al server que cerraré el juego, para que haga los controles pertinentes.
            in.close();
            out.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
