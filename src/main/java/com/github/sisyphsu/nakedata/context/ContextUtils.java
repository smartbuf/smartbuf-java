package com.github.sisyphsu.nakedata.context;

/**
 * 上下文同步工具, 封装序列化与反序列化的相关操作
 * 包括版本、结构体、类型等等
 *
 * @author sulin
 * @since 2019-05-03 17:17:07
 */
public class ContextUtils {

    private static final byte NAME_EXPIRED = 1 << 1;
    private static final byte TYPE_EXPIRED = 1 << 2;
    private static final byte NAME_ADDED = 1 << 3;
    private static final byte TYPE_ADDED = 1 << 4;
    private static final byte TMP_NAME = 1 << 5;
    private static final byte TMP_TYPE = 1 << 6;

}
