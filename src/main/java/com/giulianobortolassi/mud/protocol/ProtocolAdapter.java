package com.giulianobortolassi.mud.protocol;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ProtocolAdapter
 */
public interface ProtocolAdapter {

	void handleInput(ByteBuffer inBuffer, SocketChannel socket);

    void handleOutput(ByteBuffer inBuffer, SocketChannel socket);

    
}