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

    private int metaCount;
    private int metaStatus;

    /**
     * 将当前FrameMeta通过writer序列化输出
     *
     * @param writer 序列化输出
     */
    public void write(OutputWriter writer) {
        this.preStatus();
        byte head = VERSION;
        if (enableCxt)
            head |= FLAG_STREAM;
        if (metaStatus == 0)
            head |= FLAG_HEAD;
        writer.writeByte(head);
        writer.writeByte((byte) version);
        this.writeBody(writer);
    }

    /**
     * Output body of metadata.
     */
    private void writeBody(OutputWriter writer) {
        // 按照固定顺序依次输出各个分区
    }

    /**
     * Prepare metadata's status.
     */
    private void preStatus() {
        if (!tmpFloatArea.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_FLOAT;
        }
        if (!tmpDoubleArea.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_DOUBLE;
        }
        if (!tmpVarintArea.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_VARINT;
        }
        if (!tmpStringArea.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_STRING;
        }
        if (!tmpNames.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_NAMES;
        }
        if (!tmpStructs.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_STRUCTS;
        }
        if (!cxtNameAdded.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_NAME_ADDED;
        }
        if (!cxtNameExpired.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_NAME_EXPIRED;
        }
        if (!cxtStructAdded.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_STRUCT_ADDED;
        }
        if (!cxtStructExpired.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_STRUCT_EXPIRED;
        }
        if (!cxtSymbolAdded.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_SYMBOL_ADDED;
        }
        if (!cxtSymbolExpired.isEmpty()) {
            metaCount++;
            metaStatus |= 1 << CODE_SYMBOL_EXPIRED;
        }
    }

}
