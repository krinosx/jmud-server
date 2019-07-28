package com.giulianobortolassi.mud.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.giulianobortolassi.mud.Log;

/**
 * TelnetProtocolAdapter
 * 
 * Its just a dummy class to test telnet basic response.
 * We must implement telnet relevant parsing and command responses here.
 * 
 */
public class TelnetProtocolAdapter implements ProtocolAdapter {

    @Override
    public void handleInput(ByteBuffer bufer, SocketChannel socket) {
        
        boolean hasTelnetCommand = false;
        bufer.rewind();
        
        while( bufer.hasRemaining() ){
            byte cmd = bufer.get();

            if( cmd == Telnet.IAC ) {
                hasTelnetCommand = true;
                if( bufer.hasRemaining() ) {
                    byte instruction = bufer.get();
                    if( instruction == Telnet.IP ) {
                        Log.debug("Cleint sent an Telnet.IP command. Ignore.", TelnetProtocolAdapter.class);
                    } else if ( instruction == Telnet.DO ) {
                        if( bufer.hasRemaining() ){
                            byte action = bufer.get();
                            if ( action == Telnet.TELOPT_TM){
                                Log.debug("Cleint sent an Telnet.TELOPT_TM.", TelnetProtocolAdapter.class);
                                // ignore and send an ACK
                                ByteBuffer ackResponse = ByteBuffer.allocate(3);
                                ackResponse.put(Telnet.IAC).put(Telnet.WILL)
                                .put(Telnet.TELOPT_TM);
                                ackResponse.flip();
                                try {
                                    Log.debug("Sendind response to Telnet.TELOPT_TM -> [IAC WILL TELOPT_TM]", TelnetProtocolAdapter.class);
                                    socket.write(ackResponse);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.error("Error trying to send ACK for a IP command.", this.getClass());
                                }
                            }
                        }
                    }
                    
                }
            }

        }
        // Copy the buffer and remove the commands
        if(hasTelnetCommand) {
            ByteBuffer copy = ByteBuffer.allocate(bufer.capacity());
            bufer.rewind();
            while( bufer.hasRemaining() ){
                byte content = bufer.get();
                if( !isTelnetCommand(content) ){
                    copy.put( content );
                }
            }

            bufer.clear();
            bufer.put(copy);
            bufer.flip();
        }
    }

    private boolean isTelnetCommand(byte content) {

        return 
        content == Telnet.IAC ||
        content == Telnet.IP || 
        content == Telnet.DM || 
        content == Telnet.DO ||
        content == Telnet.DONT ||
        content == Telnet.WILL ||
        content == Telnet.WONT ||
        content == Telnet.AYT ||
        content == Telnet.TELOPT_TM;
    }

    @Override
	public void handleOutput(ByteBuffer inBuffer, SocketChannel socket) {
		
	}

    
}