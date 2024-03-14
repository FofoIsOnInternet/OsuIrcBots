/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayerlobby;

import irc.Flag;
import irc.Irc;
import irc.PrivMsg;
import java.util.*;
/**
 *
 * @author faust
 */
public class MultiplayerLobby {
    private Irc irc;
    private String roomName;
    private String roomId; 
    public boolean open = false;
    private final long CLOSE_CODE;
    
    public MultiplayerLobby(String name){
        // Lobby close code
        CLOSE_CODE = new Random().nextLong();
        System.out.println(CLOSE_CODE);
        // Irc server details
        String server = "irc.ppy.sh";
        int port = 6667;
        String channel = "#osu";
        String nickname = "f_o_f_o";
        String password = "4a45538f";
        // Instantiate an irc on bancho
        irc = new Irc(server,port,channel,nickname,password);
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
        // Run the bot
        run();
        // Close the room
        if (open) {close();}
    }
    
    /**
     * Send a private message to the lobby
     * @param message Text line to send
     */
    private void say(String message){
        irc.privateMessage(roomId, message);
    }
    
    // MP COMMANDS
    
    /**
     * Updates the room name.
     * @param title 
     */
    public void name(String title){say("!mp name " + title);}
    public void name(){name(roomName);}
    public void invite(String userName){say("!mp invite " + userName);}
    public void lock(){say("!mp lock");}
    public void unlock(){say("!mp unlock");}
    public void size(int size){say("!mp size " + size);}
    public void set(TeamMode teamMode, ScoreMode scoreMode,int size){
        say("!mp set " + teamMode.toInt() + " "
                        + scoreMode.toInt() + " "
                        + size);
    }
    public void move(String userName,int slot){say("!mp move " + userName + " " + slot);}
    public void host(String userName){}
    public void clearHost(){}
    public Object settings(){return null;} // TBD
    public void start(int time){}
    public void start(){}
    public void abort(){}
    public void team(String userName,String color){}
    public void map(int mapid){}
    public void mods(mods[] mods){}
    public void timer(int time){}
    public void abortTimer(){}
    public void kick(String userName){}
    public void ban(String userName){}
    public void password(String password){}
    public void removePassord(){}
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
    
    // END OF MP COMMANDS
    
    /**
     * Returns the last interesting event that hapenned in the room.
     * @return 
     */
    public LobbyEvent nextEvent(){
        var data = irc.NextData();
        LobbyEvent event = null;
        if(data != null && data.command.equals("PRIVMSG")){
            var message = PrivMsg.toPrivMsg(data);
        }
        
        return event;
    }
    
    private void run(){
        while(open){
            irc.ping();
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
