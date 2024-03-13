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
    private String server;
    private long lastPing;
    /**
     * Has to be lower than the maximum run time of the NextData method.
     * in the worst scenario, here it'll leave the program 10 seconds
     * to run between two NextData calls. Otherwise the client is going
     * to be timed out.
     * ( Ping -> NextData(49sec**) -> No ping -> NextData(120sec(max)) -> Ping
     * Total : 169sec (time out is 180 seconds)
     * What if we leave 60 seconds ? Well ...
     * ( Ping -> NextData(59sec**) -> No ping -> NextData(120sec(max)) -> Ping
     * Total : 179sec (time out is 180 seconds) dangerous
     * What if it's 2 minutes ? 
     * ( Ping -> NextData(119sec**) -> No ping -> NextData(120sec(max)) -> Ping
     * Total : 239sec (time out is 180 seconds) obvious disconection
     * 
     * **(maximum run time for the next ping to not be sent)
     */
    private static final long PING_INTERVAL = 50000; 
    
    private List<Flag> interests = new ArrayList<>();
    private Flag priorityFlag = null;
    private Queue<IrcProtocolMessage> priorityQueue = new PriorityQueue<>();

    public Irc(String server, int port, String channel, String nickname, String password) {
        try {
            // Connect to the IRC server
            ircSocket = new Socket(server, port);
            out = new PrintWriter(ircSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));
            this.server = server;

            // Send user and nickname commands
            send("PASS " + password);  // Send the password
            send("USER " + nickname + " " + nickname + " " + nickname + " :Hello bancho!!");
            send("NICK " + nickname);
            hostUserName = nickname;
            
            addFlag(new Flag(null, "PING", null));
            addFlag(new Flag(null, "PONG", null));

            // Join the channel
            send("JOIN " + channel);
            lastPing = System.currentTimeMillis();
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
                while(System.currentTimeMillis() - startTime < maxRunTime && 
                        data == null && !priorityQueue.isEmpty()){
                    msg = priorityQueue.remove();
                    if(msg.isInteresting(interests.toArray(Flag[]::new))){
                        data = msg;
                    }
                }
            }else{
                // Then we read the rest of the incoming data
                while (System.currentTimeMillis() - startTime < maxRunTime && 
                        data == null) {
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
     * Ping the server every 2 minutes.
     * HAS TO BE CALLED REGULARLY WHEN USING AN INSTANCE OF THIS CLASS
     */
    public void ping(){
        if(System.currentTimeMillis() - lastPing  > PING_INTERVAL){
            send("PING "+server);
            lastPing = System.currentTimeMillis();
        }
    }
    
    /**
     * Return the next message sent to the client by a user/channel
     * @param channel channel/user to wait from
     * @return A PrivMsg object
     */
    public PrivMsg waitPrivateMessage(String channel){
        PrivMsg msg = null;
        raisePriorityFlag(new Flag(channel, "PRIVMSG", new String[]{hostUserName}));
        msg = PrivMsg.toPrivMsg(NextData());
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
                System.out.println(PrivMsg.toPrivMsg(data).toString());
            }else{
                System.out.println(data.toString());
            }
        }
    }
}
