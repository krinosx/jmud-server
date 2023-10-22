package com.jgameserver.server.protocol.telnet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.jgameserver.server.protocol.ProtocolAdapter;
import com.jgameserver.server.Log;

/**
 * TelnetProtocolAdapter
 * <p>
 * Its just a dummy class to test telnet basic response.
 * We must implement telnet relevant parsing and command responses here.
 */
public class TelnetProtocolAdapter implements ProtocolAdapter {


    byte escapeColor = (byte) '&';
    byte escapeColorFg = (byte) 'f';
    byte escapeColorBg = (byte) 'b';
    byte escapeExtras = (byte) 'e';

    @Override
    public void handleInput(ByteBuffer buffer, SocketChannel socket) {

        boolean hasTelnetCommand = false;
        buffer.rewind();

        while (buffer.hasRemaining()) {
            byte cmd = buffer.get();

            if (cmd == Telnet.IAC) {
                hasTelnetCommand = true;
                if (buffer.hasRemaining()) {
                    byte instruction = buffer.get();
                    if (instruction == Telnet.IP) {
                        Log.debug("Client sent an Telnet.IP command. Ignore.", TelnetProtocolAdapter.class);
                    } else if (instruction == Telnet.DO) {
                        if (buffer.hasRemaining()) {
                            byte action = buffer.get();
                            if (action == Telnet.TELOPT_TM) {
                                Log.debug("Client sent an Telnet.TELOPT_TM.", TelnetProtocolAdapter.class);
                                // ignore and send an ACK
                                ByteBuffer ackResponse = ByteBuffer.allocate(3);
                                ackResponse.put(Telnet.IAC).put(Telnet.WILL)
                                    .put(Telnet.TELOPT_TM);
                                ackResponse.flip();
                                try {
                                    Log.debug("Sending response to Telnet.TELOPT_TM -> [IAC WILL TELOPT_TM]", TelnetProtocolAdapter.class);
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
        if (hasTelnetCommand) {
            ByteBuffer copy = ByteBuffer.allocate(buffer.capacity());
            buffer.rewind();
            while (buffer.hasRemaining()) {
                byte content = buffer.get();
                if (!isTelnetCommand(content)) {
                    copy.put(content);
                }
            }

            buffer.clear();
            buffer.put(copy);
            buffer.flip();
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
        while (outBuffer.hasRemaining()) {
            byte currPosition = outBuffer.get();
            if (currPosition == escapeColor) {
                byte fgBg = outBuffer.hasRemaining() ? outBuffer.get() : 0;
                if (fgBg == escapeColorFg || fgBg == escapeColorBg || fgBg == escapeExtras) {
                    // we found an escape color sequence. Get the color specification and sum the colorCodeCount
                    byte colorCode = outBuffer.hasRemaining() ? outBuffer.get() : 0; // we do that to move the buffer pointer
                    colorCodeCount++;
                }
            }
        }

        if (colorCodeCount > 0) {

            int currentSize = outBuffer.limit();
            int capacity = outBuffer.capacity(); // maxBufferCapacity

            int newDesiredCapacity = currentSize + (7 * colorCodeCount);

            if (newDesiredCapacity > capacity) {
                // Buffer overflow.
                Log.error(
                    "Buffer overflow while adding color codes. "
                        + "Current capacity: " + currentSize
                        + "Desired capacity: " + newDesiredCapacity
                        + ", allocating more space on the buffer. ", this.getClass());
            }
            ByteBuffer tempBuffer = ByteBuffer.allocate(newDesiredCapacity);
            // once the tmpBuffer is allocated, it's already in reset state;

            // We must rewind the outBuffer
            outBuffer.rewind();
            while (outBuffer.hasRemaining()) {
                byte copyPosition = outBuffer.get();
                if (copyPosition == escapeColor) {
                    byte fgBg = outBuffer.hasRemaining() ? outBuffer.get() : 0;
                    if (fgBg == escapeColorFg || fgBg == escapeColorBg || fgBg == escapeExtras) {
                        // we found an escape color sequence. Get the color specification and sum the colorCodeCount
                        byte colorCode = outBuffer.hasRemaining() ? outBuffer.get() : 0; // TODO: review this logic about the 0 we do that to move the buffer pointer
                        // find the code in enum
                        byte[] colorCodeBytes = new byte[3];
                        colorCodeBytes[0] = (byte) '&';
                        colorCodeBytes[1] = fgBg;
                        colorCodeBytes[2] = colorCode;

                        String colorCodeString = new String(colorCodeBytes);
                        Color color = Color.findByCode(colorCodeString);

                        if (color != null) {
                            tempBuffer.put(color.getTelnetCode());
                        } else {
                            // Did not ind the color. Just put the escape chars
                            tempBuffer.put(colorCodeBytes);
                        }
                    } else {
                        tempBuffer.put(copyPosition);
                        if (fgBg != 0) {
                            tempBuffer.put(fgBg);
                        }
                    }
                } else {
                    tempBuffer.put(copyPosition);
                }

            }
            // We finished to write. Flip the buffer
            tempBuffer.flip();

            // Replace the outputBuffer content with the replaced version
            outBuffer.clear();

            outBuffer.put(tempBuffer);
            // We finished to write. So flip() the buffer;
            outBuffer.flip();
        }
    }
}
