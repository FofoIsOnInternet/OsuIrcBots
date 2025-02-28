/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fofoisoninternet.irc;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Helps to identify interesting and important messages comming from the irc server.
 * 
 * @author fofoisoninternet
 */
public class FlagManager {
    private final List <Flag> interests = new ArrayList<>();
    private Flag priorityFlag = null;
    private final Queue<IrcProtocolMessage> priorityQueue = new PriorityQueue<>();
    
    public FlagManager(){}
    
    /**
     * Add a new flag to the list of interests
     * @param f the Flag
     */
    public void addFlag(Flag f){
        interests.add(f);
    }
    
    /**
     * Remove the given flag from the list of interests
     * @param f the Flag
     */
    public void removeFlag(Flag f){
        interests.remove(f);
    }
    
    /**
     * Set a flag as a priority.
     * Once the interesting data was found (or not) the priority will be removed.
     * @param f the Flag
     */
    public void raisePriorityFlag(Flag f){
        priorityFlag = f;
    }
    
    public Flag getPriorityFlag(){
        return priorityFlag;
    }
    
    public void resetPriorityFlag(){
        priorityFlag = null;
    }
    
    public boolean isProrityQueueEmpty(){
        return priorityQueue.isEmpty();
    }
    
    public void addToPriorityQueue(IrcProtocolMessage message){
        priorityQueue.add(message);
    }
    
    public IrcProtocolMessage getNextPriorityMessage(){
        return priorityQueue.poll();
    }
    
    public boolean isMessageInteresting(IrcProtocolMessage message){
        return message.isInteresting(interests.toArray(Flag[]::new));
    }
    
    public boolean isMessagePriority(IrcProtocolMessage message){
        return priorityFlag != null && message.isInteresting(priorityFlag);
    }
    
}
