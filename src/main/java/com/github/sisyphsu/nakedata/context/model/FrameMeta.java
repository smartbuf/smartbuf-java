package com.github.sisyphsu.nakedata.context.model;

import lombok.Data;

import java.util.List;

/**
 * 上下文日志, 用于Output与Input之间同步元数据变化信息
 *
 * @author sulin
 * @since 2019-05-03 17:43:58
 */
@Data
public class FrameMeta {

    private long    id;
    private int     version;
    private boolean enableCxt;

    private List<String>  tmpNames;
    private List<String>  cxtNameAdded;
    private List<Integer> cxtNameExpired;

    private List<int[]>   tmpStructs;
    private List<int[]>   cxtStructAdded;
    private List<Integer> cxtStructExpired;

    private List<String>  cxtSymbolAdded;
    private List<Integer> cxtSymbolExpired;

    private List<Float>  floatData;
    private List<Double> doubleData;
    private List<Long>   varintData;
    private List<String> stringData;

    public boolean isEmpty() {
        if (!cxtNameExpired.isEmpty()) {
            return false;
        }
        if (!cxtStructExpired.isEmpty()) {
            return false;
        }
        return true;
    }

}
