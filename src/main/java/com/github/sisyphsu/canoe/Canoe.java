package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.convertor.CodecFactory;
import com.github.sisyphsu.canoe.exception.CanoeClosedException;
import com.github.sisyphsu.canoe.node.NodeCodec;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.reflect.TypeRef;
import com.github.sisyphsu.canoe.transport.Input;
import com.github.sisyphsu.canoe.transport.Output;

import java.io.IOException;

/**
 * Canoe wraps the "canoe" protocol, includes packet-mode and stream-mode.
 * <p>
 * In most cases, you can use {@link CanoePacket} and {@link CanoeStream} directly.
 * <p>
 * Or, you could customized {@link IOReader} and {@link IOWriter}, which could be used to initialize Canoe.
 * In this way, you might reduce memory copy and improve performance.
 *
 * @author sulin
 * @since 2019-10-28 15:13:24
 */
public final class Canoe {

    public static int PACKET_LIMIT = 1024 * 1024 * 64;

    public static final CodecFactory CODEC = new CodecFactory();

    static {
        CODEC.installCodec(NodeCodec.class);
    }

    final IOReader reader;
    final IOWriter writer;
    Input  input;
    Output output;

    private volatile boolean closed;

    /**
     * Initialize Canoe instance, supports packet-mode and stream-mode
     *
     * @param enableStreamMode Enable stream-mode or not
     * @param reader           The underlying reader
     * @param writer           The underlying writer
     */
    public Canoe(boolean enableStreamMode, IOReader reader, IOWriter writer) {
        this.reader = reader;
        this.writer = writer;
//        this.input = new Input(enableStreamMode);
//        this.output = new Output( enableStreamMode);
    }

    /**
     * Read the next object and convert it into the specified type.
     * It will try read data from {@link IOReader}
     *
     * @param tRef The specified type, support generic type
     * @param <T>  Generic Type
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    @SuppressWarnings("unchecked")
    public <T> T read(TypeRef<T> tRef) throws IOException {
        if (closed) {
            throw new CanoeClosedException("Canoe is closed");
        }
        Object obj = input.read(null);
        return (T) CODEC.convert(obj, tRef.getType());
    }

    /**
     * Read the next object and convert it into the specified class.
     * It will try read data from {@link IOReader}
     *
     * @param tCls The specified class
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(Class<T> tCls) throws IOException {
        if (closed) {
            throw new CanoeClosedException("Canoe is closed");
        }
        Object obj = input.read(null);
        return CODEC.convert(obj, tCls);
    }

    /**
     * Write the specified object into {@link IOWriter}
     *
     * @param obj The specified object
     * @throws IOException if an I/O error occurs.
     */
    public void write(Object obj) throws IOException {
        if (closed) {
            throw new CanoeClosedException("Canoe is closed");
        }
        Node node = CODEC.convert(obj, Node.class);
        output.write(node);
    }

    /**
     * Close this canoe instance, and release all resources
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        reader.close();
        writer.close();
        this.input = null;
        this.output = null;
        this.closed = true;
    }

}
