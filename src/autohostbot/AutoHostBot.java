/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autohostbot;

import java.util.LinkedList;
import java.util.Queue;
import multiplayerlobby.LobbyEvent;
import multiplayerlobby.multiplayerBot;

/**
 *
 * @author faust
 */
public class AutoHostBot extends multiplayerBot{
    private final Queue<User> userQueue;
    
    public AutoHostBot(){
        super();
        userQueue = new LinkedList<>();
    }
    
    @Override
    protected void userJoin(LobbyEvent event){
        userQueue.add(new User(event.getValue("username"),
                               Integer.parseInt(event.getValue("slot"))));
        System.out.println(userQueue);
    }
    
    @Override
    protected void userLeave(LobbyEvent event){
        userQueue.remove(new User(event.getValue("username")));
        System.out.println(userQueue);
    }
    
    @Override
    protected void userMove(LobbyEvent event){
        User u = new User(event.getValue("username"));
        userQueue.forEach(element ->{
            if(element.equals(u)){
                element.move(Integer.parseInt(event.getValue("slot")));
            }
        });
        System.out.println(userQueue);
    }
    
    
}
