/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autohostbot;

/**
 *
 * @author faust
 */
public class User {
    private String name;
    private int slot;
    private int topCount;
    private int playCount;
    private long lastConnection;
    
    public User(String username,int slot){
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
