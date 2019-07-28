
package com.giulianobortolassi.mud;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.giulianobortolassi.mud.protocol.ProtocolAdapter;
import com.giulianobortolassi.mud.protocol.TelnetCommandExtractor;

/**
 * Connection
 */
public class Connection {

    public static int MAX_BUFFER_SIZE = 1024;

    private Integer id;
    private SocketChannel socket;
    private ByteBuffer inBuffer;
    private ByteBuffer outBuffer;

    private CommandExtractor<String> extractor;

    public Connection(Integer id, SocketChannel socket) {
        this.id = id;
        this.socket = socket;

        this.inBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
        this.outBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
        this.extractor = new TelnetCommandExtractor( );
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public void readData(ProtocolAdapter adapter) throws IOException {
        this.inBuffer.clear();
        int read = this.socket.read(this.inBuffer);
        if( read == -1){
            this.socket.close();
        }
        this.inBuffer.flip();
        // Check the buffer for protocul specific input
        adapter.handleInput(this.inBuffer, this.socket);


    }
  
    public String getInput(){
        return this.getInput(this.extractor);
    }

    /**
     * Parse the input buffer and extract a command string.
     * 
     * @param extractor
     * @return
     */
    public String getInput(CommandExtractor<String> extractor){
        String command = null;
        this.inBuffer.flip();

        // Check telnet specific commands
        if( this.inBuffer.hasRemaining()) { 
            command = extractor.extract(this.inBuffer);        
        }
        
        this.inBuffer.clear();
        return command;
    }

    public void prepareData(String data) {
        byte[] byteToSend = data.getBytes();
        
        int bufferRemaining = this.outBuffer.capacity()- this.outBuffer.position();
        
        if( bufferRemaining < byteToSend.length ){
            // Buffer overflow
            Log.error(" prepareData(): Buffer Overflow.", this.getClass());
            return;
        }
        // Fill the output buffer for the given connection
        this.outBuffer.put(byteToSend);

    }

    /**
     * Get the pending data from outbuffer and send to socket.
     * From this point no more data may be written into the buffer from the
     * game. So we start with the buffer.flip() function.
     * @param adapter
     * @throws IOException
     */
    public void sendData(ProtocolAdapter adapter) throws IOException {
        // We have data to send
        this.outBuffer.flip();
        if( this.outBuffer.hasRemaining() ) {            
            if( adapter != null ){
                adapter.handleOutput(this.outBuffer, this.socket);
            }
            int limit = this.outBuffer.limit();
            this.outBuffer.rewind();
            int writtenBytes = this.socket.write(this.outBuffer);

            if( limit > writtenBytes){
                // Not all bytes were wrintten...
                // Adjust buffer properties to send the rest of data on next pulse
                Log.error("sendData(): Not all bytes sent!!", this.getClass());
            } 
        }
        this.outBuffer.clear();

    }


    public void disconnect(){
        try {
            // flush all pending data
            sendData(null);

            // disconnect
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}