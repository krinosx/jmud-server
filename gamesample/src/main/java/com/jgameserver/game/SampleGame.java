package com.jgameserver.game;

import com.jgameserver.game.commands.SimpleParser;
import com.jgameserver.server.NetworkManager;
import com.jgameserver.server.Server;
import com.jgameserver.server.ServerConfig;
import com.jgameserver.server.ServerConfigBuilder;
import com.jgameserver.server.protocol.telnet.TelnetProtocolAdapter;

import java.io.IOException;

public class SampleGame {

    private final Server server;

    public SampleGame(){
        ServerConfig config = new ServerConfigBuilder()
            .networkManager(new NetworkManager(new TelnetProtocolAdapter()))
            .commandParser(new SimpleParser())
            .build();

        server = new Server(config);
    }

    public void runGame() throws IOException {
        // Enter the main loop phase. It is a blocking call.
        server.mainLoop();
        // Shutdown all game server systems.
        server.shutdown();
    }

    public static void main(String[] args) {
        try {
            new SampleGame().runGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
