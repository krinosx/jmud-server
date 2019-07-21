package com.giulianobortolassi.mud.systems;

import com.giulianobortolassi.mud.Server;

/**
 * BaseSystem
 */
public interface BaseSystem {

    /** Execute a pulse advance in game world */
    public void pulse(long pulse, Server server);

}