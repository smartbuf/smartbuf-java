package com.github.sisyphsu.nakedata.convertor.pipeline;

import java.util.Objects;

/**
 * Pipeline's key, means codec direction
 *
 * @author sulin
 * @since 2019-06-03 16:10:15
 */
public class PipelineKey {

    private final Class src;
    private final Class tgt;

    public PipelineKey(Class src, Class tgt) {
        this.src = src;
        this.tgt = tgt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PipelineKey that = (PipelineKey) o;
        return src.equals(that.src) && tgt.equals(that.tgt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, tgt);
    }

}
