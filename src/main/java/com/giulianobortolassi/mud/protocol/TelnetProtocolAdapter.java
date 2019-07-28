package com.giulianobortolassi.mud.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.giulianobortolassi.mud.Color;
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


    byte escapeColor   = (byte)'&';
    byte escapeColorFg = (byte)'f';
    byte escapeColorBg = (byte)'b';
    
    

    @Override
	public void handleOutput(ByteBuffer outBuffer, SocketChannel socket) {
        
        // Check for color codes and replace with telnet escape code.

        /* First, count all mud color escape chars (&f* or &b*). Each code
           will be replaced by a 10 position telnet escape char (\x1B[*;**m)
           We must check the buffer size, for each code we must add 7 bytes
           to the buffer.

            int colorCodeCount = Total of mud color symbols in output.

            int new bufferSize = currentSize + (7 * colorCodeCount)
        */
        outBuffer.rewind();

        int colorCodeCount = 0;
        while( outBuffer.hasRemaining() ) {
            byte currPosition = outBuffer.get();
            if( currPosition == escapeColor ) {
                byte fgbg = outBuffer.hasRemaining() ? outBuffer.get() : 0;
                if(  fgbg == escapeColorFg || fgbg == escapeColorBg ) {
                    // we found a escape color sequence. Get the color especification and sum the colorCodeCount
                    byte colorCode = outBuffer.hasRemaining() ? outBuffer.get() : 0; // we do that to move the buffer pointer
                    colorCodeCount++;
                }  
            }
        }

        if( colorCodeCount > 0 ) {

            int currenSize = outBuffer.limit();
            int capacity = outBuffer.capacity(); // maxBufferCapacity

            int newDesiredCapacity = currenSize + (7*colorCodeCount);

            if( newDesiredCapacity > capacity ) {
                // Buffer overflow.
            }


            ByteBuffer tempBuffer = ByteBuffer.allocate(newDesiredCapacity);
            // once the tmpBuffer is allocated, its already in resset state;
            
            // We must rewind the outBuffer
            outBuffer.rewind();
            while( outBuffer.hasRemaining() ) {
                byte copyPosition = outBuffer.get();
                if( copyPosition == escapeColor ) {
                    byte fgbg = outBuffer.hasRemaining() ? outBuffer.get() : 0;
                    if( fgbg == escapeColorFg || fgbg == escapeColorBg ) {
                        // we found a escape color sequence. Get the color especification and sum the colorCodeCount
                        byte colorCode = outBuffer.hasRemaining() ? outBuffer.get() : null; // we do that to move the buffer pointer
                        // find the code in enum
                        byte[] colorCodeBytes = new byte[3];
                        colorCodeBytes[0] = (byte)'&';
                        colorCodeBytes[1] = fgbg;
                        colorCodeBytes[2] = colorCode;

                        String colorCodeString = new String(colorCodeBytes);
                        Color color = Color.findByCode( colorCodeString );

                        if( color != null ) {
                            tempBuffer.put(color.getTelnetCode());
                        } else {
                            // Did not ind the color. Just put the escape chars
                            tempBuffer.put(colorCodeBytes);
                        }
                    } else {
                        tempBuffer.put(copyPosition);
                        if( fgbg != 0 ) {
                            tempBuffer.put(fgbg);
                        }
                    }
                } else {
                    tempBuffer.put(copyPosition);
                }
                  
            }
            // We finisehd to write. Flip the buffer
            tempBuffer.flip();

            // Replace the outputBuffer content with the replaced version
            outBuffer.clear();

            outBuffer.put(tempBuffer);
            // We finished to write. So flip() the buffer;
            outBuffer.flip();
        }
	}    
}