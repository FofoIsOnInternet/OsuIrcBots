/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
 */
public class Map {
    private int id;
    private float starRating;
    private int length; // Seconds
    
    public Map(int mapid){
        id = mapid;
    }
    
    public int id(){
        return id;
    }
}
