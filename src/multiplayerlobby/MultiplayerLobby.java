package multiplayerlobby;

import irc.Flag;
import irc.Irc;
import irc.PrivMsg;
import java.util.*;
import utils.EnvLoader;
/**
 *
 * @author fofoisoninternet
 */
public class MultiplayerLobby {
    private Irc irc;
    private String roomName;
    private String roomId; 
    private boolean open = false;
    private GameMode gameMode;
    private TeamMode teamMode;
    private ScoreMode scoreMode;
    private int size;
    private final long CLOSE_CODE;
    public final static int DEFAULT_MAP = 4532271;
    
    public MultiplayerLobby(String name, GameMode gameMode, TeamMode teamMode, ScoreMode scoreMode){
        this.gameMode = gameMode;
        this.teamMode = teamMode;
        this.scoreMode = scoreMode;
        this.size = 16;
        // Lobby close code
        CLOSE_CODE = new Random().nextLong();
        System.out.println(CLOSE_CODE);
        // Instantiate an irc on bancho
        String channel = "#osu";
        irc = new Irc(
                EnvLoader.get("OSU_IRC_SERVER"),
                Integer.parseInt(EnvLoader.get("OSU_IRC_PORT")),
                channel,
                EnvLoader.get("OSU_IRC_USERNAME"),
                EnvLoader.get("OSU_IRC_PASSWORD")
        );
        //irc.run();
        // opens a new room
        roomName = name;
        irc.privateMessage("BanchoBot","!mp make " + roomName);
        // get the answer
        PrivMsg response = irc.waitPrivateMessage("BanchoBot");
        // join the new channel
        String message = response.message.split("https://osu.ppy.sh/mp/")[1];
        String mp_id = message.split(" ")[0];
        roomId = "#mp_"+mp_id;
        irc.join(roomId);
        // open the room
        say("!mp password");
        open = true;
        // Leave the default channel
        irc.leave(channel);
        // Adds a flag on messages coming from the lobby
        irc.addFlag(new Flag(null,"PRIVMSG",new String[]{roomId}));
        map();
    }
    public MultiplayerLobby (String name){
        this(name,GameMode.STD,TeamMode.HEAD2HEAD,ScoreMode.SCORE);
    }
    
    /**
     * Send a private message to the lobby
     * @param message Text line to send
     */
    public void say(String message){
        irc.privateMessage(roomId, message);
    }
    
    public boolean isOpen(){
        return open;
    }
    
    public void setName(String roomName){
        this.roomName = roomName;
    }
    
    // MP COMMANDS
    
    /**
     * Updates the room name.
     * @param title 
     */
    public void name(String title){roomName=title;say("!mp name " + title);}
    public void name(){name(roomName);}
    public void invite(String userName){say("!mp invite " + userName);}
    public void lock(){say("!mp lock");}
    public void unlock(){say("!mp unlock");}
    public void size(int size){this.size=size;say("!mp size " + size);}
    public void set(TeamMode teamMode, ScoreMode scoreMode,int size){
        say("!mp set " + teamMode.toInt() + " "
                        + scoreMode.toInt() + " "
                        + size);
    }
    public void set(){set(teamMode,scoreMode,size);}
    public void move(String userName,int slot){say("!mp move " + userName + " " + slot);}
    public void host(String userName){say("!mp host " + userName);}
    public void clearHost(){say("!mp clearhost");}
    public Object settings(){return null;} // TBD
    public void start(int time){say("!mp start " + time);}
    public void start(){say("!mp start");}
    public void abort(){say("!mp abort");}
    public void team(String userName,TeamColor color){say("!mp team " + userName + " " + color.toString());}
    public void map(int mapid,GameMode mode){say("!mp map " + mapid + " " + mode.toInt());}
    public void map(int mapid){map(mapid,gameMode);}
    public void map(Map map, GameMode mode){map(map.getID(),mode);}
    public void map(Map map){map(map.getID());}
    public void map(){map(DEFAULT_MAP);}
    public void mods(Mod[] mods){}
    public void mods(Mod mod){}
    public void timer(int time){say("!mp timer " + time);}
    public void abortTimer(){say("!mp aborttimer ");}
    public void kick(String userName){say("!mp kick " + userName);}
    public void ban(String userName){say("!mp ban " + userName);}
    public void password(String password){say("!mp password " + password);}
    public void removePassord(){say("!mp password");}
    public void addRef(String[] usersName){}
    public void removeRef(String[] usersName){}
    public void listRefs(String[] usersName){}
    
    /**
     * Close the lobby.
     */
    public void close(){
        say("!mp close");
        open = false;
        irc.disconect();
    }
    
    /**
     * Ping the irc client to not get disconnected
     */
    public void ping(){
        irc.ping();
    }
    
    // END OF MP COMMANDS
    
    /**
     * Returns the last interesting event that hapenned in the room.
     * @return 
     */
    public LobbyEvent nextEvent(){
        var data = irc.NextData();
        LobbyEvent event = null;
        if(data != null && data.command.equals("PRIVMSG")){
            var message = new PrivMsg(data);
            event = new LobbyEvent(message);
        }
        return event;
    }
    
    public void run(){
        while(isOpen()){
            ping();
            var data = irc.NextData();
            if(data != null){
                if(data.command.equals("PRIVMSG")){
                    PrivMsg message = new PrivMsg(data);
                    System.out.println(message.toString());
                    LobbyEvent event = new LobbyEvent(message);
                    System.out.println(event.toString());
                    if(message.message.contains("!quit "+CLOSE_CODE)){
                        close();
                    }
                }else{
                    System.out.println(data.toString());
                }
            }
        }
    }
    
}
