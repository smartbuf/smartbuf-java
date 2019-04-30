package com.github.sisyphsu.nakedata.protocol;

import com.github.sisyphsu.nakedata.io.OutputWriter;

import java.util.List;

/**
 * @author sulin
 * @since 2019-04-27 18:43:47
 */
public class Package {

    /**
     * Variable name of context, should be cached by otherside.
     * must match the specified variable role.
     */
    private List<String> cxtNameAdds;
    /**
     *
     */
    private List<String> cxtNameDels;
    /**
     * Variable data type, should be cached by otherside.
     */
    private List<TypeMeta> cxtTypeAdds;
    /**
     *
     */
    private List<TypeMeta> cxtTypeDels;

    /**
     * Temporary names, could be the keys of map/object.
     */
    private List<String> tempNames;
    /**
     * Temporary object type, could be map.
     */
    private List<TypeMeta> tempTypes;

    /**
     * Data body
     */
    private byte[] body;

    /**
     * 定义Output和Input接口？
     * 还是抽取独立的工具类？
     *
     * @param writer
     */
    public void output(OutputWriter writer) {

    }

}
