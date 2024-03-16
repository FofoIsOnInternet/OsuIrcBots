/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package multiplayerlobby;

/**
 *
 * @author faust
 */
@FunctionalInterface
public interface LobbyEventTrigger {
    void call(LobbyEvent e);
}
