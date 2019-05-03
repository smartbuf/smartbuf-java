package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextStruct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-05-02 20:32:10
 */
public class OutputStructPool {

    /**
     * ID池
     */
    private IDPool pool;

    private Map<String, ActiveRef<ContextStruct>> nameMap = new HashMap<>();

    private Node root;

    /**
     * 根据变量名列表获取结构ID
     *
     * @param names 变量名集合
     * @return ID
     */
    public int getStructID(Collection<String> names) {
        return 0;
    }

    
    public static class Node {
        /**
         * 变量名
         */
        private ContextStruct struct;
        /**
         * 下一级变量名
         */
        private Map<String, Node> next = new HashMap<>();
    }

}
