package com.fofoisoninternet.multiplayerlobby;

import com.fofoisoninternet.irc.PrivMsg;
import java.util.HashMap;
import java.util.function.Function;
/**
 * Naming convention: [SUBJECT]_[ACTION]
 *   String[] - Data found in the message (message content, username, team, ...)
 *   Function - Check if the given message matches the EventType (Is it a message sent by a user or did someone leave the lobby?)
 *   Function - Extracts the data from the message to a hashMap
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
    USER_MOVE(
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
    MAP_CHANGE(
            new String[]{"mapid"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           (m.message.contains("Beatmap changed to:") || m.message.contains("Changed beatmap to")),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                if (msg.contains("Beatmap changed to:")){
                    map.put("mapid",msg.substring(msg.lastIndexOf('/')+1,msg.lastIndexOf(')')));
                } else{
                    map.put("mapid",msg.substring(msg.lastIndexOf('/')+1,msg.indexOf(" ", msg.lastIndexOf('/')+1)));
                }
                return map;
            }
    ),
    USER_CHANGE_TEAM(
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
                           m.message.contains(" became the host."),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                map.put("username", m.message.split(" became the host.")[0]);
                return map;
            }
    ),
    HOST_CLEAR(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Cleared match host"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
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
    GAME_ABORT(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Aborted the match"),
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
    ALL_USERS_READY(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("All players are ready"),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                return map;
            }
    ),
    TIMER_INFO(
            new String[]{"time"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Countdown ends in "),
            (PrivMsg m)->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                int seconds = 0;
                // Minutes
                if (msg.contains("minute")){
                    seconds = 60 * Integer.parseInt(msg.split("in ")[1].split(" min")[0]); 
                }
                // Seconds
                if (msg.contains("second")){
                    if (seconds == 0){
                        seconds = Integer.parseInt(msg.split("in ")[1].split(" sec")[0]);
                    }else{
                        seconds += Integer.parseInt(msg.split("and ")[1].split(" sec")[0]);
                    }
                }
                // Result
                map.put("time","" + seconds);
                return map;
            }
    ),
    TIMER_ABORT(
            new String[0],
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("Countdown aborted"),
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
    ),
    USER_ROLL(
            new String[]{"username","points"},
            (PrivMsg m) -> SYSTEM_MESSAGE.isMessageOfType(m) && 
                           m.message.contains("rolls") && 
                           m.message.contains("point(s)"),
            (PrivMsg m) ->{
                HashMap<String,String> map = new HashMap<>();
                String msg = m.message;
                map.put("username",msg.split(" rolls ")[0]);
                map.put("points",msg.split(" rolls ")[1].split(" point")[0]);
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
