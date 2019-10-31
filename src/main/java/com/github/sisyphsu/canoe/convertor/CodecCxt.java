package com.github.sisyphsu.canoe.convertor;

import com.github.sisyphsu.canoe.reflect.XType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author sulin
 * @since 2019-10-31 18:00:34
 */
public final class CodecCxt {

    private int depth;

    private final Set<Object> visits = new HashSet<>();
    private final XType       targetType;

    public CodecCxt(XType targetType) {
        this.targetType = targetType;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Set<Object> getVisits() {
        return visits;
    }

    public XType getTargetType() {
        return targetType;
    }

}
