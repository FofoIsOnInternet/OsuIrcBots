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
    
    private String hostUserName;
    
    private List<Flag> interests = new ArrayList<>();
    private Flag priorityFlag = null;
    private Queue<IrcProtocolMessage> priorityQueue = new PriorityQueue<>();

    public Irc(String server, int port, String channel, String nickname, String password) {
        try {
            // Connect to the IRC server
            ircSocket = new Socket(server, port);
            out = new PrintWriter(ircSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));

            // Send user and nickname commands
            send("PASS " + password);  // Send the password
            send("USER " + nickname + " " + nickname + " " + nickname + " :Hello bancho!!");
            send("NICK " + nickname);
            hostUserName = nickname;

            // Join the channel
            send("JOIN " + channel);
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
     * Returns the next interesting line of data
     * @return An IrcProtocolMessage object
     */
    public IrcProtocolMessage NextData() {
        IrcProtocolMessage data = null;
        IrcProtocolMessage msg;
        long startTime = System.currentTimeMillis();
        long maxRunTime = 120000; // 2 minutes
        if(priorityFlag != null){ // Leaves a 15 secondes wait time limit for the answer to come
            maxRunTime = 15000;
        }
        try {
            String line;
            // If there is no priorityFlag and if there are lines in the priority queue
            if(priorityFlag == null && !priorityQueue.isEmpty()){
                while(data == null && !priorityQueue.isEmpty()){
                    msg = priorityQueue.remove();
                    if(msg.isInteresting(interests.toArray(Flag[]::new))){
                        data = msg;
                    }
                }
            }
            // Then we read the rest of the incoming data
            while (startTime - System.currentTimeMillis() < maxRunTime 
                    && data == null) {
                line = in.readLine();
                if(line != null){
                    // Extract the informations
                    msg = new IrcProtocolMessage(line);
                    // If there is no priority flag
                    if (priorityFlag == null){
                        // If the message can be interesting
                        if(msg.isInteresting(interests.toArray(Flag[]::new))){
                            data = msg;
                        }
                    }else{
                        // if the message fills the requirements of the priority flag
                        if(msg.isInteresting(priorityFlag)){
                            data = msg;
                        }else if (msg.isInteresting(interests.toArray(Flag[]::new))){
                        // else if the message can be interesting
                            priorityQueue.add(msg);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        priorityFlag = null; // removes the priority state
        return data;
    }
    
    /**
     * Add a new flag to the list of interests
     * @param f the Flag
     */
    public void addFlag(Flag f){
        interests.add(f);
    }
    
    /**
     * Remove the given flag from the list of interests
     * @param f the Flag
     */
    public void removeFlag(Flag f){
        interests.remove(f);
    }
    
    /**
     * Set a flag as a priority.
     * Once the interesting data was found (or not) the priority will be removed.
     * @param f the Flag
     */
    public void raisePriorityFlag(Flag f){
        priorityFlag = f;
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
    
    /**
     * Leaves a channel
     * @param channel
     */
    public void leave(String channel){
        send("PART "+ channel);
    }
    
    /**
     * Disconnect the irc client from the irc server.
     */
    public void disconect(){
        send("QUIT");
    }
    
    /**
     * Return the next message sent to the client by a user/channel
     * @param channel channel/user to wait from
     * @return A PrivMsg object
     */
    public PrivMsg waitPrivateMessage(String channel){
        PrivMsg msg = null;
        raisePriorityFlag(new Flag(channel, "PRIVMSG", new String[]{hostUserName}));
        msg = new PrivMsg(NextData());
        return msg;
    }
    
    /**
     * Simple run loop that prints out the incoming messages.
     */
    public void run(){
        addFlag(new Flag(null,"PRIVMSG",new String[0]));
        while (true) {
            IrcProtocolMessage data = NextData();
            if(data.command.equals("PRIVMSG")){
                System.out.println(new PrivMsg(data).toString());
            }else{
                System.out.println(data.toString());
            }
        }
    }
}
