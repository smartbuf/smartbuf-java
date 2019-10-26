package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.convertor.CodecFactory;
import com.github.sisyphsu.canoe.node.ArrayNodeCodec;
import com.github.sisyphsu.canoe.node.BasicNodeCodec;
import com.github.sisyphsu.canoe.node.BeanNodeCodec;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.reflect.TypeRef;
import com.github.sisyphsu.canoe.transport.Input;
import com.github.sisyphsu.canoe.transport.Output;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * CanoeUtils provides an easy way to use `canoe` in packet-mode.
 * <p>
 * In packet-mode, you can serialize any object into byte[],
 * and deserialize byte[] into the specified object directly,
 * the schema or metadata will attched at the beginning of byte[].
 * <p>
 * Compared to stream-mode, packet-mode is much easier to use,
 * and you don't need to care about the context of data stream,
 * which is used for sharing metadata in stream-mode.
 * <p>
 * But easy always has cost, packet-mode might be a little slower,
 * and the serialization result might be a bit larger.
 *
 * @author sulin
 * @since 2019-10-25 17:08:04
 */
public final class CanoePacket {

    public static int PACKET_LIMIT = 1024 * 1024 * 64;

    private static final ThreadLocal<ByteArrayReader> INPUT_LOCAL  = new ThreadLocal<>();
    private static final ThreadLocal<ByteArrayWriter> OUTPUT_LOCAL = new ThreadLocal<>();

    private static final CodecFactory CODEC = new CodecFactory();

    static {
        CODEC.installCodec(BeanNodeCodec.class);
        CODEC.installCodec(BasicNodeCodec.class);
        CODEC.installCodec(ArrayNodeCodec.class);
    }

    /**
     * Use packet-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayWriter writer = OUTPUT_LOCAL.get();
        if (writer == null) {
            writer = new ByteArrayWriter();
            writer.output = new Output(writer, false);
            OUTPUT_LOCAL.set(writer);
        } else {
            writer.reset();
        }
        Node node = CODEC.convert(obj, Node.class);
        writer.output.write(node);

        return writer.toByteArray();
    }

    /**
     * Use packet-mode to deserialize byte[] into an object of the specified class.
     *
     * @param data Binary data in packet-mode format
     * @param clz  The specified class to convert
     * @param <T>  Template of target class
     * @return Deserialization result
     */
    public static <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        Object obj = deserialize(data);
        return CODEC.convert(obj, clz);
    }

    /**
     * Use packet-mode to deserialize byte[] into an object of the specified type,
     * could used for supporting generic type.
     *
     * @param data Binary data in packet-mode format
     * @param ref  The specified type to convert
     * @param <T>  Template of target type
     * @return Deserialization result
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data, TypeRef<T> ref) throws IOException {
        Object obj = deserialize(data);
        Type type = ref.getType();
        return (T) CODEC.convert(obj, type);
    }

    public static Object deserialize(byte[] data) throws IOException {
        ByteArrayReader reader = INPUT_LOCAL.get();
        if (reader == null) {
            reader = new ByteArrayReader();
            reader.input = new Input(reader, false);
            INPUT_LOCAL.set(reader);
        }
        reader.reset(data);
        return reader.input.read();
    }

    /**
     * IOReader's byte[] implementation, used to wrap byte[] as IOReader for underlying Input
     */
    static class ByteArrayReader implements IOReader {
        private int    off;
        private byte[] data;

        private transient Input input;

        @Override
        public int read() throws IOException {
            if (off == data.length) {
                throw new EOFException();
            }
            return data[off++] & 0xFF;
        }

        void reset(byte[] data) {
            this.off = 0;
            this.data = data;
        }
    }

    /**
     * IOWriter's byte[] implementation, used to accept Output's serialization result
     */
    static class ByteArrayWriter implements IOWriter {
        private int      off;
        private byte[][] data = new byte[64][];

        private transient Output output;

        @Override
        public void write(byte b) throws IOException {
            if (off >= PACKET_LIMIT) {
                throw new EOFException();
            }
            int pos = off++;
            int sliceOff = pos / 1024;
            if (sliceOff >= data.length) {
                byte[][] newData = new byte[data.length * 2][];
                System.arraycopy(data, 0, newData, 0, data.length);
                this.data = newData;
            }
            if (data[sliceOff] == null) {
                data[sliceOff] = new byte[1024];
            }
            data[sliceOff][pos % 1024] = b;
        }

        byte[] toByteArray() {
            byte[] result = new byte[off];
            for (int i = 0; i < off; i += 1024) {
                byte[] slice = data[i / 1024];
                int sliceLen = Math.min(off - i, 1024);
                System.arraycopy(slice, 0, result, i, sliceLen);
            }
            return result;
        }

        void reset() {
            this.off = 0;
        }
    }

}
