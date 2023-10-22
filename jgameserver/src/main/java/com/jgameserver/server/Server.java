package com.jgameserver.server;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jgameserver.server.systems.weather.WeatherControll;

/**
 * Hello world!
 */
public final class Server implements ConnectionListener {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    private final ServerConfig serverConfig;

    private ArrayList<Entity> gameObjects = new ArrayList<>();


    /*
    * Game Systems
    */
    WeatherControll weather = new WeatherControll();

    long pulseCounter = 0;

    /**
     * Instantiate a new Server.
     */
    public Server() {
        // build server with all default configs
            this(new ServerConfigBuilder().build());
    }

    public Server(ServerConfig config) {
        this.serverConfig = config;
        if( serverConfig.getLoggingMode().ordinal() <= LoggingMode.DEBUG.ordinal() ) {
            Log.debugEnabled = true;
        }
        serverConfig.getNetworkManager().addConnectionListener(this);
        // INIT NETWORK MANAGER
    }

    /**
     * Try to stop the main loop.
     * TODO: Rethink the design of the main loop run condition and the shutdown process. Maybe we can use some shutdown
     * hooks on the VM instead of adding this calls on the main thread. It just looks weird now.
     *
     */
    public void stop() {
        this.isRunning.set(false);
    }

    public void shutdown() {
        Log.debug("Shutting down Server", this.getClass());
        this.serverConfig.getNetworkManager().shutdown();
        // Add other objects as a 'GameWorld' or something like that here

        Log.debug("Server shutdown complete.", this.getClass());
    }

    public void mainLoop() throws IOException {
        if( serverConfig.getNetworkManager() == null ) {
            throw new IllegalStateException("Server have no network manager configured.");
        }
        if( serverConfig.getCommandParser() == null) {
            throw new IllegalStateException("Server have no command parser configured.");
        }

        if (!serverConfig.getNetworkManager().isInitialized()) {
            serverConfig.getNetworkManager().init();
        }

        /*
         * Code to test non blocking socket IO
         */

        isRunning.set(true);

        while (isRunning.get()) {
            // check current time.
            long timeCount = System.currentTimeMillis();

            // Check for dead connections and remove it.
            serverConfig.getNetworkManager().removeInvalidConnections();

            // Check for new connections and read input data for all sockets
            serverConfig.getNetworkManager().handleSocketInput();

            // Check for commands from GameObjects
            for (Entity gameObj : gameObjects) {
                String command = gameObj.getCurrentCommand();
                if (command != null) {
                    try{
                        serverConfig.getCommandParser().parseCommand(gameObj, command, this);
                        Log.debug("Parsing Command: " + command, Server.class);
                    } catch( Exception e ){
                        Log.error("Error parsing command: " + command + " Err: " + e.getMessage(), Server.class);
                        gameObj.addResponse(" Hum? Ah! No no no... \r\n");
                    }
                }
            }

            // Update world model
            hearthBeat();

            // Send pending data to all connected clients
            serverConfig.getNetworkManager().handleSocketOutput();

            // Deal with timing
            sleepControl(timeCount);

        }
        Log.debug("Exiting main loop.", this.getClass());
    }

    long cycleTimeMedia = 0;
    long cycleCount = 0;

    private void sleepControl(long initialTime) {
        long timeElapsed = System.currentTimeMillis() - initialTime;
        long pulseDesiredTime = 100; // we want 10 cycles per second.

        if (cycleTimeMedia == 0) {
            cycleTimeMedia = timeElapsed;
        }

        cycleTimeMedia = (cycleTimeMedia + timeElapsed) / 2;

        // here we are just telling your world still running ...
        // debug purpose only
        cycleCount++;
        if (cycleCount % 50 == 0) {
            Log.debug("running. MS: " + cycleTimeMedia, Server.class);
            cycleCount = 0;
        }

        if (timeElapsed < pulseDesiredTime) {
            // We are fast enough, lets sleep a little bit
            long timeToSleep = pulseDesiredTime - timeElapsed;
            try {
                Thread.sleep(timeToSleep);

            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.error("mainLoop() -> Error to thread.sleep", Server.class);
            }
        } else {
            Log.error(" We are to slow!! " + timeElapsed + " ms ", Server.class);
        }
    }

    private void hearthBeat() {
        // Advance 1 unit of time in the game world.
        pulseCounter++;

        weather.pulse(pulseCounter, this);

    }



    @Override
    public void newConnection(Connection connection) {
        // Create a PAW like class, able to receive player input
        Entity gameObj = new Entity(connection, "undefined", "undefined");
        gameObjects.add(gameObj);
    }

    @Override
    public void connectionLost(Connection connection) {
        Iterator<Entity> it = this.gameObjects.iterator();
        while (it.hasNext()) {
            Entity entity = it.next();
            if (entity.getConnection().getId() == connection.getId()) {
                entity.setConnection(null);
                it.remove();
            }
        }
    }

    @Override
    public void registered(Class<?> aClass) {
        if( this.serverConfig.getLoggingMode() == LoggingMode.DEBUG) {
            Log.debug(this.getClass().getSimpleName() + " registered as listener on " + aClass.getSimpleName(), this.getClass());
        }
    }

    @Override
    public void unregistered(Class<?> aClass) {
        if( this.serverConfig.getLoggingMode() == LoggingMode.DEBUG) {
            Log.debug(this.getClass().getSimpleName() + " deregistered as listener on " + aClass.getSimpleName(), this.getClass());
        }
    }


    public List<Entity> getGameObjects(){
        return this.gameObjects;
    }

	public void broadcast(String message) {
        gameObjects.forEach(t -> t.addResponse( message ));
	}

}
