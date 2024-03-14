/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autohostbot;

import multiplayerlobby.TeamColor;

/**
 *
 * @author faust
 */
public class User {
    
    private String name;
    private int slot;
    private TeamColor color;
    private int topCount;
    private int playCount;
    private long lastConnection;
    
    public User(String username,int slot){
        this(username,slot,null);
    }
    public User(String username,int slot, TeamColor color){
        this.name = username;
        this.slot = slot;
        loadIfExists();
    }
    
    /**
     * Load the informations about the current player if they can be found
     */
    private void loadIfExists(){}
    
    /**
     * Save the user in the files
     */
    public void save(){}
}
