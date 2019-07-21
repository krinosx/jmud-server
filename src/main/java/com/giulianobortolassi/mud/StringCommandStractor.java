
package com.giulianobortolassi.mud;

import java.nio.ByteBuffer;

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