/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autohostbot;

import com.oopsjpeg.osu4j.exception.OsuAPIException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import multiplayerlobby.LobbyEvent;
import multiplayerlobby.Map;
import multiplayerlobby.MultiplayerLobby;
import multiplayerlobby.multiplayerBot;

/**
 *
 * @author faust
 */
public class AutoHostBot extends multiplayerBot{
    private final Queue<User> userQueue;
    private User host;
    private Map lastSelectedMap;
    private final HashSet<LobbyEvent> scores;
    private final float minDif;
    private final float maxDif;
    private final int maxLen;
    
    public AutoHostBot(float minDifficulty, float maxDifficulty, int maxLength){
        super();
        userQueue = new LinkedList<>();
        host = null;
        lastSelectedMap = null;
        scores = new HashSet<>();
        minDif = minDifficulty;
        maxDif = maxDifficulty;
        maxLen = maxLength;
        lobby.name(String.format("AutoHost !TEST! | %.2f - %.2f | %d:%02d",
                   minDif,maxDif,maxLen/60,maxLen%60));
    }
    
    @Override
    protected void userJoin(LobbyEvent event){
        // Adds the new player to the queue
        userQueue.add(new User(event.getValue("username"),
                               Integer.parseInt(event.getValue("slot"))));
        // If there's no host and there's only one player in queue
        if(host == null && userQueue.size() == 1){
            nextHost();
        }
        // If there is only one other player in queue and it's the host
        if(host.equals(userQueue.peek()) && userQueue.size() == 2){
            userQueue.add(userQueue.poll()); // move the host behind the new user
        }
    }
    
    @Override
    protected void userLeave(LobbyEvent event){
        User u = new User(event.getValue("username"));
        // Remove the user from the queue
        userQueue.remove(u);
        // If it was the host
        if(host.equals(u)){
            nextHost();
        }
        // If the lobby is empy
        if(userQueue.isEmpty()){
            lobby.abortTimer();
            lobby.map();
        }
    }
    
    @Override
    protected void userMove(LobbyEvent event){
        User u = new User(event.getValue("username"));
        userQueue.forEach(element ->{
            if(element.equals(u)){
                element.move(Integer.parseInt(event.getValue("slot")));
            }
        });
    }
    
    @Override
    protected void pickedMap(LobbyEvent event){
        Map m = null;
        try {
            m = new Map(osuApi,Integer.parseInt(event.getValue("mapid")));
        } catch (OsuAPIException ex) {
            Logger.getLogger(AutoHostBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!m.equals(MultiplayerLobby.DEFAULT_MAP) && !m.equals(lastSelectedMap)){
            if(isMapCorrect(m)){
                for(String l : m.textInfo()){
                    lobby.say(l);
                }
                lastSelectedMap = m;
            }else{
                if(lastSelectedMap != null){
                    lobby.map(lastSelectedMap.getID());
                }else{
                    lobby.map();
                }
            }
        }
    }
    
    @Override
    protected void gameStarted (LobbyEvent event){
        clearScores();
    }
    
    @Override
    protected void gameEnded (LobbyEvent event){
        nextTurn();
    }
    
    @Override
    protected void allReady (LobbyEvent event){
        lobby.say("Everyone is ready!");
        startMap();
    }
    
    @Override
    protected void userScored(LobbyEvent event){
        scores.add(event);
    }
    
    /**
     * Indicates if the given map correspond to criterias of the room.
     * @return 
//     */
    private boolean isMapCorrect(Map m){
        boolean result = true;
        float mDif = m.getDifficulty();
        int mLen = m.getTotalLength();
        if(mDif > maxDif){
            lobby.say(String.format("This map exceed the difficulty limit of this lobby : %.2f > %.2f", mDif,maxDif));
            result = false;
        } else if(mDif < minDif){
            lobby.say(String.format("This map fall behind the difficulty limit of this lobby : %.2f < %.2f", mDif,minDif));
            result = false;
        } else if (mLen > maxLen){
            lobby.say(String.format("This map exceed the maximum length of this lobby : %d < %d", mLen,maxLen));
            result = false;
        }
        return result;
    }
    
    /**
     * Pass the host priviledge to the next player in queue.
     * If the queue is empty clears the host of the room.
     */
    private void nextHost(){
        host = userQueue.poll();
        if(host != null){
            lobby.host(host.getUserName());
            userQueue.add(host);
            // Prints out the queue
            sayQueue();
        }else{
            lobby.clearHost();
        }
    }
    
    private void startMap(){
        if(lastSelectedMap != null && !lastSelectedMap.equals(MultiplayerLobby.DEFAULT_MAP)){
            lobby.set();
            lobby.name();
            lobby.removePassord();
            lobby.start(20);
        }else{
            lobby.say("No selected map.");
        }
        
    }
    
    private void nextTurn(){
        nextHost();
    }
    
    /**
     * Clear the set of scores
     */
    private void clearScores(){
        scores.clear();
    }
    
    /**
     * Prints in the lobby the state of the queue.
     */
    private void sayQueue(){
        StringBuilder sb = new StringBuilder();
        sb.append("Queue : ");
        for(int i=0;i<userQueue.size();i++){
            User u = userQueue.poll();
            sb.append(u.getUserName()).append(", ");
            userQueue.add(u);
        }
        if(sb.length() > 100){
            sb = sb.replace(100,sb.length()-1 , "...");
        }
        lobby.say(sb.toString());
    }
}
