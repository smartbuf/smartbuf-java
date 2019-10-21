package com.github.sisyphsu.datube.proto;

import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-08 11:11:31
 */
public class OutputNamePoolTest {

    @Test
    public void test() {
        OutputNamePool pool = new OutputNamePool(new Schema(true));

        pool.register(true, "tmp1", "tmp2", "tmp3", "tmp4", "tmp5", "tmp6", "tmp7", "tmp1");
        assert pool.size() == 7;
        pool.register(false, "cxt1", "cxt2", "cxt3", "cxt4", "cxt5", "cxt6", "cxt1", "tmp7", "tmp1");
        assert pool.size() == 13;

        assert pool.findNameID("tmp6") == 0;
        assert pool.findNameID("tmp2") == 1;
        assert pool.findNameID("tmp5") == 4;
        assert pool.findNameID("cxt1") == 5;
        assert pool.findNameID("cxt6") == 10;
        assert pool.findNameID("tmp1") == 12;

        assert Objects.equals(pool.findNameByID(3), "tmp4");
        assert Objects.equals(pool.findNameByID(9), "cxt5");

        try {
            pool.findNameID("nnnnn");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        try {
            pool.findNameByID(-1);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        try {
            pool.findNameByID(100);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }


        pool.unregister("tmp2", "tmp3"); // do nothing
        assert pool.size() == 13;

        pool.unregister("nnnnnn"); // do nothing
        assert pool.size() == 13;

        assert pool.findNameID("cxt5") == 9;
        pool.unregister("cxt5");
        try {
            pool.findNameByID(9);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        assert Objects.equals(pool.findNameByID(10), "cxt6");

        pool.reset();
        assert pool.size() == 7;

        pool.unregister("tmp1");
        assert pool.size() == 6;

        pool.unregister("cxt1");
        assert pool.size() == 6;
        pool.unregister("cxt1");
        assert pool.size() == 5;
    }

}
