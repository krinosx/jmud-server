package com.giulianobortolassi.mud.commands;

import java.util.HashMap;

import com.giulianobortolassi.mud.Entity;
import com.giulianobortolassi.mud.Server;
import com.giulianobortolassi.mud.commands.MudCommand;
import com.giulianobortolassi.mud.commands.WhoCommand;


/**
 * CommandParser
 */
public class CommandParser {

    HashMap<String, MudCommand> commands = new HashMap<>();


    public CommandParser(){
        commands.put("who", new WhoCommand());
        commands.put("quit", new QuitCommand());

    }
    
    public void parseCommand(Entity entity, String command, Server server){
        
        if( !commands.containsKey(command) ) {
            entity.addResponse("&fr Hun? &fn \r\n");
            return;
        } 

        MudCommand commandRef = commands.get(command);

        if( !commandRef.checkRequirements(entity) ){
            entity.addResponse("&fr Ein? &fn \r\n");
            return;
        }

        // Tokenize command string
        String[] args = command.split(" ");

        Entity ett = commandRef.execute(entity, args, server);

    }

}