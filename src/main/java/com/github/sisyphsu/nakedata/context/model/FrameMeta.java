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

    private List<String> tmpNames;
    private List<int[]>  tmpStructs;

    private List<Float>  tmpFloatData;
    private List<Double> tmpDoubleData;
    private List<Long>   tmpVarintData;
    private List<String> tmpStringData;

    private List<String>  cxtNameAdded;
    private List<Integer> cxtNameExpired;
    private List<int[]>   cxtStructAdded;
    private List<Integer> cxtStructExpired;
    private List<String>  cxtSymbolAdded;
    private List<Integer> cxtSymbolExpired;

    public boolean isEmpty() {
        return true;
    }

}
