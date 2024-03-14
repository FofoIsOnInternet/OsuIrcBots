/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

import irc.PrivMsg;
import java.util.HashMap;
import java.util.function.Function;
/**
 *
 * @author faust
 */
public enum LobbyEventType {
    SYSTEM_MESSAGE(
            new String[]{"message"},
            (PrivMsg m) -> m.sender.equals("BanchoBot")
    ),
    USER_JOIN(
            new String[]{"username","slot"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" joined in slot ")
    ),
    USER_LEAVE(
            new String[]{"username"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" left the game.")
    ),
    USER_MOVED(
            new String[]{"username","slot"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" moved to slot ")
    ),
    USER_MESSAGE(
            new String[]{"username","message"},
            (PrivMsg m) -> !SYSTEM_MESSAGE.isMessageOfType(m)
    ),
    PICKED_MAP(
            new String[]{"mapid"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Beatmap changed to:")
    ),
    USER_CHANGED_TEAM(
            new String[]{"username","color"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" changed to ")
    ),
    HOST_CHANGE(
            new String[]{"username"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Changed match host to ") ||
                           m.message.contains("Cleared match host")
    );
    
    private final String[] attrs;
    private final Function<PrivMsg,Boolean> isMessageOfType;
    
    private LobbyEventType (String[] attributes,Function<PrivMsg,Boolean> typeFunction){
        attrs = attributes;
        isMessageOfType = typeFunction;
    }
    
    /**
     * The attributes for each kind of event
     * @return a list of strings, name of every attribute
     */
    public String[] attributes (){
        return attrs;
    }
    
    /**
     * Gives an empty hash map with no values where keys
     * are the attributes of the given LobbyEventType
     * @return 
     */
    public HashMap<String,Object> hashMap(){
        HashMap<String,Object> map = new HashMap<>();
        for(String attr : attributes()){
            map.put(attr, null);
        }
        return map;
    }
    
    /**
     * Reads the content of a private message from the bancho irc server
     * and indicates what kind of event it corresponds to.
     * If it doesn't match any particular event, it'll just either be
     * SYSTEM_MESSAGE if it's from BanchoBot or USER_MESSAGE if it's
     * from any user.
     * @param m A PrivMsg object with everything to know about the message
     * @return A LobbyEventType object 
     */
    public static LobbyEventType identifyEventType (PrivMsg m){
        LobbyEventType type = null;
        int i = 0;
        LobbyEventType t;
        while( i < values().length && (type == null || type == SYSTEM_MESSAGE)){
            t = values()[i];
            if(t.isMessageOfType(m)){
                type = t;
            }
            i++;
        }
        return type;
    }
    
    /**
     * @param m A message to check
     * @return true iff the message is of the given type
     */
    public boolean isMessageOfType(PrivMsg m ){
        return isMessageOfType.apply(m);
    }
}
