package com.fofoisoninternet.multiplayerlobby;

import com.fofoisoninternet.irc.PrivMsg;
import java.util.HashMap;

/**
 *
 * @author fofoisoninternet
 */
public class LobbyEvent {
    private final LobbyEventType type;
    private final HashMap<String,String> attributes;
    
    public LobbyEvent(LobbyEventType eventType){
        this.type = eventType;
        this.attributes = type.hashMap();
    }
    public LobbyEvent(PrivMsg m){
        this(LobbyEventType.identifyEventType(m));
        this.extractAttributes(m);
    }
    
    
    /**
     * gets the value of an attribute
     * @param attr LobbyEventType attribute
     * @return any object
     */
    public String getValue(String attr){
        return attributes.get(attr);
    }
    /**
     * Gives a new value to the given LobbyEventType attribute
     * @param attr LobbyEventType attribute
     * @param value 
     */
    public void setValue(String attr,String value){
        attributes.replace(attr, value);
    }
    
    /**
     * @return the type of the event
     */
    public LobbyEventType getType(){
        return type;
    }
    
    /**
     * Extract all the informations from a message
     * @param m private message
     */
    private void extractAttributes(PrivMsg m){
        var attrs = type.extractAttributes(m);
        for(String k : type.attributes()){
            attributes.put(k,attrs.get(k));
        }
    }
    
    /**
     * Indicates if the given LobbyEventType is the same as the event.
     * @param eventType
     * @return true iff the event is of the given type
     */
    public boolean isOfType(LobbyEventType eventType){
        return type == eventType;
    }

    @Override
    public String toString(){
        String s = type.name() + " : ";
        for (String k : type.attributes()){
            s += attributes.get(k) + "|";
        }
        return s;
    }
}
