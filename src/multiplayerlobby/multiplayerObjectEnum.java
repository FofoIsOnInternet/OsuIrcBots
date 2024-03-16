/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
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
