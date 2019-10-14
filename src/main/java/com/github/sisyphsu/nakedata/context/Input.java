package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.node.Node;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.github.sisyphsu.nakedata.context.Proto.*;

/**
 * @author sulin
 * @since 2019-10-10 21:43:41
 */
public final class Input {

    private long sequence;

    private final boolean      stream;
    private final Schema       schema;
    private final InputReader  reader;
    private final InputContext context;

    public Input(InputStream inputStream, boolean stream) {
        this.stream = stream;
        this.schema = new Schema(stream);
        this.reader = new InputReader(inputStream);
        this.context = new InputContext(schema);
    }

    public Node read() throws IOException {
        schema.reset();
        schema.read(reader);
        // valid schema
        if (schema.stream != stream) {
            throw new RuntimeException("invalid mode");
        }
        if (schema.hasCxtMeta) {
            if ((schema.sequence & 0xFF) != ((sequence + 1) & 0xFF)) {
                throw new RuntimeException("invalid sequence");
            }
            this.sequence++;
        }
        // sync context
        this.context.sync();

        // load data

        return null;
    }

    private Object readNode() throws IOException {
        long nodeID = reader.readVarUint();
        byte flag = (byte) (nodeID & 0b0000_0011);
        nodeID = nodeID >>> 2;
        switch (flag) {
            case BODY_FLAG_DATA:
                return context.findDataByID((int) nodeID);
            case BODY_FLAG_ARRAY:
                return this.readArray(nodeID >>> 2);
            case BODY_FLAG_STRUCT:
                String[] fields = context.findStructByID((int) nodeID);
                Map<String, Object> tmp = new HashMap<>();
                for (String field : fields) {
                    tmp.put(field, readNode());
                }
                return tmp;
            default:
                throw new RuntimeException("");
        }
    }

    private Object readArray(long head) throws IOException {
        return null;
    }

}
