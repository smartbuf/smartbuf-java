package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.context.model.ContextStruct;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.context.model.FrameMeta;
import com.github.sisyphsu.nakedata.io.InputReader;
import com.github.sisyphsu.nakedata.io.OutputWriter;

/**
 * 上下文同步工具, 封装序列化与反序列化的相关操作
 * 包括版本、结构体、类型等等
 *
 * @author sulin
 * @since 2019-05-03 17:17:07
 */
public class ContextUtils {

    private static final short NAME_EXPIRED = 1 << 1;
    private static final short NAME_ADDED   = 1 << 2;
    private static final short NAME_TEMP    = 1 << 3;

    private static final short STRUCT_EXPIRED = 1 << 4;
    private static final short STRUCT_ADDED   = 1 << 5;
    private static final short STRUCT_TEMP    = 1 << 6;

    private static final short TYPE_EXPIRED = 1 << 7;
    private static final short TYPE_ADDED   = 1 << 8;
    private static final short TYPE_TEMP    = 1 << 9;

    /**
     * 上下文增量版本输出
     *
     * @param writer 底层输出接口
     */
    public static void doWrite(OutputWriter writer, FrameMeta ver) {
        // 输出Flag
        int flag = (ver.getCxtNameExpired().isEmpty() ? 0 : NAME_EXPIRED)
            | (ver.getNameAdded().isEmpty() ? 0 : NAME_ADDED)
            | (ver.getNameTemp().isEmpty() ? 0 : NAME_TEMP)
            | (ver.getCxtStructExpired().isEmpty() ? 0 : STRUCT_EXPIRED)
            | (ver.getStructAdded().isEmpty() ? 0 : STRUCT_ADDED)
            | (ver.getStructTemp().isEmpty() ? 0 : STRUCT_TEMP);
        int version = ver.getVersion() % (1 << 22);
        writer.writeInt(flag | (version << 9));
        // 变量名
        if (!ver.getCxtNameExpired().isEmpty()) {
            writer.writeVarUint(ver.getCxtNameExpired().size());
            ver.getCxtNameExpired().forEach(writer::writeVarUint);
        }
        if (!ver.getNameAdded().isEmpty()) {
            writer.writeVarUint(ver.getNameAdded().size());
            ver.getNameAdded().forEach(writer::writeString);
        }
        if (!ver.getNameTemp().isEmpty()) {
            writer.writeVarUint(ver.getNameTemp().size());
            ver.getNameTemp().forEach(writer::writeString);
        }
        // 数据结构
        if (!ver.getCxtStructExpired().isEmpty()) {
            writer.writeVarUint(ver.getCxtStructExpired().size());
            ver.getCxtStructExpired().forEach(writer::writeVarUint);
        }
        if (!ver.getStructAdded().isEmpty()) {
            writer.writeVarUint(ver.getStructAdded().size());
            ver.getStructAdded().forEach(struct -> doWriteStruct(writer, struct));
        }
        if (!ver.getStructTemp().isEmpty()) {
            writer.writeVarUint(ver.getStructTemp().size());
            ver.getStructTemp().forEach(struct -> doWriteStruct(writer, struct));
        }
    }

    /**
     * 从输入流中读取ContextVersion信息
     *
     * @param reader 输入流接口
     */
    public static FrameMeta doRead(InputReader reader) {
        int head = reader.readInt32();
        FrameMeta version = new FrameMeta();
        version.setVersion(head >>> 9);
        // 读取Flag
        boolean nameExpiredFlag = (head & NAME_EXPIRED) != 0;
        boolean nameAddedFlag = (head & NAME_ADDED) != 0;
        boolean nameTempFlag = (head & NAME_TEMP) != 0;
        boolean structExpiredFlag = (head & STRUCT_EXPIRED) != 0;
        boolean structAddedFlag = (head & STRUCT_ADDED) != 0;
        boolean structTempFlag = (head & STRUCT_TEMP) != 0;
        boolean typeExpiredFlag = (head & TYPE_EXPIRED) != 0;
        boolean typeAddedFlag = (head & TYPE_ADDED) != 0;
        boolean typeTempFlag = (head & TYPE_TEMP) != 0;
        // 读取变量名
        if (nameExpiredFlag) {
            int size = (int) reader.readVarUint();
            for (int i = 0; i < size; i++) {
                version.getCxtNameExpired().add((int) reader.readVarUint());
            }
        }
        if (nameAddedFlag) {
            int size = (int) reader.readVarUint();
            for (int i = 0; i < size; i++) {
                version.getNameAdded().add(reader.readString());
            }
        }
        if (nameTempFlag) {
            int size = (int) reader.readVarUint();
            for (int i = 0; i < size; i++) {
                version.getNameTemp().add(reader.readString());
            }
        }
        // 读取数据结构
        if (structExpiredFlag) {
            int size = (int) reader.readVarUint();
            for (int i = 0; i < size; i++) {
                version.getCxtStructExpired().add((int) reader.readVarUint());
            }
        }
        if (structAddedFlag) {
            int size = (int) reader.readVarUint();
            for (int i = 0; i < size; i++) {
                version.getStructAdded().add(doReadStruct(reader));
            }
        }
        if (structTempFlag) {
            int size = (int) reader.readVarUint();
            for (int i = 0; i < size; i++) {
                version.getStructAdded().add(doReadStruct(reader));
            }
        }
        return version;
    }

    private static void doWriteStruct(OutputWriter writer, ContextStruct struct) {
        writer.writeVarUint(struct.getNameIds().length);
        for (int nameId : struct.getNameIds()) {
            writer.writeVarInt(nameId); // 可能是负数
        }
    }

    private static ContextStruct doReadStruct(InputReader reader) {
        int size = (int) reader.readVarUint();
        int[] nameIds = new int[size];
        for (int i = 0; i < size; i++) {
            nameIds[i] = (int) reader.readVarInt();
        }
        return new ContextStruct(nameIds);
    }

    private static void doWriteType(OutputWriter writer, ContextType type) {
        writer.writeVarInt(type.getStructId());
        writer.writeVarUint(type.getTypes().length);
        byte b = 0;
        for (int i = 0; i < type.getTypes().length; i++) {
            if (i % 2 == 0) {
                b = (byte) i;
            } else {
                b |= (byte) (i << 4);
            }
            if (i == type.getTypes().length - 1 || i % 2 == 1) {
                writer.writeByte(b);
            }
        }
    }

    private static ContextType doReadType(InputReader reader) {
        int structId = (int) reader.readVarInt();
        int size = (int) reader.readVarUint();
        int[] types = new int[size];
        for (int i = 0; i < (size + 1) / 2; i++) {
            int b = reader.readByte();
            types[i * 2] = b & 0xF;
            types[i * 2 + 1] = b & 0xF;
        }
        return new ContextType(structId, types);
    }

}
