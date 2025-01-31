package multiplayerlobby;

import irc.PrivMsg;
import java.util.HashMap;
import java.util.function.Function;
/**
 *
 * @author fofoisoninternet
 */
public enum LobbyEventType {
    SYSTEM_MESSAGE(
            new String[]{"message"},
            (PrivMsg m) -> m.sender.equals("BanchoBot"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                map.put("message", m.message);
                return map;
            }
    ),
    USER_JOIN(
            new String[]{"username","slot"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" joined in slot "),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                map.put("username",msg.substring(0, msg.indexOf(" joined in slot ")));
                map.put("slot",msg.split(" joined in slot ")[1].replace(".", ""));
                return map;
            }
    ),
    USER_LEAVE(
            new String[]{"username"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" left the game."),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                map.put("username",msg.split(" left the game.")[0]);
                return map;
            }
    ),
    USER_MOVED(
            new String[]{"username","slot"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" moved to slot "),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                map.put("username",msg.substring(0, msg.indexOf(" moved to slot ")));
                map.put("slot",msg.split(" moved to slot ")[1].replace(".", ""));
                return map;
            }
    ),
    USER_MESSAGE(
            new String[]{"username","message"},
            (PrivMsg m) -> !SYSTEM_MESSAGE.isMessageOfType(m),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                map.put("username",m.sender);
                map.put("message", m.message);
                return map;
            }
    ),
    PICKED_MAP(
            new String[]{"mapid"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Beatmap changed to:"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                map.put("mapid",msg.substring(msg.lastIndexOf('/')+1,msg.lastIndexOf(')')));
                return map;
            }
    ),
    USER_CHANGED_TEAM(
            new String[]{"username","color"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" changed to "),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String[] info = m.message.split(" changed to ");
                map.put("username",info[0]);
                map.put("color",info[1]);
                return map;
            }
    ),
    HOST_CHANGE(
            new String[]{"username"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Changed match host to ") ||
                           m.message.contains("Cleared match host"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                if(m.message.contains("Cleared match host")){
                    map.put("username", null);
                }else{
                    map.put("username", m.message.split("Changed match host to ")[1]);
                }
                return map;
            }
    ),
    GAME_START(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("The match has started"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                return map;
            }
    ),
    GAME_END(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("The match has finished!"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                return map;
            }
    ),
    ALL_READY(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("All players are ready"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                return map;
            }
    ),
    TIMER_END(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Countdown finished"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                return map;
            }
    ),
    USER_SCORE(
            new String[]{"username","score","status"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains(" finished playing "),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                map.put("username",msg.split(" finished playing ")[0]);
                String result = msg.substring(msg.lastIndexOf('('), msg.lastIndexOf(')'));
                map.put("score",result.split(",")[0].split("Score: ")[1]);
                map.put("status",result.split(",")[1].strip());
                return map;
            }
    ),
    MATCH_CLOSE(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Closed the match"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                return map;
            }
    );
    
    private final String[] attrs;
    private final Function<PrivMsg,Boolean> messageTypeChecker;
    private final Function<PrivMsg,HashMap<String,String>> attributeExtractor;
    
    private LobbyEventType (String[] attributes,Function<PrivMsg,Boolean> messageTypeChecker,
                            Function<PrivMsg,HashMap<String,String>> attributeExtractor){
        this.attrs = attributes;
        this.messageTypeChecker = messageTypeChecker;
        this.attributeExtractor = attributeExtractor;
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
    public HashMap<String,String> hashMap(){
        HashMap<String,String> map = new HashMap<>();
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
        return messageTypeChecker.apply(m);
    }
    
    public HashMap<String,String> extractAttributes(PrivMsg m ){
        return attributeExtractor.apply(m);
    }
}
