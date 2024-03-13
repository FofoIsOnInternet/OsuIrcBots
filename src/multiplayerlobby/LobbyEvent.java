/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayerlobby;

import irc.PrivMsg;
import java.util.HashMap;

/**
 *
 * @author faust
 */
public class LobbyEvent {
    private final LobbyEventType type;
    private final HashMap<String,Object> attributes;
    
    public LobbyEvent(LobbyEventType eventType){
        this.type = eventType;
        this.attributes = type.hashMap();
    }
    
    /**
     * gets the value of an attribute
     * @param attr LobbyEventType attribute
     * @return any object
     */
    public Object getValue(String attr){
        return attributes.get(attr);
    }
    /**
     * Gives a new value to the given LobbyEventType attribute
     * @param attr LobbyEventType attribute
     * @param value 
     */
    public void setValue(String attr,Object value){
        attributes.replace(attr, value);
    }
    
    public static LobbyEvent toLobbyEvent (PrivMsg m){
        LobbyEvent event = new LobbyEvent(LobbyEventType.identifyEventType(m));
        return event;
    }
    
    @Override
    public String toString(){
        return type.name();
    }
}
