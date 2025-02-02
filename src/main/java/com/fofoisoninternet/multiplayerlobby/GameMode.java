package com.fofoisoninternet.multiplayerlobby;

/**
 *
 * @author fofoisoninternet
 */
public enum GameMode implements MultiplayerObjectEnum{
    STD("osu!Standard",0),
    MANIA("osu!Mania",3),
    CTB("osu!CatchTheBeat",2),
    TAIKO("osu!Taiko",1);
    
    private final String name;
    private final int number;
    
    private GameMode(String name, int number){
        this.name = name;
        this.number = number;
    }
    
    @Override
    public int toInt(){
        return number;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
