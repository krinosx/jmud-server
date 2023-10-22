package com.jgameserver.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.jgameserver.server.protocol.ProtocolAdapter;

/**
 * NetworkManager
 */
public class NetworkManager {

    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private SelectionKey serverSocketKey;
    private final ArrayList<Connection> connectionList = new ArrayList<>();
    private final ArrayList<ConnectionListener> listeners = new ArrayList<>();

    private final ProtocolAdapter protocolAdapter;

    // Configuration
    /** Port number to lister for new connections - Default 4444 */
    private final int port;
    /** Bind address to the server socket - Default 'localhost' */
    private static final int DEFAULT_PORT = 4444;
    private final InetAddress bindAddress;

    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public NetworkManager(ProtocolAdapter protocolAdapter){
        this(InetAddress.getLoopbackAddress(), DEFAULT_PORT, protocolAdapter);
    }

    public NetworkManager(InetAddress bindAddress, Integer port, ProtocolAdapter protocolAdapter){
        this.bindAddress = bindAddress;
        this.port = port;
        this.protocolAdapter = protocolAdapter;
    }

    /**
     * Create a new ServerSocket and try to bind it to designed port.
     *
     * @throws IOException if server fails to bind or init network resources
     */
    public void init() throws IOException {
        // if not initialized just ignore
        if( isInitialized.compareAndSet(false, true)) {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(bindAddress, port));
            serverSocketChannel.configureBlocking(false);
            serverSocketKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

    /**
     * Check if the NetworkManager is already initialized
     *
     * @return true if server went through initialization process with success, false otherwise
     */
    public boolean isInitialized() {
        return this.isInitialized.get();
    }

    /**
     * Close the server socket and selector.
     */
    public void shutdown() {
        Log.debug("Shutting down NetworkManager", this.getClass());
        closeAllConnections();
        removeAllConnectionListeners();

        if( this.serverSocketKey != null && this.serverSocketKey.isValid() ) {
        try {
            this.serverSocketKey.channel().close();
        } catch (IOException e){
          Log.error("Failed to close ServerSocket channel. Server will continue the shutdown.", this.getClass());
        }
            this.serverSocketKey.cancel();
        }
        try {
            selector.close();
        } catch (IOException e) {
            Log.error("Failed to close the selector. Server will continue the shutdown.", this.getClass());
        }
        Log.debug("NetworkManager shutdown complete.", this.getClass());
    }

    public void removeInvalidConnections(){
        // Handle dead connections. Remove it from connected list and from key selector.
        Iterator<SelectionKey> keysIterator = selector.keys().iterator();
        while(keysIterator.hasNext()){
            SelectionKey key = keysIterator.next();
            if( !key.isValid() ){
                Object attach = key.attachment();
                if( attach != null ) {
                    Connection conn = (Connection)attach;

                    Log.info("Connection lost. ID: " + conn.getId(), NetworkManager.class);

                    // Notify Listeners
                    notifyConnectionLost(conn);

                    connectionList.remove(conn);

                }
                key.cancel();
            }
        }
    }


    public void handleSocketInput() throws IOException {
        int countKeys = selector.selectNow();

        if (countKeys > 0) {
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();
                  /* Handle new connections */
                if (key.isAcceptable()) {
                    register(selector, serverSocketChannel);
                }

                /* Read pending input */
                if (key.isReadable()) {
                    processInput( key );
                }

                iterator.remove();
            }

        }
    }


    private void register(Selector selector, ServerSocketChannel channel) throws IOException {
        SocketChannel client = channel.accept();
        Connection conn = new Connection(idGenerator.addAndGet(1), client);

        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, conn);

        connectionList.add(conn);

        // Notify listeners
        notifyNewConnection(conn);

        Log.info("New Connection. ID: " + conn.getId() + " Address: " + client.getRemoteAddress(), NetworkManager.class );

    }

    private void processInput(SelectionKey key) throws IOException {
        Connection con = (Connection) key.attachment();
        con.readData(this.protocolAdapter);
    }


    /*
     * Listener Logic
     *
     */

    private void notifyNewConnection(Connection con){
        for (ConnectionListener listener : this.listeners) {
            listener.newConnection(con);
        }
    }

    private void notifyConnectionLost(Connection con){
        for (ConnectionListener listener : this.listeners) {
            listener.connectionLost(con);
        }
    }

    public void addConnectionListener(ConnectionListener listener) {
        this.listeners.add(listener);
        listener.registered(this.getClass());
    }

    public void removeConnectionListener(ConnectionListener listener) {
        this.listeners.remove(listener);
        listener.unregistered(this.getClass());
    }

    private void removeAllConnectionListeners() {
        for(ConnectionListener listener : this.listeners) {
            listener.unregistered(this.getClass());
        }
        listeners.clear();
    }

	public void handleSocketOutput() {
        Iterator<SelectionKey> keysIterator = selector.keys().iterator();
        while(keysIterator.hasNext()){
            SelectionKey key = keysIterator.next();
            if( key.isValid() ){
                Object attach = key.attachment();
                if( attach != null ) {
                    Connection conn = (Connection)attach;
                    try{
                       // From this point no new data may be put into buffer from the game.
                       // Flip it.
                        conn.sendData(protocolAdapter);

                    } catch( IOException e ){
                        Log.error("handleSocketOutput() -> Error to send data! ConnectionID: " + conn.getId() + ". closing.", NetworkManager.class);
                        // Will the cancel close the socket??
                        // key.channel().close();
                        key.cancel();
                    }
                }

            }
        }

    }

    /**
     * Disconnect all pending connections
     */
	public void closeAllConnections() {
	}


}
