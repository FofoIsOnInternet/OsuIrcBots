package com.fofoisoninternet.multiplayerlobby;

/**
 *
 * @author fofoisoninternet
 */
public enum TeamMode implements multiplayerObjectEnum{
    HEAD2HEAD,
    TAGCOOP,
    TEAMVS,
    TAGTEAMVS;
    
    /**
     * Gives the number associated to each mode.
     * Default 0.
     * @return An integer
     */
    @Override
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
