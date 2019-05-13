package com.github.sisyphsu.nakedata.convertor.adaptor.sql;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.sql.Blob;

/**
 * @author sulin
 * @since 2019-05-13 18:10:14
 */
public class BlobAdaptor extends Adaptor<Blob, byte[]> {

    @Override
    protected byte[] toTargetNotNull(Blob blob) {
        return new byte[0];
    }

    @Override
    protected Blob toSourceNotNull(byte[] bytes) {
        return null;
    }

}
