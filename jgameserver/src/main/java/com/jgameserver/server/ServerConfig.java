package com.jgameserver.server;

import com.jgameserver.server.commands.CommandParser;
import com.jgameserver.server.protocol.ProtocolAdapter;

public class ServerConfig {
    private final ProtocolAdapter protocolAdapter;
    private final NetworkManager networkManager;
    private final CommandParser commandParser;

    private final LoggingMode loggingMode;

    ServerConfig(ProtocolAdapter protocolAdapter, NetworkManager networkManager, CommandParser commandParser, LoggingMode loggingMode) {
        this.protocolAdapter = protocolAdapter;
        this.networkManager = networkManager;
        this.commandParser = commandParser;
        this.loggingMode = loggingMode;
    }

    public ProtocolAdapter getProtocolAdapter() {
        return protocolAdapter;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public CommandParser getCommandParser() {
        return commandParser;
    }

    public LoggingMode getLoggingMode() {
        return loggingMode;
    }
}
