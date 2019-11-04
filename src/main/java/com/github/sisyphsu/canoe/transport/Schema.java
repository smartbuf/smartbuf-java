package com.github.sisyphsu.canoe.transport;

import java.io.IOException;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * Schema represents data's meta info, used for discribe and explain data area
 *
 * @author sulin
 * @since 2019-10-10 21:48:38
 */
public final class Schema {

    byte    head;
    byte    sequence;
    boolean stream;
    boolean hasTmpMeta;
    boolean hasCxtMeta;

    final Array<Float>  tmpFloats  = new Array<>();
    final Array<Double> tmpDoubles = new Array<>();
    final Array<Long>   tmpVarints = new Array<>();
    final Array<String> tmpStrings = new Array<>();
    final Array<String> tmpNames   = new Array<>();
    final Array<int[]>  tmpStructs = new Array<>();

    final Array<String>  cxtSymbolAdded   = new Array<>();
    final Array<Integer> cxtSymbolExpired = new Array<>();
    final Array<String>  cxtNameAdded     = new Array<>();
    final Array<Integer> cxtNameExpired   = new Array<>();
    final Array<int[]>   cxtStructAdded   = new Array<>();
    final Array<Integer> cxtStructExpired = new Array<>();

    /**
     * Initialize Schema
     *
     * @param stream Enable stream-mode or not
     */
    public Schema(boolean stream) {
        this.stream = stream;
        this.sequence = 0;
    }

    /**
     * Reset scheme for next round's reuse
     */
    public void reset() {
        this.tmpFloats.clear();
        this.tmpDoubles.clear();
        this.tmpVarints.clear();
        this.tmpStrings.clear();
        this.tmpNames.clear();
        this.tmpStructs.clear();
        this.cxtSymbolAdded.clear();
        this.cxtSymbolExpired.clear();
        this.cxtNameAdded.clear();
        this.cxtNameExpired.clear();
        this.cxtStructAdded.clear();
        this.cxtStructExpired.clear();
    }

    /**
     * Read schema info into this instance from the specified InputReader
     *
     * @param reader Underlying OutputReader
     */
    public void read(InputBuffer reader) throws IOException {
        this.head = reader.readByte();
        this.stream = (head & VER_STREAM) != 0;
        this.hasTmpMeta = (head & VER_HAS_DATA) != 0;
        this.hasCxtMeta = (head & VER_HAS_META) != 0;
        // only stream-mode needs sequence
        if (stream && hasCxtMeta) {
            this.sequence = reader.readByte();
        }
        // read temporary metadata
        if (hasTmpMeta) {
            this.readTmpMeta(reader);
        }
        // read context metadata
        if (hasCxtMeta) {
            this.readCxtMeta(reader);
        }
    }

    /**
     * Read temporary metadata from the specified Reader
     */
    private void readTmpMeta(InputBuffer reader) throws IOException {
    }

    /**
     * Read context metadata from the specified reader
     */
    private void readCxtMeta(InputBuffer reader) throws IOException {
    }

}
