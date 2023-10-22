package com.jgameserver.server.commands;

import com.jgameserver.server.Entity;
import com.jgameserver.server.Server;

/**
 * MudCommand
 */
public interface GameCommand {

    /** Check if the entity has the requirements to access this command.
     *
     * We may check if it has the proper level, the proper class, etc
     *
     * If this method return false, the entity will be notified that there is
     * no such command available.
     *
     * @param entity
     * @return
     */
    boolean checkRequirements(Entity entity);

    /**
     * Execute the command and return the entity. Its important to return
     * the entity if it was, someway, changed.
     *
     * @param entity
     * @param args
     * @return
     */
    Entity execute(Entity entity, String[] args, Server server);
}
