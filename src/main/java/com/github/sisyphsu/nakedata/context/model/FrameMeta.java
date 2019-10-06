package com.github.sisyphsu.nakedata.context.model;

import com.github.sisyphsu.nakedata.common.Array;
import com.github.sisyphsu.nakedata.common.DoubleArray;
import com.github.sisyphsu.nakedata.common.FloatArray;
import com.github.sisyphsu.nakedata.common.LongArray;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import lombok.Getter;

/**
 * 报文元数据，在数据包的头部声明报文体的数据模型、数据区等
 *
 * @author sulin
 * @since 2019-05-03 17:43:58
 */
@Getter
public final class FrameMeta {

    private static final byte VERSION     = (byte) 0b0100_0000;
    private static final byte FLAG_STREAM = (byte) 0b0010_0000;
    private static final byte FLAG_HEAD   = (byte) 0b0001_0000;

    private static final byte CODE_FLOAT          = (byte) 1;
    private static final byte CODE_DOUBLE         = (byte) 2;
    private static final byte CODE_VARINT         = (byte) 3;
    private static final byte CODE_STRING         = (byte) 4;
    private static final byte CODE_NAMES          = (byte) 5;
    private static final byte CODE_STRUCTS        = (byte) 6;
    private static final byte CODE_NAME_ADDED     = (byte) 7;
    private static final byte CODE_NAME_EXPIRED   = (byte) 8;
    private static final byte CODE_STRUCT_ADDED   = (byte) 9;
    private static final byte CODE_STRUCT_EXPIRED = (byte) 10;
    private static final byte CODE_SYMBOL_ADDED   = (byte) 11;
    private static final byte CODE_SYMBOL_EXPIRED = (byte) 12;

    private final boolean output;
    private final boolean enableCxt;

    private final FloatArray    tmpFloatArea;
    private final DoubleArray   tmpDoubleArea;
    private final LongArray     tmpVarintArea;
    private final Array<String> tmpStringArea;

    private final Array<String> tmpNames;
    private final Array<int[]>  tmpStructs;

    private final Array<String> cxtNameAdded;
    private final Array<int[]>  cxtStructAdded;
    private final Array<String> cxtSymbolAdded;

    private final LongArray cxtNameExpired;
    private final LongArray cxtStructExpired;
    private final LongArray cxtSymbolExpired;

    private int version;

    /**
     * Initialize frame's metadata, it should be reused for every context.
     *
     * @param output Used for output side or not
     */
    public FrameMeta(boolean output, boolean enableCxt) {
        this.output = output;
        this.enableCxt = enableCxt;
        this.tmpFloatArea = new FloatArray(output);
        this.tmpDoubleArea = new DoubleArray(output);
        this.tmpVarintArea = new LongArray(output);
        this.tmpStringArea = new Array<>(output);
        this.tmpNames = new Array<>(output);
        this.tmpStructs = new Array<>(output);
        this.cxtNameAdded = new Array<>(output);
        this.cxtStructAdded = new Array<>(output);
        this.cxtSymbolAdded = new Array<>(output);
        this.cxtNameExpired = new LongArray(output);
        this.cxtStructExpired = new LongArray(output);
        this.cxtSymbolExpired = new LongArray(output);
    }

    public void reset(int version) {
        this.version = version;
        this.tmpFloatArea.clear();
        this.tmpDoubleArea.clear();
        this.tmpVarintArea.clear();
        this.tmpStringArea.clear();
        this.tmpNames.clear();
        this.tmpStructs.clear();
        if (enableCxt) {
            this.cxtSymbolAdded.clear();
            this.cxtNameAdded.clear();
            this.cxtStructAdded.clear();
            this.cxtSymbolExpired.clear();
            this.cxtNameExpired.clear();
            this.cxtStructExpired.clear();
        }
    }

