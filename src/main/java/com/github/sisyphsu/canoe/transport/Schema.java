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
    public void read(InputReader reader) throws IOException {
        this.head = reader.readByte();
        this.stream = (head & VER_STREAM) != 0;
        this.hasTmpMeta = (head & VER_TMP_META) != 0;
        this.hasCxtMeta = (head & VER_CXT_META) != 0;
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
    private void readTmpMeta(InputReader reader) throws IOException {
        boolean hasMore = true;
        byte flag;
        while (hasMore) {
            long head = reader.readVarUint();
            int size = (int) (head >> 4);
            hasMore = (head & 0b0000_0001) == 1;
            flag = (byte) ((head >>> 1) & 0b0000_0111);
            switch (flag) {
                case TMP_FLOAT:
                    for (int i = 0; i < size; i++) {
                        tmpFloats.add(reader.readFloat());
                    }
                    break;
                case TMP_DOUBLE:
                    for (int i = 0; i < size; i++) {
                        tmpDoubles.add(reader.readDouble());
                    }
                    break;
                case TMP_VARINT:
                    for (int i = 0; i < size; i++) {
                        tmpVarints.add(reader.readVarInt());
                    }
                    break;
                case TMP_STRING:
                    for (int i = 0; i < size; i++) {
                        tmpStrings.add(reader.readString());
                    }
                    break;
                case TMP_NAMES:
                    for (int i = 0; i < size; i++) {
                        tmpNames.add(reader.readString());
                    }
                    break;
                case TMP_STRUCTS:
                    for (int i = 0; i < size; i++) {
                        int nameCount = (int) reader.readVarUint();
                        int[] nameIds = new int[nameCount];
                        for (int j = 0; j < nameCount; j++) {
                            nameIds[j] = (int) reader.readVarUint();
                        }
                        tmpStructs.add(nameIds);
                    }
                    break;
                default:
                    throw new RuntimeException("invalid flag: " + flag);
            }
        }
    }

    /**
     * Read context metadata from the specified reader
     */
    private void readCxtMeta(InputReader reader) throws IOException {
        boolean hasMore = true;
        while (hasMore) {
            long head = reader.readVarUint();
            int size = (int) (head >>> 4);
            hasMore = (head & 0b0000_0001) == 1;
            byte flag = (byte) ((head >>> 1) & 0b0000_0111);
            switch (flag) {
                case CXT_NAME_ADDED:
                    for (int i = 0; i < size; i++) {
                        cxtNameAdded.add(reader.readString());
                    }
                    break;
                case CXT_NAME_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        cxtNameExpired.add((int) reader.readVarUint());
                    }
                    break;
                case CXT_STRUCT_ADDED:
                    for (int i = 0; i < size; i++) {
                        int nameCount = (int) reader.readVarUint();
                        int[] nameIds = new int[nameCount];
                        for (int j = 0; j < nameCount; j++) {
                            nameIds[j] = (int) reader.readVarUint();
                        }
                        cxtStructAdded.add(nameIds);
                    }
                    break;
                case CXT_STRUCT_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        cxtStructExpired.add((int) reader.readVarUint());
                    }
                    break;
                case CXT_SYMBOL_ADDED:
                    for (int i = 0; i < size; i++) {
                        cxtSymbolAdded.add(reader.readString());
                    }
                    break;
                case CXT_SYMBOL_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        cxtSymbolExpired.add((int) reader.readVarUint());
                    }
                    break;
                default:
                    throw new RuntimeException("invalid flag: " + flag);
            }
        }
    }

}
