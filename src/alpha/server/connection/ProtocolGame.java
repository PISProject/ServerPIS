/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alpha.server.connection;

import alpha.server.scenario.GameThread;

/**
 *
 * @author kirtash
 */
public class ProtocolGame {
    GameThread game;
    
    public ProtocolGame(GameThread aThis) {
        game = aThis;
    }
     public void parse(int uid,String s){
        // We assume that info was correctly sent.
        String[] splitted;
        splitted = s.split("[|]");
        String func = splitted[0];
        if(func.equals("0")) { //La función '0' está definida para cualquier tipo de protocolo.
                                //Es la función que permite cerrar la conexión.
             return;
        }
        
        if(splitted.length==1) {
            getInfo(uid,func,null);
        }else{
            splitted = splitted[1].split("[,]");
            getInfo(uid,func,splitted);
        }
    }
     
     
    public void getInfo(int uid, String func, String[] args) {
            switch (func) {
                case "1": //MoveTo
                    moveTo(uid,Integer.parseInt(args[0]),Integer.parseInt(args[1]));
                    //connection.pushToClient("exit");
                    //Hacer eso, para que el read que espera que le
                case "2":
                    attack(uid);
                default:
                    //throw new AssertionError();
            }
    }
    void moveTo(int uid,int x, int y) {
        game.moveTo(uid,x,y);
    }

    void attack(int uid){
        game.attack(uid);
    }
    
    public String getMap(){
        return game.map;
    }
    public GameThread.GameState getState(){
        return game.gamestate;
    }
}
