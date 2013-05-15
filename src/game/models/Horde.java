package game.models;

import java.util.ArrayList;

public class Horde {
    public ArrayList<String> list;
    public int time;
    public Horde(){
        list = new ArrayList<>();
    }
    public boolean hasNext(){
        return list.size()>0;
    }
    
    public String getNextMonster(){
        return list.remove(0);
        
    }
}
