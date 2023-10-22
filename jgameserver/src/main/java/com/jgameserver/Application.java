package com.jgameserver;

import com.jgameserver.server.*;
import com.jgameserver.server.commands.DefaultCommandParser;
import com.jgameserver.server.protocol.telnet.TelnetProtocolAdapter;

import java.io.IOException;

public class Application {

    /**
     * Startup the applications.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws IOException {

        ServerConfig config = new ServerConfigBuilder()
            .networkManager(new NetworkManager(new TelnetProtocolAdapter()))
            .commandParser(new DefaultCommandParser())
            .build();

        Server gameServer = new Server(config);
        // TODO: Load and init systems (AI, etc etc) before entering the main loop

        // Enter the main loop phase. It is a blocking call.
        gameServer.mainLoop();


        // Shutdown all game server systems.
        gameServer.shutdown();

    }
}
