/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayerlobby;

import java.util.HashMap;

/**
 *
 * @author faust
 */
public class LobbyEvent {
    private LobbyEventTypes type;
    private HashMap<String,Object> attributes;
    
    public LobbyEvent(LobbyEventTypes eventType){
        this.type = eventType;
        this.attributes = type.hashMap();
    }
    
    public Object getValue(String attr){
        return attributes.get(attr);
    }
    public void setValue(String attr,Object value){
        attributes.replace(attr, value);
    }
}
