/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
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
