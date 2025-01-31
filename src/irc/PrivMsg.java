package irc;

import java.util.Date;
import java.text.DateFormat;   
import java.text.SimpleDateFormat;
/**
 *
 * @author fofoisoninternet
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
    public PrivMsg(IrcProtocolMessage msg){
        if(msg.command.equals("PRIVMSG")){
            sender = msg.sender;
            message = msg.attributes[1];
            recipient = msg.attributes[0];
        }
    }
    public PrivMsg(String m){
        if(m.contains("PRIVMSG" )){
            sender = m.split("!")[0].replaceFirst(":", "");
            message = m.split(" :")[1];
            recipient = m.split(" :")[0].split("PRIVMSG ")[1];
        }
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