    /**
     * 将当前FrameMeta通过writer序列化输出
     *
     * @param writer 序列化输出
     */
    public void write(OutputWriter writer) {
        int cxtCount = 0;
        if (enableCxt) {
            if (cxtNameAdded.size() > 0) cxtCount++;
            if (cxtNameExpired.size() > 0) cxtCount++;
            if (cxtStructAdded.size() > 0) cxtCount++;
            if (cxtStructExpired.size() > 0) cxtCount++;
            if (cxtSymbolAdded.size() > 0) cxtCount++;
            if (cxtSymbolExpired.size() > 0) cxtCount++;
        }
        int count = cxtCount;
        if (tmpFloatArea.size() > 0) count++;
        if (tmpDoubleArea.size() > 0) count++;
        if (tmpVarintArea.size() > 0) count++;
        if (tmpStringArea.size() > 0) count++;
        if (tmpNames.size() > 0) count++;
        if (tmpStructs.size() > 0) count++;

        byte head = VERSION;
        if (enableCxt) {
            head |= FLAG_STREAM;
        }
        if (count == 0) {
            head |= FLAG_HEAD;
        }
        writer.writeByte(head);
        writer.writeByte((byte) version);
        if (count > 0) {
            this.writeTmpMeta(writer, count);
        }
        if (enableCxt && cxtCount > 0) {
            this.writeCxtMeta(writer, cxtCount);
        }
    }

    /**
     * Output body of metadata.
     */
    private void writeTmpMeta(OutputWriter writer, int count) {
        if (tmpFloatArea.size() > 0) {
            this.writeMetaHead(writer, tmpFloatArea.size(), CODE_FLOAT, --count == 0);
            writer.writeFloatArray(tmpFloatArea.data());
        }
        if (tmpDoubleArea.size() > 0) {
            this.writeMetaHead(writer, tmpDoubleArea.size(), CODE_DOUBLE, --count == 0);
            writer.writeDoubleArray(tmpDoubleArea.data());
        }
        if (tmpVarintArea.size() > 0) {
            this.writeMetaHead(writer, tmpVarintArea.size(), CODE_VARINT, --count == 0);
            writer.writeLongArray(tmpVarintArea.data());
        }
        if (tmpStringArea.size() > 0) {
            this.writeMetaHead(writer, tmpStringArea.size(), CODE_STRING, --count == 0);
            writer.writeStringArray(tmpStringArea.data());
        }
        if (tmpNames.size() > 0) {
            this.writeMetaHead(writer, tmpNames.size(), CODE_NAMES, --count == 0);
            writer.writeStringArray(tmpNames.data());
        }
        if (tmpStructs.size() > 0) {
            this.writeMetaHead(writer, tmpStructs.size(), CODE_STRING, --count == 0);
            for (int i = 0; i < tmpStructs.size(); i++) {
                int[] ints = tmpStructs.get(i);
                writer.writeVarUint(ints.length);
                writer.writeIntArray(ints);
            }
        }
    }

    /**
     * Output context metadata
     */
    private void writeCxtMeta(OutputWriter writer, int count) {
        if (cxtNameAdded.size() > 0) {
            writeMetaHead(writer, cxtNameAdded.size(), CODE_NAME_ADDED, --count == 0);
            writer.writeStringArray(cxtNameAdded.data());
        }
        if (cxtNameExpired.size() > 0) {
            this.writeMetaHead(writer, cxtNameExpired.size(), CODE_NAME_EXPIRED, --count == 0);
            writer.writeLongArray(cxtNameExpired.data());
        }
        if (cxtStructAdded.size() > 0) {
            this.writeMetaHead(writer, cxtStructAdded.size(), CODE_STRUCT_ADDED, --count == 0);
            for (int i = 0; i < cxtStructAdded.size(); i++) {
                int[] ints = cxtStructAdded.get(i);
                writer.writeVarUint(ints.length);
                writer.writeIntArray(ints);
            }
        }
        if (cxtStructExpired.size() > 0) {
            this.writeMetaHead(writer, cxtStructExpired.size(), CODE_STRUCT_EXPIRED, --count == 0);
            writer.writeLongArray(cxtStructExpired.data());
        }
        if (cxtSymbolAdded.size() > 0) {
            this.writeMetaHead(writer, cxtSymbolAdded.size(), CODE_SYMBOL_ADDED, --count == 0);
            writer.writeStringArray(cxtSymbolAdded.data());
        }
        if (cxtSymbolExpired.size() > 0) {
            this.writeMetaHead(writer, cxtSymbolExpired.size(), CODE_SYMBOL_EXPIRED, --count == 0);
            writer.writeLongArray(cxtSymbolExpired.data());
        }
    }

    /**
     * Output the head of one schema area.
     */
    private void writeMetaHead(OutputWriter writer, long size, int code, boolean hasMore) {
        writer.writeVarUint((size << 5) | (code << 1) | (hasMore ? 1 : 0));
    }

}
