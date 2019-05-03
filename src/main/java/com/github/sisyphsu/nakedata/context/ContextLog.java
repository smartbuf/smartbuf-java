package com.github.sisyphsu.nakedata.context;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 上下文日志, 用于Output与Input之间同步元数据变化信息
 *
 * @author sulin
 * @since 2019-05-03 17:43:58
 */
@Data
public class ContextLog {

    /**
     * 版本号ID
     */
    private int version;
    /**
     * 已过期的变量名
     */
    private List<Integer> nameExpired = new ArrayList<>();
    /**
     * 已过期的数据框架
     */
    private List<Integer> structExpired = new ArrayList<>();
    /**
     * 已过期的数据类型
     */
    private List<Integer> typeExpired = new ArrayList<>();
    /**
     * 新增的变量名
     */
    private List<ContextName> nameAdded = new ArrayList<>();
    /**
     * 新增的数据框架
     */
    private List<ContextStruct> structAdded = new ArrayList<>();
    /**
     * 新增的数据类型
     */
    private List<ContextType> typeAdded = new ArrayList<>();
    /**
     * 临时使用的变量名
     */
    private List<ContextName> nameTemp = new ArrayList<>();
    /**
     * 临时使用的数据框架
     */
    private List<ContextStruct> structTemp = new ArrayList<>();
    /**
     * 临时使用的数据类型
     */
    private List<ContextType> typeTemp = new ArrayList<>();

    /**
     * 重置Log, 用于支持复用
     */
    public void reset() {
        this.version = 0;
        this.nameExpired.clear();
        this.structExpired.clear();
        this.typeExpired.clear();
        this.nameAdded.clear();
        this.structAdded.clear();
        this.typeAdded.clear();
        this.nameTemp.clear();
        this.structTemp.clear();
        this.typeTemp.clear();
    }

}
