package com.fofoisoninternet.multiplayerlobby;

/**
 *
 * @author fofoisoninternet
 */
public interface multiplayerObjectEnum {
    /**
     * Indicates the number understood by bancho.
     * @return An integer, the equivalent of the object for bancho
     */
    public int toInt();
    
    /**
     * The String reprasentation of the object.
     * @return A String containing almost the .name() of the object
     */
    @Override
    public String toString();
}
