package com.giulianobortolassi.mud;

/**
 * ConnectionListener
 */
public interface ConnectionListener {

    public void newConnection(Connection connection);
    public void connectionLost(Connection connection);
    
}