package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.node.Node;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author sulin
 * @since 2019-10-10 21:43:41
 */
public final class Input {

    private long sequence;

    private final boolean     stream;
    private final Schema      schema;
    private final InputReader reader;

    private final Array<String>   cxtSymbols = new Array<>();
    private final Array<String>   cxtNames   = new Array<>();
    private final IDAllocator     cxtIdAlloc = new IDAllocator();
    private final Array<String[]> cxtStructs = new Array<>();

    public Input(InputStream inputStream, boolean stream) {
        this.stream = stream;
        this.schema = new Schema(stream);
        this.reader = new InputReader(inputStream);
    }

    public Node read() throws IOException {
        schema.read(reader);
        // valid schema
        if (schema.stream != stream) {
            throw new RuntimeException("invalid mode");
        }
        if ((schema.sequence & 0xFF) != ((sequence + 1) & 0xFF)) {
            throw new RuntimeException("invalid sequence");
        }
        // sync context

        // load data

        return null;
    }

    public Object findDataByID(int dataId) {
        return null;
    }

    public String findNameByID(int nameId) {
        return null;
    }

    public String[] findStructByID(int structID) {
        return null;
    }

}
