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
    
    public User(String username){
        this(username,-1,null);
    }
    public User(String username,int slot){
        this(username,slot,null);
    }
    public User(String username,int slot, TeamColor color){
        this.name = username;
        this.slot = slot;
        this.color = color;
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
    
    public void move(int newSlot){
        slot = newSlot;
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof User)){
            return false;
        }
        User u = (User) o;
        return this.name.equals(u.name);
    }
    
    @Override
    public String toString(){
        String s = this.name + "(" + this.slot + ")";
        if(color != null){
            s += "("  + color + ")";
        }
        return s;
    }
}
