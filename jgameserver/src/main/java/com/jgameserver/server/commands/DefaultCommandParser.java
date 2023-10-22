package com.jgameserver.server.commands;

import java.util.HashMap;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;


/**
 * CommandParser
 */
public class DefaultCommandParser implements CommandParser {

    HashMap<String, GameCommand> commands = new HashMap<>();
    public DefaultCommandParser(){
        commands.put("who", new WhoCommand());
        commands.put("quit", new QuitCommand());
        commands.put("colortest", new TestColorCommand());
        commands.put("shutdown", new ShutdownCommand());
    }

    @Override
    public void parseCommand(Entity entity, String command, Server server){

        if( !commands.containsKey(command) ) {
            entity.addResponse("&fr Hun? &fn \r\n");
            return;
        }

        GameCommand commandRef = commands.get(command);

        if( !commandRef.checkRequirements(entity) ){
            entity.addResponse("&fr Ein? &fn \r\n");
            return;
        }

        // Tokenize command string
        String[] args = command.split(" ");

       commandRef.execute(entity, args, server);

    }

}
