package com.fofoisoninternet.multiplayerlobby;

import com.oopsjpeg.osu4j.backend.Osu;
import java.util.HashMap;
import com.fofoisoninternet.utils.Tools;

/**
 *
 * @author fofoisoninternet
 */
public abstract class MultiplayerBot {
    /**
     * Links every LobbyEventType to a function
     */
    private final HashMap<LobbyEventType,LobbyEventTrigger> eventTriggers;
    
    /**
     * The multiplayer lobby the bot will be running in
     */
    protected final MultiplayerLobby lobby;
    
    /**
     * An instance of the osu API
     */
    protected final Osu osuApi;
    
    public MultiplayerBot(){
        // New lobby & irc
        lobby = new MultiplayerLobby("AutoHostBot Test");
        eventTriggers = new HashMap<>();
        initializeEventTriggers();
        // New API
        osuApi = Osu.getAPI(Tools.API_KEY);
    }
    
    /**
     * Initialize lobby event triggers.
     * 
     * Follow the naming convention on[Subject][Action]
     */
    private void initializeEventTriggers() {
        eventTriggers.put(LobbyEventType.USER_JOIN, this::onUserJoin);
        eventTriggers.put(LobbyEventType.USER_LEAVE, this::onUserLeave);
        eventTriggers.put(LobbyEventType.USER_MOVE, this::onUserMove);
        eventTriggers.put(LobbyEventType.MATCH_CLOSE, this::onMatchClose);
        eventTriggers.put(LobbyEventType.USER_MESSAGE,this::onUserMessage);
        eventTriggers.put(LobbyEventType.MAP_PICK,this::onMapPick);
        eventTriggers.put(LobbyEventType.GAME_START,this::onGameStart);
        eventTriggers.put(LobbyEventType.GAME_END,this::onGameEnd);
        eventTriggers.put(LobbyEventType.ALL_USERS_READY,this::onAllUsersReady);
        eventTriggers.put(LobbyEventType.TIMER_END,this::onTimerEnd);
        eventTriggers.put(LobbyEventType.USER_SCORE,this::onUserScore);
        eventTriggers.put(LobbyEventType.USER_CHANGE_TEAM,this::onUserChangeTeam);
        eventTriggers.put(LobbyEventType.HOST_CHANGE,this::onHostChange);
        // Add other event triggers
    }
    
    /**
     * The action realized when a user joins.
     * @param event data
     */
    protected void onUserJoin(LobbyEvent event){}
    
    /**
     * The action realized when a user leaves.
     * @param event data
     */
    protected void onUserLeave(LobbyEvent event){}
    
    /**
     * The action realized when a user changes slot.
     * @param event data
     */
    protected void onUserMove(LobbyEvent event){}
    
    /**
     * The action realized when a user sends a message.
     * @param event data
     */
    protected void onUserMessage(LobbyEvent event){}
    
    /**
     * The action realized when a map is picked
     * @param event data
     */
    protected void onMapPick(LobbyEvent event){}
    
    /**
     * The action realized when a game start
     * @param event data
     */
    protected void onGameStart(LobbyEvent event){}
    
    /**
     * The action realized when a game end
     * @param event data
     */
    protected void onGameEnd(LobbyEvent event){}
    
    /**
     * The action realized when all players are ready
     * @param event data
     */
    protected void onAllUsersReady(LobbyEvent event){}
    
    /**
     * The action realized when a set time end
     * @param event data
     */
    protected void onTimerEnd (LobbyEvent event){}
    
    /**
     * The action realized when a user finished playing
     * @param event data
     */
    protected void onUserScore (LobbyEvent event){}
    
    /**
     * The action realized when a user changes of team
     * @param event data
     */
    protected void onUserChangeTeam (LobbyEvent event){}
    
    /**
     * The action realized when the host changed
     * @param event data
     */
    protected void onHostChange (LobbyEvent event){}
    
    /**
     * Tells the lobby it has been closed.
     */
    private void onMatchClose(LobbyEvent event){
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
            lobby.ping();
            LobbyEvent event = lobby.nextEvent();
            if (event != null){
                System.out.println(event.toString());
                handleEvent(event);
            }
        }
        
    }
}
