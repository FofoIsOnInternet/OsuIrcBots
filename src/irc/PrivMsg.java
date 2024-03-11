/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irc;

import java.util.Date;
import java.text.DateFormat;   
import java.text.SimpleDateFormat;
/**
 *
 * @author faust
 */
public class PrivMsg {
    public String sender;
    public String message;
    public String recipient;
    public long timestamp;
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm");
    
    public PrivMsg(String sender, String message, String recipient){
        this.sender = sender;
        this.message = message;
        this.recipient = recipient;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * Convert a line of data from the irc server into a PrivMsg object
     * @param m String of data
     * @return A PrivMsg object
     */
    public static PrivMsg toPrivMsg(String m){
        String sender = null;
        String message = null;
        String recipient = null;
        if(m.contains("PRIVMSG" )){
            sender = m.split("!")[0].replaceFirst(":", "");
            message = m.split(" :")[1];
            recipient = m.split(" :")[0].split("PRIVMSG ")[1];
        }
        return new PrivMsg(sender,message,recipient);
    }
    
    /**
     * Convert an IrcProtocolMessage object into a PrivMsg object if possible
     * @param msg The IrcProtocolMessage object
     * @return A PrivMsg object
     */
    public static PrivMsg toPrivMsg(IrcProtocolMessage msg){
        String sender = null;
        String message = null;
        String recipient = null;
        if(msg.command.equals("PRIVMSG")){
            sender = msg.sender;
            message = msg.attributes[1];
            recipient = msg.attributes[0];
        }
        return new PrivMsg(sender,message,recipient);
    }
    
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(PrivMsg.DATE_FORMAT.format(new Date(timestamp)));
        b.append("|TO:");
        b.append(recipient);
        b.append("|");
        b.append(sender);
        b.append(" : ");
        b.append(message);
        return b.toString() ;
    }
}
