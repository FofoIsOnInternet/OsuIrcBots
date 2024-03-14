/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
 */
public enum GameMode {
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
    
    public int toInt(){
        return number;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
