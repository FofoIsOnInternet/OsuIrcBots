/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fofoisoninternet.irc;

/**
 *  Helps to keep the connection with the irc server alive.
 * 
 * @author fofoisoninternet
 */
public class PingManager {
    private final IrcConnection connection;
    private final String server;
    
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
    private long lastPing;
    
    public PingManager(IrcConnection connection, String server){
        this.connection = connection;
        this.server = server;
        this.lastPing = System.currentTimeMillis();
    }
    
    /**
     * Ping the irc server to keep the connection alive.
     */
    public void ping(){
        if(!connection.isConnected()) throw new IllegalStateException("Irc not connected.");
        
        if(System.currentTimeMillis() - lastPing  > PING_INTERVAL){
            connection.send("PING "+server);
            lastPing = System.currentTimeMillis();
        }
    }
}
