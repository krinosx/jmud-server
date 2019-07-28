package com.giulianobortolassi.mud.protocol;

import java.nio.ByteBuffer;

import com.giulianobortolassi.mud.CommandExtractor;

/**
 * TelnetCommandExtractor
 */
public class TelnetCommandExtractor implements CommandExtractor<String> {


    @Override
    public String extract(ByteBuffer bufer) {
        /** At this point all protocol specifi input must have been removed
         * only game specific input must be in the buffer */    
        int bufferLength = bufer.limit();

        byte[] tmp = new byte[bufferLength];
        bufer.get(tmp);
        
        String command = new String(tmp);
        return command.substring(0, command.length()-2); // remove \r\n
	}    
}