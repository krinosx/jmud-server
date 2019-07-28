
package com.giulianobortolassi.mud.protocol;

import java.nio.ByteBuffer;

import com.giulianobortolassi.mud.CommandExtractor;

/**
 * StringCommandStractor
 */
public class StringCommandStractor implements CommandExtractor<String> {

    @Override
    public String extract(ByteBuffer bufer) {

        int bufferLength = bufer.limit();

        byte[] tmp = new byte[bufferLength];
        bufer.get(tmp);
        String command = new String(tmp);

        return command;
    }

}