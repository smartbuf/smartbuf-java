package com.github.sisyphsu.datube.exception;

/**
 * CircularReferenceException represents that the specified object has
 * circular reference problem, which causes it's no serializable.
 *
 * @author sulin
 * @since 2019-10-22 20:55:29
 */
public class CircleReferenceException extends RuntimeException {

    public CircleReferenceException() {
    }
    
}
