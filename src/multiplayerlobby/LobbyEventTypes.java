/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 * @author faust
 */
public enum LobbyEventTypes {
    USER_JOIN,
    USER_LEAVE,
    USER_MOVED,
    USER_MESSAGE,
    SYSTEM_MESSAGE,
    USER_PICKED_MAP,
    USER_CHANGED_TEAM;
    
    /**
     * The attributes for each kind of event
     * @return a list of strings, name of every attribute
     */
    public ArrayList<String> attributes (){
        ArrayList<String> attrs = new ArrayList<>();
        switch(this){
            case USER_JOIN :
            case USER_MOVED :
                attrs.add("slot");
                attrs.add("username");
                break;
            case USER_LEAVE :
                attrs.add("username");
                break;
            case USER_MESSAGE:
                attrs.add("username");
                attrs.add("message");
                break;
            case SYSTEM_MESSAGE:
                attrs.add("message");
                break;
            case USER_PICKED_MAP:
                attrs.add("username");
                attrs.add("mapid");
                break;
            case USER_CHANGED_TEAM:
                attrs.add("username");
                attrs.add("color");
                break;
        }
        return attrs;
    }
    
    public HashMap<String,Object> hashMap(){
        HashMap<String,Object> map = new HashMap<>();
        for(String attr : attributes().toArray(String[]::new)){
            map.put(attr, null);
        }
        return map;
    }
}
