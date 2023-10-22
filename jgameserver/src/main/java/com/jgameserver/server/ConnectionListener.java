package com.jgameserver.server;

/**
 * ConnectionListener
 */
public interface ConnectionListener {

    void newConnection(Connection connection);

    void connectionLost(Connection connection);

    void registered(Class<?> aClass);

    void unregistered(Class<?> aClass);
}
