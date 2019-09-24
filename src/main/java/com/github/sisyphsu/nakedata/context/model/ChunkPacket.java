package com.github.sisyphsu.nakedata.context.model;

import com.github.sisyphsu.nakedata.node.Node;

import java.util.List;

/**
 * @author sulin
 * @since 2019-09-23 20:58:27
 */
public class ChunkPacket {

    private final int version = 0b00010000;
    private boolean hasData;

    private List<String> names;
    private List<String> structs;

    /**
     * data body of this packet.
     */
    private Node body;

}
