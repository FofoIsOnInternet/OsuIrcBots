/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package multiplayerlobby;

import java.util.HashMap;

/**
 *
 * @author faust
 */
public abstract class multiplayerBot {
    /**
     * Links every LobbyEventType to a function
     */
    private final HashMap<LobbyEventType,LobbyEventTrigger> eventTriggers;
    
    /**
     * The multiplayer lobby the bot will be running in
     */
    protected final MultiplayerLobby lobby;
    
    public multiplayerBot(){
        lobby = new MultiplayerLobby("AutoHostBot Test");
        eventTriggers = new HashMap<>();
        initializeEventTriggers();
    }
    
    private void initializeEventTriggers() {
        eventTriggers.put(LobbyEventType.USER_JOIN, this::userJoin);
        eventTriggers.put(LobbyEventType.USER_LEAVE, this::userLeave);
        eventTriggers.put(LobbyEventType.USER_MOVED, this::userMove);
        eventTriggers.put(LobbyEventType.MATCH_CLOSE, this::close);
        // Add other event triggers
    }
    
    /**
     * The action realised when a user joins.
     * @param event informations
     */
    protected void userJoin(LobbyEvent event){}
    
    /**
     * The action realised when a user leaves.
     * @param event informations
     */
    protected void userLeave(LobbyEvent event){}
    
    /**
     * The action realised when a user changes slot.
     * @param event informations
     */
    protected void userMove(LobbyEvent event){}
    
    /**
     * The action realised when a user sends a message.
     * @param event informations
     */
    protected void userMessage(LobbyEvent event){}
    
    /**
     * Tells the lobby it has been closed.
     */
    private void close(LobbyEvent event){
        lobby.close();
    }
    
    private void handleEvent(LobbyEvent event) {
        LobbyEventType eventType = event.getType();
        LobbyEventTrigger trigger = eventTriggers.get(eventType);
        if (trigger != null) {
            trigger.call(event);
        }
    }
    
    /**
     * Main loop of the bot
     */
    public void run(){
        while(lobby.isOpen()){
            LobbyEvent event = lobby.nextEvent();
            if (event != null){
                System.out.println(event.toString());
                handleEvent(event);
            }
        }
    }
}
