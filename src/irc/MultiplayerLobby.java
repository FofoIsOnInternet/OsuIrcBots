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
    
    public MultiplayerLobby(String name){
        // Irc server details
        String server = "irc.ppy.sh";
        int port = 6667;
        String channel = "#osu";
        String nickname = "f_o_f_o";
        String password = "4a45538f";
        // Instantiate an irc on bancho
        irc = new Irc(server,port,channel,nickname,password);
        irc.run();
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
     * Returns an array containing the last messages from the lobby
     * @return an array of messages (PrivMsg) 
     */
    private PrivMsg[] get_messages(){
        PrivMsg[] messages = null;
        List<PrivMsg> room_messages = new ArrayList<>();
        for(PrivMsg msg : messages ){
            if(msg.recipient.equals(roomId)){
                room_messages.add(msg);
            }
        }
        return room_messages.toArray(PrivMsg[]::new);
    }
    
    /**
     * Close the lobby.
     */
    private void close(){
        say("!mp close");
        open = false;
    }
    
    public void run(){
        while(open){
            for(PrivMsg msg : get_messages() ){
                System.out.println(msg.toString());
                if(msg.message.contains("!quit")){
                    close();
                }
            }
        }
    }
    
}
