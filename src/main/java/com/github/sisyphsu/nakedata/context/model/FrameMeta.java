package com.github.sisyphsu.nakedata.context.model;

import lombok.Data;

import java.util.List;

/**
 * 报文元数据，在数据包的头部声明报文体的数据模型、数据区等
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
