package com.github.sisyphsu.nakedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import com.github.sisyphsu.nakedata.utils.JSONUtils;

/**
 * 1. scan object's type
 * 2. adjust namePool
 * 3. adjust typePool
 * 4. serialize data: name, type, content
 *
 * @author sulin
 * @since 2019-04-24 21:43:29
 */
public class NDataSerializer {

    private OutputWriter writer;

    public void serialize(Object obj) {
        JsonNode node = JSONUtils.toJsonNode(obj);
        // step1. pre-scan metadata
        this.scanMetadata(node);
        // step2. output head: has-cxt-meta(1b), has-tmp-meta(1b), body-type(4b)

        // step3. output metadata

        // step4. output body

        if (node != null) {
            switch (node.getNodeType()) {
                case NULL:
                case BOOLEAN:
                case NUMBER:
                case BINARY:
                case STRING:
                case ARRAY:
                case OBJECT:
                default:
            }
        } else {

        }
    }

    /**
     * 扫描元数据, 并执行更新
     *
     * @param data
     */
    public void scanMetadata(JsonNode data) {

    }

}
