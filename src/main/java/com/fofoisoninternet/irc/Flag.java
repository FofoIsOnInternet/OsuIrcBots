package com.fofoisoninternet.irc;

/**
 * Message stereotype
 * 
 * @author fofoisoninternet
 */
public class Flag {
    public String sender;
    public String command;
    public String[] attributes;
    
    public Flag(String sender,String command,String[] attributes){
        this.sender = sender;
        this.command = command;
        this.attributes = attributes;
    }
}
