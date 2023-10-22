package com.jgameserver.server.protocol.telnet;

import java.nio.ByteBuffer;

import com.jgameserver.server.CommandExtractor;

/**
 * TelnetCommandExtractor
 */
public class TelnetCommandExtractor implements CommandExtractor<String> {


    @Override
    public String extract(ByteBuffer buffer) {
        /* At this point all protocol specific input must have been removed
         * only game specific input must be in the buffer */
        int bufferLength = buffer.limit();

        byte[] tmp = new byte[bufferLength];
        buffer.get(tmp);

        String command = new String(tmp);
        return command.substring(0, command.length()-2); // remove \r\n
	}
}
