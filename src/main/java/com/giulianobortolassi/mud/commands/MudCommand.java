package com.giulianobortolassi.mud.commands;

import com.giulianobortolassi.mud.Entity;
import com.giulianobortolassi.mud.Server;

/**
 * MudCommand
 */
public interface MudCommand {

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
    public boolean checkRequirements(Entity entity);

    /**
     * Execute the command and return the entity. Its important to return
     * the entity if it was, someway, changed.
     * 
     * @param entity
     * @param args
     * @return
     */
    public Entity execute(Entity entity, String[] args, Server server);
}