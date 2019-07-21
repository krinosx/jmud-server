package com.giulianobortolassi.mud;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

import com.giulianobortolassi.mud.commands.CommandParser;
import com.giulianobortolassi.mud.systems.weather.WeatherControll;

/**
 * Hello world!
 */
public final class Server implements ConnectionListener {


    static LinkedList<String> commandList = new LinkedList<>();

    static CommandParser commandParser = new CommandParser();

    private ArrayList<Entity> gameObjects = new ArrayList<>();

    private NetworkManager networkManager;

    /*
    * Game Systems
    */
    WeatherControll weather = new WeatherControll();



    long pulseCounter = 0;

    /**
     * Instantiate a new Server.
     */
    private Server() {
    }

    /**
     * Add a NetworkManager implementation to handle input and output
     * traffic
     * 
     * @param networkManager the networkManager to set
     */
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    /**
     * Startup the applications.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws IOException {

        Server gameServer = new Server();

        // TODO: Load server data, configs etc


        // TODO: Load and init systems (AI, etc etc)
        NetworkManager networkManager = new NetworkManager();
        gameServer.setNetworkManager(networkManager);
        networkManager.addConnectionListener(gameServer);
        networkManager.init();


        // Enter the main loop pahse        
        gameServer.mainLoop();


        // Cleanup our mess

        networkManager.removeConnectionListener(gameServer);
        networkManager.closeAllConnections();
        networkManager.shutdown();

    }

    public void mainLoop() throws IOException {
        /*
         * Code to test non blocking socket IO
         */

        while (true) {
            // check current time.
            long timeCount = System.currentTimeMillis();

            // Check for dead connections and remove it.
            networkManager.removeInvalidConnections();

            // Check for new connections and read input data for all sockets
            networkManager.handleSocketInput();

            // Check for commands from GameObjects
            for (Entity gameObj : gameObjects) {
                String command = gameObj.getCurrentCommand();
                if (command != null) {
                    try{ 
                    commandParser.parseCommand(gameObj, command, this);
                    Log.info("Parsing Command: " + command, Server.class);
                    } catch( Exception e ){
                        Log.error("Error parsing command: " + command + " Err: " + e.getMessage(), Server.class);
                        gameObj.addResponse(" Hum? Ah! No no no... \r\n");
                    }      
                }
            }

            // Update world model
            hearthBeat();

            // Send pending data to all connected clients
            networkManager.handleSocktOutput();

            // Deal with timming
            sleepControll(timeCount);

        }
    }

    long cycleTimeMedia = 0;
    long cycleCount = 0;

    private void sleepControll(long intialTime) {
        long timeElapsed = System.currentTimeMillis() - intialTime;
        long pulseDesiredTime = 100; // we want 10 cycles per seccond.

        if (cycleTimeMedia == 0) {
            cycleTimeMedia = timeElapsed;
        }

        cycleTimeMedia = (cycleTimeMedia + timeElapsed) / 2;

        // here we are just telling your world still running..
        // debug purpose only
        cycleCount++;
        if (cycleCount % 50 == 0) {
            Log.info("running. MS: " + cycleTimeMedia, Server.class);
            cycleCount = 0;
        }

        if (timeElapsed < pulseDesiredTime) {
            // We are fast enought, lets sleep a little bit
            long timeToSleep = pulseDesiredTime - timeElapsed;
            try {
                Thread.sleep(timeToSleep);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.error("mainLoop() -> Erro to trhead.sleep", Server.class);
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


    public java.util.List<Entity> getGameObjects(){
        return this.gameObjects;
    }

	public void broadcast(String message) {
        gameObjects.parallelStream().forEach(new Consumer<Entity>() {
            @Override
            public void accept(Entity t) {
                t.addResponse( message );
            }
        });
	}

}
