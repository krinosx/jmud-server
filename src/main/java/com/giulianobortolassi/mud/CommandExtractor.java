package com.giulianobortolassi.mud;

import java.nio.ByteBuffer;

/**
 * CommandExtractor
 */
public interface CommandExtractor<T> {

    public T extract(ByteBuffer bufer);

}