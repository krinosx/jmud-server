package com.giulianobortolassi.mud.commands;

import com.giulianobortolassi.mud.Entity;
import com.giulianobortolassi.mud.Server;

/**
 * QuitCommand
 */
public class QuitCommand implements MudCommand {

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