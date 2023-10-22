package com.jgameserver.server.systems;

import com.jgameserver.server.Server;

/**
 * BaseSystem
 */
public interface BaseSystem {

    /** Execute a pulse advance in game world */
    void pulse(long pulse, Server server);

}
