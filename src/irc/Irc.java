/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package irc;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author faust
 */
public class Irc {
    private Socket ircSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    private List<Flag> interests = new ArrayList<>();
    private Flag priorityFlag = null;

    public Irc(String server, int port, String channel, String nickname, String password) {
        try {
            // Connect to the IRC server
            ircSocket = new Socket(server, port);
            out = new PrintWriter(ircSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));

            // Send user and nickname commands
            send("PASS " + password);  // Send the password
            send("USER " + nickname + " " + nickname + " " + nickname + " :Example IRC Bot");
            send("NICK " + nickname);

            // Join the channel
            send("JOIN " + channel);
            
            // Adds some basic flags
            addFlag(new Flag(null,"PRIVMSG", new String[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sends a command to the IRC server.
     * @param message Text line to send
     */
    private void send(String message) {
        System.out.println(message);
        out.println(message);
    }
    
    
    /**
     * Returns the IRC data
     * @return A string with lines of data
     */
    public IrcProtocolMessage NextData() {
        IrcProtocolMessage data = null;
        try {
            String line;
            while (null != (line = in.readLine()) && data == null) {
                // Extract the informations
                IrcProtocolMessage t = new IrcProtocolMessage(line);
                // If there is no priority flag
                if (priorityFlag == null){
                    // If the message can be interesting
                    if(t.isInteresting(interests.toArray(Flag[]::new))){
                        data = t;
                    }
                }else{
                    // if the message fills the requirements of the priority flag
                    // else if the message can be interesting
                    // else 
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public void addFlag(Flag f){
        interests.add(f);
    }
    public void removeFlag(Flag f){
        interests.remove(f);
    }
    
    /**
     * Sends a private message to a specified user/channel
     * @param channel Channel or user to send the message to
     * @param message Text line to send
     */
    public void privateMessage(String channel, String message) {
        send("PRIVMSG " + channel + " :" + message);
    }
    
    /**
     * Joins a channel
     * @param channel Channel to join
     */
    public void join(String channel) {
        send("JOIN " + channel);
    }
    
    public PrivMsg waitPrivateMessage(String channel){
        PrivMsg msg = null;
        long startTime = System.currentTimeMillis();
        long runTime = 60000;
        while(msg == null && (startTime - System.currentTimeMillis()) < runTime){
            
        }
        return msg;
    }
    
    public void run(int runTime){
        while (true) {
            IrcProtocolMessage data = NextData();
            System.out.println(data.toString());
        }
    }
    public void run(){
        run(120);
    }
}
