/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
 */
public enum TeamMode {
    HEAD2HEAD,
    TAGCOOP,
    TEAMVS,
    TAGTEAMVS;
    
    /**
     * Gives the number associated to each mode.
     * Default 0.
     * @return An integer
     */
    public int toInt(){
        int value = 0;
        switch(this){
            case HEAD2HEAD :
                value = 0;
                break;
            case TAGCOOP :
                value = 1;
                break;
            case TEAMVS :
                value = 2;
                break;
            case TAGTEAMVS :
                value = 3;
                break;
        }
        return value;
    }
}
