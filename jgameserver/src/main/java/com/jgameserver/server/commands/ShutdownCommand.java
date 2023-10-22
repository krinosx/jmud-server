package com.jgameserver.server.commands;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;

/**
 * QuitCommand
 */
public class ShutdownCommand implements GameCommand {

    @Override
    public boolean checkRequirements(Entity entity) {
        return true;
    }

    @Override
	public Entity execute(Entity entity, String[] args, Server server) {
        server.broadcast("Server shutting down.");
        server.stop();
        return entity;
	}


}
