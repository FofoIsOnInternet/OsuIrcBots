package com.fofoisoninternet.multiplayerlobby;

/**
 *
 * @author fofoisoninternet
 */
@FunctionalInterface
public interface LobbyEventTrigger {
    void call(LobbyEvent e);
}
