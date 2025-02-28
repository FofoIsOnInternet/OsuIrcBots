/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fofoisoninternet.irc;

import java.io.IOException;

/**
 * Manages irc commands and efficient message reading.
 * 
 * @author fofoisoninternet
 */
public class IrcClient {
    private static final IrcClient INSTANCE = new IrcClient();
    private final IrcConnection connection = new IrcConnection();
    
    private String nickname;
    private String password;
    private String server;
    private int port;
    
    private PingManager pingManager;
    private final FlagManager flagManager;
    
    private IrcClient(){
        flagManager = new FlagManager();
    }
    
    public static IrcClient getInstance(){
        return INSTANCE;
    }
    /**
     * Set the user credentials.
     * 
     * @param nickname username
     * @param password password
     * @return the irc client
     */
    public IrcClient setUser(String nickname, String password){
        this.nickname = nickname;
        this.password = password;
        return this;
    }
    
    /**
     * Set the server information.
     * 
     * @param server server address
     * @param port server port
     * @return the irc client
     */
    public IrcClient setServer(String server, int port){
        this.server = server;
        this.port = port;
        return this;
    }
    
    /**
     * Connect the irc to the server.
     * 
     * @param initialChannel Initial channel to be connected in.
     * @return the irc client
     * @throws IOException
     */
    public IrcClient connect(String initialChannel) throws IOException{
        if(nickname == null || password == null || server == null){
            throw new IllegalStateException("User credentials and server must be set before connecting.");
        }
        
        connection.connect(server, port);
        
        connection.send("PASS " + password);
        connection.send("USER " + nickname + " " + nickname + " " + nickname + " :Hello bancho!!");
        connection.send("NICK " + nickname);
        connection.send("JOIN " + initialChannel);
        
        pingManager = new PingManager(connection, server);
         
        return this;
    }
    
    /**
     * Disconnect the irc client from the irc server.
     * @throws IOException 
     */
    public void disconect() throws IOException{
        connection.disconnect();
    }
    
    /**
     * Sends a private message to a specified user/channel
     * @param channel Channel or user to send the message to
     * @param message Text line to send
     */
    public void privateMessage(String channel, String message) {
        connection.send("PRIVMSG " + channel + " :" + message);
    }
    
    /**
     * Joins a channel
     * @param channel Channel to join
     */
    public void join(String channel) {
        connection.send("JOIN " + channel);
    }
    
    /**
     * Leaves a channel
     * @param channel
     */
    public void leave(String channel){
        connection.send("PART "+ channel);
    }
    
    /**
     * Ping the server to keep the connection alive.
     */
    public void ping(){
        if(pingManager != null) pingManager.ping();
    }
    
    /**
     * Add a new flag to the list of interesting messages
     * 
     * @param f the Flag
     */
    public void addFlag(Flag f) {
        flagManager.addFlag(f);
    }

    public void removeFlag(Flag f) {
        flagManager.removeFlag(f);
    }

    public void raisePriorityFlag(Flag f) {
        flagManager.raisePriorityFlag(f);
    }
    
    /**
     * Returns the next interesting line of data
     * @return An IrcProtocolMessage object
     */
    public IrcProtocolMessage NextData() {
        IrcProtocolMessage data = null;
        IrcProtocolMessage msg;
        long startTime = System.currentTimeMillis();
        // Leaves a 15 secondes wait time limit for the answer to come
        long maxRunTime = flagManager.getPriorityFlag() == null ? 120000 : 15000;
        
        try {
            String line;
            // If there is no priorityFlag and if there are lines in the priority queue
            if(flagManager.getPriorityFlag() == null && !flagManager.isProrityQueueEmpty()){
                while(System.currentTimeMillis() - startTime < maxRunTime && 
                        data == null && !flagManager.isProrityQueueEmpty()){
                    msg = flagManager.getNextPriorityMessage();
                    if(flagManager.isMessageInteresting(msg)){
                        data = msg;
                    }
                }
            }else{
                // Then we read the rest of the incoming data
                while (System.currentTimeMillis() - startTime < maxRunTime && 
                        data == null) {
                    line = connection.readLine();
                    if(line != null){
                        // Extract the informations
                        msg = new IrcProtocolMessage(line);
                        // If there is no priority flag
                        if (flagManager.getPriorityFlag() == null){
                            // If the message can be interesting
                            if(flagManager.isMessageInteresting(msg)){
                                data = msg;
                            }
                        }else{
                            // if the message fills the requirements of the priority flag
                            if(flagManager.isMessagePriority(msg)){
                                data = msg;
                            }else if (flagManager.isMessageInteresting(msg)){
                                // else if the message can be interesting
                                flagManager.addToPriorityQueue(msg);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        flagManager.resetPriorityFlag(); // removes the priority state
        return data;
    }
    
    /**
     * Return the next message sent to the client by a user/channel
     * @param channel channel/user to wait from
     * @return A PrivMsg object
     */
    public PrivMsg waitPrivateMessage(String channel){
        PrivMsg msg = null;
        raisePriorityFlag(new Flag(channel, "PRIVMSG", new String[]{nickname}));
        msg = new PrivMsg(NextData());
        return msg;
    }
}
