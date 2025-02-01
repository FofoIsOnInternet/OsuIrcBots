package com.fofoisoninternet.multiplayerlobby;

/**
 *
 * @author fofoisoninternet
 */
public enum TeamColor {
    RED("red"),
    BLUE("blue");
    
    private final String color;
    
    private TeamColor(String color){
        this.color = color;
    }
    
    @Override
    public String toString(){
        return color;
    }
}
