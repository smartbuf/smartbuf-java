package com.github.sisyphsu.nakedata.context.model;

import com.github.sisyphsu.nakedata.io.OutputWriter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 报文元数据，在数据包的头部声明报文体的数据模型、数据区等
 *
 * @author sulin
 * @since 2019-05-03 17:43:58
 */
@Data
public class FrameMeta {

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

    private long    id;
    private int     version;
    private boolean enableCxt;

    private List<Float>  tmpFloatArea  = new ArrayList<>();
    private List<Double> tmpDoubleArea = new ArrayList<>();
    private List<Long>   tmpVarintArea = new ArrayList<>();
    private List<String> tmpStringArea = new ArrayList<>();

    private List<String> tmpNames   = new ArrayList<>();
    private List<int[]>  tmpStructs = new ArrayList<>();

    private List<String>  cxtNameAdded     = new ArrayList<>();
    private List<Integer> cxtNameExpired   = new ArrayList<>();
    private List<int[]>   cxtStructAdded   = new ArrayList<>();
    private List<Integer> cxtStructExpired = new ArrayList<>();
    private List<String>  cxtSymbolAdded   = new ArrayList<>();
    private List<Integer> cxtSymbolExpired = new ArrayList<>();

    /**
     * 将当前FrameMeta通过writer序列化输出
     *
     * @param writer 序列化输出
     */
    public void write(OutputWriter writer) {
        int count = 0;
        if (!tmpFloatArea.isEmpty()) count++;
        if (!tmpDoubleArea.isEmpty()) count++;
        if (!tmpVarintArea.isEmpty()) count++;
        if (!tmpStringArea.isEmpty()) count++;
        if (!tmpNames.isEmpty()) count++;
        if (!tmpStructs.isEmpty()) count++;
        if (!cxtNameAdded.isEmpty()) count++;
        if (!cxtNameExpired.isEmpty()) count++;
        if (!cxtStructAdded.isEmpty()) count++;
        if (!cxtStructExpired.isEmpty()) count++;
        if (!cxtSymbolAdded.isEmpty()) count++;
        if (!cxtSymbolExpired.isEmpty()) count++;

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
            count = this.writeTmpMeta(writer, count);
        }
        if (count > 0 && enableCxt) {
            this.writeCxtMeta(writer, count);
        }
    }

    private void writeCxtMeta(OutputWriter writer, int count) {
        if (!cxtNameAdded.isEmpty()) {
            this.writeMetaHead(writer, cxtNameAdded.size(), CODE_NAME_ADDED, --count == 0);
            // TODO output list
        }
        if (!cxtNameExpired.isEmpty()) {
            this.writeMetaHead(writer, cxtNameExpired.size(), CODE_NAME_EXPIRED, --count == 0);
            // TODO output list
        }
        if (!cxtStructAdded.isEmpty()) {
            this.writeMetaHead(writer, cxtStructAdded.size(), CODE_STRUCT_ADDED, --count == 0);
            // TODO output list
        }
        if (!cxtStructExpired.isEmpty()) {
            this.writeMetaHead(writer, cxtStructExpired.size(), CODE_STRUCT_EXPIRED, --count == 0);
            // TODO output list
        }
        if (!cxtSymbolAdded.isEmpty()) {
            this.writeMetaHead(writer, cxtSymbolAdded.size(), CODE_SYMBOL_ADDED, --count == 0);
            // TODO output list
        }
        if (!cxtSymbolExpired.isEmpty()) {
            this.writeMetaHead(writer, cxtSymbolExpired.size(), CODE_SYMBOL_EXPIRED, --count == 0);
            // TODO output list
        }
    }

    /**
     * Output body of metadata.
     */
    private int writeTmpMeta(OutputWriter writer, int count) {
        if (!tmpFloatArea.isEmpty()) {
            this.writeMetaHead(writer, tmpFloatArea.size(), CODE_FLOAT, --count == 0);
            // TODO output list
        }
        if (!tmpDoubleArea.isEmpty()) {
            this.writeMetaHead(writer, tmpDoubleArea.size(), CODE_DOUBLE, --count == 0);
            // TODO output list
        }
        if (!tmpVarintArea.isEmpty()) {
            this.writeMetaHead(writer, tmpVarintArea.size(), CODE_VARINT, --count == 0);
            // TODO output list
        }
        if (!tmpStringArea.isEmpty()) {
            this.writeMetaHead(writer, tmpStringArea.size(), CODE_STRING, --count == 0);
            // TODO output list
        }
        if (!tmpNames.isEmpty()) {
            this.writeMetaHead(writer, tmpNames.size(), CODE_NAMES, --count == 0);
            // TODO output list
        }
        if (!tmpStructs.isEmpty()) {
            this.writeMetaHead(writer, tmpStructs.size(), CODE_STRING, --count == 0);
            // TODO output list
        }
        return count;
    }

    /**
     * Output the head of one schema area.
     */
    private void writeMetaHead(OutputWriter writer, long size, int code, boolean hasMore) {
        writer.writeVarUint((size << 5) | (code << 1) | (hasMore ? 1 : 0));
    }

}
