/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irc;

/**
 *
 * @author faust
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
