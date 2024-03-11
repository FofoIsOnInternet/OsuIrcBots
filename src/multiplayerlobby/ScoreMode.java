/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
 */
public enum ScoreMode {
    SCORE,
    ACCURACY,
    COMBO,
    SCOREV2;
    
    /**
     * Gives the number associated to each mode.
     * Default 0.
     * @return An integer
     */
    public int toInt(){
        int value = 0;
        switch(this){
            case SCORE :
                value = 0;
                break;
            case ACCURACY :
                value = 1;
                break;
            case COMBO :
                value = 2;
                break;
            case SCOREV2 :
                value = 3;
                break;
        }
        return value;
    }
}
