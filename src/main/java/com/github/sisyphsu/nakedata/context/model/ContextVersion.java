package com.github.sisyphsu.nakedata.context.model;

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
public class ContextVersion {

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
     * 新增的变量名
     */
    private List<String> nameAdded = new ArrayList<>();
    /**
     * 新增的数据框架
     */
    private List<ContextStruct> structAdded = new ArrayList<>();
    /**
     * 临时使用的变量名
     */
    private List<String> nameTemp = new ArrayList<>();
    /**
     * 临时使用的数据框架
     */
    private List<ContextStruct> structTemp = new ArrayList<>();



    /**
     * 重置Log, 用于支持复用
     */
    public ContextVersion reset() {
        this.version = 0;
        this.nameExpired.clear();
        this.structExpired.clear();
        this.nameAdded.clear();
        this.structAdded.clear();
        this.nameTemp.clear();
        this.structTemp.clear();
        return this;
    }

    /**
     * 是否为空
     *
     * @return true表示空
     */
    public boolean isEmpty() {
        if (!nameExpired.isEmpty()) {
            return false;
        }
        if (!structExpired.isEmpty()) {
            return false;
        }
        if (!nameAdded.isEmpty()) {
            return false;
        }
        if (!structAdded.isEmpty()) {
            return false;
        }
        if (!nameTemp.isEmpty()) {
            return false;
        }
        if (!structTemp.isEmpty()) {
            return false;
        }

        return true;
    }

}
