package com.github.smartbuf.exception;

import com.github.smartbuf.converter.CodecFactory;

/**
 * NoShortestPipelineException indicates the {@link CodecFactory}
 * can't decide the shortest codec-pipeline to use.
 *
 * @author sulin
 * @since 2019-11-11 21:47:21
 */
public class NoShortestPipelineException extends RuntimeException {

    public NoShortestPipelineException(String message) {
        super(message);
    }

}
