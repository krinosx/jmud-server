package com.giulianobortolassi.mud;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.giulianobortolassi.mud.protocol.ProtocolAdapter;

/**
 * NetworkManager
 */
public class NetworkManager {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel; 
    private SelectionKey serverSocketKey;
    private ArrayList<Connection> connectionList = new ArrayList<>();
    private ArrayList<ConnectionListener> listeners = new ArrayList<>();

    private ProtocolAdapter protocolAdapter;

    // Configuration
    /** Port number to lister for new connections - Default 4444 */
    private Integer port = 4444;
    /** Bind address to the server socket - Default 'localhost' */
    private String bindAddress = "localhost";

    private static int idgenerator = 0;

    public NetworkManager(ProtocolAdapter protocolAdapter){
        this.protocolAdapter = protocolAdapter;
    }

    public NetworkManager(String bindAddress, Integer port){
        this.bindAddress = bindAddress;
        this.port = port;
    }

    /**
     * Create a new ServerSocket and try to bind it to designed port.
     * @throws IOException
     */
    public void init() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(bindAddress, port));
        serverSocketChannel.configureBlocking(false);
        serverSocketKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    /**
     * Close the server socket and selector.
     * 
     * @throws IOException
     */
    public void shutdown() throws IOException {
        if( this.serverSocketKey != null && this.serverSocketKey.isValid() ) {
            this.serverSocketKey.channel().close();
            this.serverSocketKey.cancel();
        }

        selector.close();
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

                    // Nofify Listeners
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
                  /* Trata novas conexoes */
                if (key.isAcceptable()) {
                    register(selector, serverSocketChannel);
                }

                /* Le inputs pendentes */
                if (key.isReadable()) {
                    proccessInput( key );
                }

                iterator.remove();
            }             

        }
    }
    

    private void register(Selector selector, ServerSocketChannel channel) throws IOException {
        SocketChannel client = channel.accept();
        Connection conn = new Connection(++idgenerator, client);

        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, conn);

        connectionList.add(conn);

        // Notify listeners
        notifyNewConnection(conn);

        Log.info("New Connection. ID: " + conn.getId() + " Address: " + client.getRemoteAddress(), NetworkManager.class );

    }

    private void proccessInput(SelectionKey key) throws IOException {
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
    }

    public void removeConnectionListener(ConnectionListener listener) {
        this.listeners.remove(listener);
    }

	public void handleSocktOutput() {
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
                        Log.error("handleSocketOutput() -> Erro to send data! ConnectionID: " + conn.getId() + ". closing.", NetworkManager.class);
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