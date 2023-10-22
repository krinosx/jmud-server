package com.jgameserver.game.commands;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;
import com.jgameserver.server.commands.GameCommand;

public class EchoCommand implements GameCommand {
    @Override
    public boolean checkRequirements(Entity entity) {
        return entity != null;
    }

    @Override
    public Entity execute(Entity entity, String[] args, Server server) {

        StringBuilder buf = new StringBuilder();
        for(String arg : args) {
            buf.append(arg).append(" ");
        }

        entity.addResponse(buf.toString());
        return entity;
    }
}
