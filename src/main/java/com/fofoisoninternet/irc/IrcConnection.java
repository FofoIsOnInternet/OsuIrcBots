package com.fofoisoninternet.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Manages the connection and communication with the irc server.
 * 
 * @author fofoisoninternet
 */
public class IrcConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected = false;
    
    public IrcConnection(){}
    
    /**
     * Connect the Irc to the given server.
     * 
     * @param server
     * @param port
     * @throws IOException 
     */
    public void connect(String server, int port) throws IOException{
        if(connected) throw new IllegalStateException("Irc already connected.");
        
        socket = new Socket(server, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        connected = true;
    }
    
    /**
     * Disconnect the irc from the server.
     * @throws IOException 
     */
    public void disconnect() throws IOException{
        if(!connected) return;
        
        send("QUIT");
        socket.close();
        connected = false;
    }
    
    /**
     * Sends a command to the IRC server.
     * @param message Text line to send
     */
    public void send(String message) {
        if(!connected) throw new IllegalStateException("Irc not connected.");
        
        System.out.println(message);
        out.println(message);
    }
    
    /**
     * Reads the next message received from the server.
     * 
     * @return The message.
     * @throws IOException 
     */
    public String readLine() throws IOException {
        if (!connected) throw new IllegalStateException("Irc not connected.");
        
        return in.readLine();
    }
    
    /**
     * Indicates if the Irc is connected or not.
     * @return True iff the irc is connected
     */
    public boolean isConnected(){
        return connected;
    }
}
