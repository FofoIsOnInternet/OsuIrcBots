/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irc;

import java.util.*;
/**
 *
 * @author faust
 */
public class MultiplayerLobby {
    private Irc irc;
    private String roomName;
    private String roomId;
    private boolean open = false;
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
    
    /**
     * Close the lobby.
     */
    private void close(){
        say("!mp close");
        open = false;
        irc.disconect();
    }
    
    private void run(){
        while(open){
            var data = irc.NextData();
            if(data.command.equals("PRIVMSG")){
                var message = PrivMsg.toPrivMsg(data);
                System.out.println(message.toString());
                if(message.message.contains("!quit "+CLOSE_CODE)){
                    close();
                }
            }else{
                System.out.println(data.toString());
            }
        }
    }
    
}
