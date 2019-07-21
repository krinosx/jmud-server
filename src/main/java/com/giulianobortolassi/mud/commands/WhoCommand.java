package com.giulianobortolassi.mud.commands;

import java.util.Iterator;
import java.util.List;


import com.giulianobortolassi.mud.Entity;
import com.giulianobortolassi.mud.Server;

/**
 * WhoCommand
 * 
 * Provide a list of active connections
 * 
 * 
 */
public class WhoCommand implements MudCommand {

    @Override
    public boolean checkRequirements(Entity entity) {
        return entity != null;
    }

    @Override
    public Entity execute(Entity entity, String[] args, Server server) {

        List<Entity> gameObjects = server.getGameObjects();

        StringBuffer output = new StringBuffer();
        output.append(" --- Players Online: ------ \r\n"); 
        output.append(" Name \t Level \r\n ");
        Iterator<Entity> objectsIterator = gameObjects.iterator();
        int totalOnline = 0;
        while (objectsIterator.hasNext()) {
            Entity objEntity = objectsIterator.next();

            if (objEntity.getConnection() != null) {
                output.append(objEntity.getName()).append("\t 0 \r\n");
                totalOnline++;
            }
        }
        output.append("\r\n --- ").append(totalOnline).append(" ------------------' \r\n");
        entity.addResponse(output.toString());

        return entity;
    }

}