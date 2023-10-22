package com.jgameserver.server.commands;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;

public interface CommandParser {
    void parseCommand(Entity entity, String command, Server server);
}
