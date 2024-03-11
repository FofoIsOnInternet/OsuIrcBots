/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irc;

import java.util.Arrays;

/**
 *
 * @author faust
 */
public class IrcProtocolMessage {
    public String sender;
    public String command;
    public String[] attributes;
    public boolean isReply;
    private final String originalMessage;
    
    public IrcProtocolMessage(String message){
        originalMessage = message;
        String[] messageCut = message.split(" ");
        // Command
        command = messageCut[1];
        // Attributes
        int attributesCount = messageCut.length -2;
        int actualAttributesCount = attributesCount;
        attributes = new String[attributesCount];
        int i = 0;
        boolean over = false;
        while(!over && i<attributesCount){
            String attr = messageCut[i+2];
            if(attr.indexOf(":")==0){ // If the long attribute is found
                actualAttributesCount = i+1;
                attr = attr.substring(1);
                over = true;
                for(int j=i+1;j<attributesCount;j++){
                    attr += " " + messageCut[j+2];
                }
            }
            attributes[i] = attr;
            i++;
        }
        attributes = Arrays.copyOf(attributes,actualAttributesCount);
        // Sender
        if(isNumeric(command)){// If the command is numeric then it is a reply from the server
            isReply=true;
            sender = null;
        }else{ // Otherwise we can extract the username
            isReply=false;
            int start = 1;
            int end = messageCut[0].indexOf('!');
            if(end==-1){
                end=1;
            }
            sender = messageCut[0].substring(start, end);
        }
    }
    
    /**
     * The original irc data line.
     * @return The original string sent by the irc server
     */
    public String original(){
        return originalMessage;
    }
    
    /**
     * Compares the data of the message with a flag to see.
     * @param f A Flag or an array of Flag objects
     * @return True iff the data fills the requierements to be flaged.
     */
    public boolean isInteresting(Flag f){
        boolean result = true;
        // Sender
        result &= f.sender == null || (sender != null && sender.equals(f.sender));
        // Command 
        result &= f.command == null || command.equals(f.command);
        // Attributes
        for (int i=0;i<min(f.attributes.length,attributes.length);i++){
            String attr = attributes[i];
            String fattr = f.attributes[i];
            result &= fattr == null || attr.equals(fattr);
        }
        return result;
    }
    public boolean isInteresting(Flag[] fs){
        boolean interesting = false;
        int i = 0;
        while(!interesting && i<fs.length){
            interesting |= isInteresting(fs[i]);
            i++;
        }
        return interesting;
    }
    
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(originalMessage);
        b.append("\nSender : ");
        b.append(sender);
        b.append("\nCommand : ");
        b.append(command);
        b.append("\n Attrs : ");
        for(String attr : attributes){
            b.append(attr);
            b.append(" | ");
        }
        return b.toString();
    }
    
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static int min(int a, int b){
        if(a>b){
            return b;
        }else{
            return a;
        }
    }
}
