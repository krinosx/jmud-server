package com.jgameserver.server;

import java.nio.ByteBuffer;

/**
 * CommandExtractor
 */
public interface CommandExtractor<T> {

    T extract(ByteBuffer buffer);

}
