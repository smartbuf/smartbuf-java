package com.github.sisyphsu.smartbuf.exception;

/**
 * NoShortestPipelineException indicates the {@link com.github.sisyphsu.smartbuf.converter.CodecFactory}
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
