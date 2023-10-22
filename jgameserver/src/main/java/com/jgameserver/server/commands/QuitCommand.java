package com.jgameserver.server.commands;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;

/**
 * QuitCommand
 */
public class QuitCommand implements GameCommand {

    @Override
    public boolean checkRequirements(Entity entity) {
        return entity != null && entity.getConnection() != null;
    }

    @Override
	public Entity execute(Entity entity, String[] args, Server server) {
        // Disconnect will send all pending data before closing the socket
        entity.addResponse(" GoodBye! ");
        entity.getConnection().disconnect();

        return entity;
	}


}
