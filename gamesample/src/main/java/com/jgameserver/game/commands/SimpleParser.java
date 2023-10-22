package com.jgameserver.game.commands;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;
import com.jgameserver.server.commands.CommandParser;

public class SimpleParser implements CommandParser {

    EchoCommand echo = new EchoCommand();

    public SimpleParser(){
        System.out.println("Creating simple parser");
    }
    @Override
    public void parseCommand(Entity entity, String command, Server server) {

        if(command != null && command.startsWith("echo") && echo.checkRequirements(entity)) {

            String[] params = command.split(" ");

            echo.execute(entity, params, server);

        }


    }
}
