package com.github.sisyphsu.nakedata.utils;

import com.github.sisyphsu.nakedata.exception.AssertException;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-04-27 14:50:47
 */
public class AssetUtils {

    public static void assetEqual(Object... objs) {
        if (objs == null || objs.length < 2) {
            return;
        }
        Object obj = objs[0];
        for (int i = 1; i < objs.length; i++) {
            if (!Objects.equals(obj, objs[i])) {
                throw new AssertException(String.format("Not equal: %s, %s", obj, objs[i]));
            }
        }
    }

}
