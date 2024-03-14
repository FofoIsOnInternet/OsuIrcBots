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
    private LobbyEventType type;
    private HashMap<String,Object> attributes;
    
    public LobbyEvent(LobbyEventType eventType){
        this.type = eventType;
        this.attributes = type.hashMap();
    }
    public LobbyEvent(PrivMsg m){
        this(LobbyEventType.identifyEventType(m));
    }
    
    
    public Object getValue(String attr){
        return attributes.get(attr);
    }
    public void setValue(String attr,Object value){
        attributes.replace(attr, value);
    }
    
    @Override
    public String toString(){
        return type.name();
    }
}
