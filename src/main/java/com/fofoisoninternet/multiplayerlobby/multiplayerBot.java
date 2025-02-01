package com.fofoisoninternet.multiplayerlobby;

import com.oopsjpeg.osu4j.backend.Osu;
import java.util.HashMap;
import com.fofoisoninternet.utils.Tools;

/**
 *
 * @author fofoisoninternet
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
    
    /**
     * An instance of the osu API
     */
    protected final Osu osuApi;
    
    public multiplayerBot(){
        // New lobby & irc
        lobby = new MultiplayerLobby("AutoHostBot Test");
        eventTriggers = new HashMap<>();
        initializeEventTriggers();
        // New API
        osuApi = Osu.getAPI(Tools.API_KEY);
    }
    
    private void initializeEventTriggers() {
        eventTriggers.put(LobbyEventType.USER_JOIN, this::userJoin);
        eventTriggers.put(LobbyEventType.USER_LEAVE, this::userLeave);
        eventTriggers.put(LobbyEventType.USER_MOVED, this::userMove);
        eventTriggers.put(LobbyEventType.MATCH_CLOSE, this::close);
        eventTriggers.put(LobbyEventType.USER_MESSAGE,this::userMessage);
        eventTriggers.put(LobbyEventType.PICKED_MAP,this::pickedMap);
        eventTriggers.put(LobbyEventType.GAME_START,this::gameStarted);
        eventTriggers.put(LobbyEventType.GAME_END,this::gameEnded);
        eventTriggers.put(LobbyEventType.ALL_READY,this::allReady);
        eventTriggers.put(LobbyEventType.TIMER_END,this::timerEnd);
        eventTriggers.put(LobbyEventType.USER_SCORE,this::userScored);
        eventTriggers.put(LobbyEventType.USER_CHANGED_TEAM,this::userChangedTeam);
        eventTriggers.put(LobbyEventType.HOST_CHANGE,this::hostChanged);
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
     * The action realised when a map is picked
     * @param event informations
     */
    protected void pickedMap(LobbyEvent event){}
    
    /**
     * The action realised when a game start
     * @param event informations
     */
    protected void gameStarted(LobbyEvent event){}
    
    /**
     * The action realised when a game end
     * @param event informations
     */
    protected void gameEnded (LobbyEvent event){}
    
    /**
     * The action realised when all players are ready
     * @param event informations
     */
    protected void allReady (LobbyEvent event){}
    
    /**
     * The action realised when a set time end
     * @param event informations
     */
    protected void timerEnd (LobbyEvent event){}
    
    /**
     * The action realised when a user finished playing
     * @param event informations
     */
    protected void userScored (LobbyEvent event){}
    
    /**
     * The action realised when a user changes of team
     * @param event informations
     */
    protected void userChangedTeam (LobbyEvent event){}
    
    /**
     * The action realised when the host changed
     * @param event informations
     */
    protected void hostChanged (LobbyEvent event){}
    
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
            lobby.ping();
            LobbyEvent event = lobby.nextEvent();
            if (event != null){
                System.out.println(event.toString());
                handleEvent(event);
            }
        }
        
    }
}
