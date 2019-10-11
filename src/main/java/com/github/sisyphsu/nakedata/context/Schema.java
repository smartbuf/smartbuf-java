package com.github.sisyphsu.nakedata.context;

import java.io.IOException;

import static com.github.sisyphsu.nakedata.context.Proto.*;

/**
 * @author sulin
 * @since 2019-10-10 21:48:38
 */
public final class Schema {

    private final boolean stream;

    private byte    version;
    private byte    sequence;
    private boolean hasTmpMeta;
    private boolean hasCxtMeta;

    final Array<Float>  tmpFloats  = new Array<>();
    final Array<Double> tmpDoubles = new Array<>();
    final Array<Long>   tmpVarints = new Array<>();
    final Array<String> tmpStrings = new Array<>();
    final Array<String> tmpNames   = new Array<>();
    final Array<int[]>  tmpStructs = new Array<>();

    final Array<String>  cxtSymbolAdded   = new Array<>();
    final Array<Integer> cxtSymbolExpired = new Array<>();
    final Array<String>  cxtNameAdded     = new Array<>();
    final Array<int[]>   cxtStructAdded   = new Array<>();
    final Array<Integer> cxtStructExpired = new Array<>();

    public Schema(boolean stream) {
        this.stream = stream;
        this.sequence = 0;
    }

    public void read(InputReader reader) throws IOException {
        if (((version = reader.readByte()) & VERSION) == 0) {
            throw new RuntimeException("unknown version");
        }
        if (this.stream == ((version & FLAG_STREAM) == 0)) {
            throw new RuntimeException("unknown version");
        }
        this.hasTmpMeta = (version & FLAG_TMP_META) != 0;
        this.hasCxtMeta = (version & FLAG_CXT_META) != 0;
        // only stream-mode needs sequence
        if (stream) {
            byte nextSeq = reader.readByte();
            if (nextSeq != this.sequence + 1) {
                throw new RuntimeException();
            }
            this.sequence = nextSeq;
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
        while (hasMore) {
            long head = reader.readVarInt();
            int size = (int) (head >> 4);
            hasMore = (head & 0b0000_0001) == 1;
            switch ((byte) ((head >>> 1) & 0b0000_0111)) {
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
                    throw new RuntimeException("invalid flag");
            }
        }
    }

    /**
     * Read context metadata from the specified reader
     */
    private void readCxtMeta(InputReader reader) throws IOException {
        boolean hasMore = true;
        while (hasMore) {
            long head = reader.readVarInt();
            int size = (int) (head >> 4);
            hasMore = (head & 0b0000_0001) == 1;
            switch ((byte) ((head >>> 1) & 0b0000_0111)) {
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
                case CXT_NAME_ADDED:
                    for (int i = 0; i < size; i++) {
                        cxtNameAdded.add(reader.readString());
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
                default:
                    throw new RuntimeException("invalid flag");
            }
        }
    }

    /**
     * Output this schema into the specified writer with the specified sequence, which is only for context-mode.
     *
     * @param writer Underlying OutputWriter
     */
    public void output(OutputWriter writer) throws IOException {
        int cxtCount = 0, tmpCount = 0;
        // prepare the first byte to identify metadata
        byte head = VERSION;
        if (stream) {
            head |= FLAG_STREAM;
            if (cxtNameAdded.size() > 0) cxtCount++;
            if (cxtStructAdded.size() > 0) cxtCount++;
            if (cxtStructExpired.size() > 0) cxtCount++;
            if (cxtSymbolAdded.size() > 0) cxtCount++;
            if (cxtSymbolExpired.size() > 0) cxtCount++;
            if (cxtCount > 0) head |= FLAG_CXT_META;
        }
        if (tmpFloats.size() > 0) tmpCount++;
        if (tmpDoubles.size() > 0) tmpCount++;
        if (tmpVarints.size() > 0) tmpCount++;
        if (tmpStrings.size() > 0) tmpCount++;
        if (tmpNames.size() > 0) tmpCount++;
        if (tmpStructs.size() > 0) tmpCount++;
        if (tmpCount > 0) head |= FLAG_TMP_META;

        // 1-byte for summary
        writer.writeByte(head);
        // 1-byte for context sequence, optional
        if (stream) {
            writer.writeByte(this.sequence++);
        }
        // output temporary metadata
        if (tmpCount > 0) {
            this.writeTmpMeta(writer, tmpCount);
        }
        // output context metadata
        if (cxtCount > 0) {
            this.writeCxtMeta(writer, cxtCount);
        }
    }

    /**
     * Write temporary metadata to the specified writer
     */
    private void writeTmpMeta(OutputWriter writer, int count) throws IOException {
        int len;
        if (count > 0 && (len = tmpFloats.size()) > 0) {
            writer.writeMetaHead(len, TMP_FLOAT, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeFloat(tmpFloats.get(i));
            }
        }
        if (count > 0 && (len = tmpDoubles.size()) > 0) {
            writer.writeMetaHead(len, TMP_DOUBLE, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeDouble(tmpDoubles.get(i));
            }
        }
        if (count > 0 && (len = tmpVarints.size()) > 0) {
            writer.writeMetaHead(len, TMP_VARINT, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarInt(tmpVarints.get(i));
            }
        }
        if (count > 0 && (len = tmpStrings.size()) > 0) {
            writer.writeMetaHead(len, TMP_STRING, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(tmpStrings.get(i));
            }
        }
        if (count > 0 && (len = tmpNames.size()) > 0) {
            writer.writeMetaHead(len, TMP_NAMES, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(tmpNames.get(i));
            }
        }
        if (count > 0 && (len = tmpStructs.size()) > 0) {
            writer.writeMetaHead(len, TMP_STRUCTS, --count == 0);
            for (int i = 0; i < len; i++) {
                int[] nameIds = tmpStructs.get(i);
                writer.writeVarUint(nameIds.length);
                for (int nameId : nameIds) {
                    writer.writeVarUint(nameId);
                }
            }
        }
    }

    /**
     * Write context metadata to the specified writer
     */
    private void writeCxtMeta(OutputWriter writer, int count) throws IOException {
        int len;
        if (count > 0 && (len = cxtNameAdded.size()) > 0) {
            writer.writeMetaHead(len, CXT_NAME_ADDED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(cxtNameAdded.get(i));
            }
        }
        if (count > 0 && (len = cxtStructAdded.size()) > 0) {
            writer.writeMetaHead(len, CXT_STRUCT_ADDED, --count == 0);
            for (int i = 0; i < len; i++) {
                int[] nameIds = cxtStructAdded.get(i);
                writer.writeVarUint(nameIds.length);
                for (int nameId : nameIds) {
                    writer.writeVarUint(nameId);
                }
            }
        }
        if (count > 0 && (len = cxtStructExpired.size()) > 0) {
            writer.writeMetaHead(len, CXT_STRUCT_EXPIRED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(cxtStructExpired.get(i));
            }
        }
        if (count > 0 && (len = cxtSymbolAdded.size()) > 0) {
            writer.writeMetaHead(len, CXT_SYMBOL_ADDED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(cxtSymbolAdded.get(i));
            }
        }
        if (count > 0 && (len = cxtSymbolExpired.size()) > 0) {
            writer.writeMetaHead(len, CXT_SYMBOL_EXPIRED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(cxtSymbolExpired.get(i));
            }
        }
    }

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
        this.cxtStructAdded.clear();
        this.cxtStructExpired.clear();
    }

}
