package com.github.sisyphsu.datube.proto;

import java.io.IOException;

import static com.github.sisyphsu.datube.proto.Const.*;

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

    /**
     * Output this schema into the specified writer with the specified sequence
     *
     * @param writer Underlying OutputWriter
     */
    public void output(OutputWriter writer) throws IOException {
        int cxtCount = 0, tmpCount = 0;
        if (tmpFloats.size() > 0) tmpCount++;
        if (tmpDoubles.size() > 0) tmpCount++;
        if (tmpVarints.size() > 0) tmpCount++;
        if (tmpStrings.size() > 0) tmpCount++;
        if (tmpNames.size() > 0) tmpCount++;
        if (tmpStructs.size() > 0) tmpCount++;
        if (cxtNameAdded.size() > 0) cxtCount++;
        if (cxtNameExpired.size() > 0) cxtCount++;
        if (cxtStructAdded.size() > 0) cxtCount++;
        if (cxtStructExpired.size() > 0) cxtCount++;
        if (cxtSymbolAdded.size() > 0) cxtCount++;
        if (cxtSymbolExpired.size() > 0) cxtCount++;

        this.hasTmpMeta = tmpCount > 0;
        this.hasCxtMeta = cxtCount > 0;
        this.head = (byte) (VER | (stream ? VER_STREAM : 0) | (hasTmpMeta ? VER_TMP_META : 0) | (hasCxtMeta ? VER_CXT_META : 0));

        // 1-byte for summary
        writer.writeByte(head);
        // 1-byte for context sequence, optional
        if (stream && hasCxtMeta) {
            writer.writeByte(++this.sequence);
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
        if ((len = tmpFloats.size()) > 0) {
            writer.writeVarUint((len << 4) | (TMP_FLOAT << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeFloat(tmpFloats.get(i));
            }
        }
        if (count > 0 && (len = tmpDoubles.size()) > 0) {
            writer.writeVarUint((len << 4) | (TMP_DOUBLE << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeDouble(tmpDoubles.get(i));
            }
        }
        if (count > 0 && (len = tmpVarints.size()) > 0) {
            writer.writeVarUint((len << 4) | (TMP_VARINT << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeVarInt(tmpVarints.get(i));
            }
        }
        if (count > 0 && (len = tmpStrings.size()) > 0) {
            writer.writeVarUint((len << 4) | (TMP_STRING << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeString(tmpStrings.get(i));
            }
        }
        if (count > 0 && (len = tmpNames.size()) > 0) {
            writer.writeVarUint((len << 4) | (TMP_NAMES << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeString(tmpNames.get(i));
            }
        }
        if (count > 0) {
            len = tmpStructs.size();
            writer.writeVarUint((len << 4) | (TMP_STRUCTS << 1));
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
        if ((len = cxtNameAdded.size()) > 0) {
            writer.writeVarUint((len << 4) | (CXT_NAME_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeString(cxtNameAdded.get(i));
            }
        }
        if ((len = cxtNameExpired.size()) > 0) {
            writer.writeVarUint((len << 4) | (CXT_NAME_EXPIRED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(cxtNameExpired.get(i));
            }
        }
        if (count > 0 && (len = cxtStructAdded.size()) > 0) {
            writer.writeVarUint((len << 4) | (CXT_STRUCT_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                int[] nameIds = cxtStructAdded.get(i);
                writer.writeVarUint(nameIds.length);
                for (int nameId : nameIds) {
                    writer.writeVarUint(nameId);
                }
            }
        }
        if (count > 0 && (len = cxtStructExpired.size()) > 0) {
            writer.writeVarUint((len << 4) | (CXT_STRUCT_EXPIRED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(cxtStructExpired.get(i));
            }
        }
        if (count > 0 && (len = cxtSymbolAdded.size()) > 0) {
            writer.writeVarUint((len << 4) | (CXT_SYMBOL_ADDED << 1) | ((--count == 0) ? 0 : 1));
            for (int i = 0; i < len; i++) {
                writer.writeString(cxtSymbolAdded.get(i));
            }
        }
        if (count > 0) {
            len = cxtSymbolExpired.size();
            writer.writeVarUint((len << 4) | (CXT_SYMBOL_EXPIRED << 1));
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(cxtSymbolExpired.get(i));
            }
        }
    }

}
