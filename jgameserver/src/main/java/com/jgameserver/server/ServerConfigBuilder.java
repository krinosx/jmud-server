package com.jgameserver.server;

import com.jgameserver.server.commands.CommandParser;
import com.jgameserver.server.commands.DefaultCommandParser;
import com.jgameserver.server.protocol.ProtocolAdapter;
import com.jgameserver.server.protocol.telnet.TelnetProtocolAdapter;

import java.net.InetAddress;

public class ServerConfigBuilder {
    // default values
    private final InetAddress DEFAULT_BIND_ADDRESS = InetAddress.getLoopbackAddress();
    private final int DEFAULT_LISTEN_PORT = 4444;


    private CommandParser commandParser;
    private NetworkManager networkManager;
    private LoggingMode loggingMode = LoggingMode.DEBUG;
    private ProtocolAdapter protocolAdapter;
    private int listenPort = DEFAULT_LISTEN_PORT;
    private InetAddress bindAddress = DEFAULT_BIND_ADDRESS;


    public ServerConfigBuilder(){
    }

    public ServerConfigBuilder listenPort(int listenPort) {
        this.listenPort = listenPort;
        return this;
    }

    public ServerConfigBuilder loggingMode(LoggingMode loggingMode) {
        this.loggingMode = loggingMode;
        return this;
    }

    public ServerConfigBuilder networkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
        return this;
    }

    public ServerConfigBuilder commandParser(CommandParser commandParser) {
        this.commandParser = commandParser;
        return this;
    }

    public ServerConfigBuilder protocolAdapter(ProtocolAdapter protocolAdapter) {
        this.protocolAdapter = protocolAdapter;
        return this;
    }

    public ServerConfigBuilder bindingAddress(InetAddress bindAddress) {
        this.bindAddress = bindAddress;
        return this;
    }

    public ServerConfig build() {
        if( this.protocolAdapter == null ) {
            this.protocolAdapter = new TelnetProtocolAdapter();
        }

        if(this.networkManager == null) {
            this.networkManager = new NetworkManager(this.bindAddress, this.listenPort, this.protocolAdapter);
        }

        if( this.commandParser == null) {
            this.commandParser = new DefaultCommandParser();
        }

        return new ServerConfig(protocolAdapter, networkManager, commandParser, loggingMode);
    }

}
