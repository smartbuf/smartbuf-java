package com.github.sisyphsu.nakedata.node;

import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Node与Object的映射器, 负责数据解析与转换
 *
 * @author sulin
 * @since 2019-05-09 20:35:25
 */
public class NodeMapper {

    private Map<Class, NodeAdapter> adapterMap = new HashMap<>();

    /**
     * 将obj转换为NodeTree
     *
     * @param obj 原始对象
     * @return 根节点
     */
    public static Node convertNodeTree(Object obj) {
        BeanMap map = BeanMap.create(obj);
        return null;
    }

}
