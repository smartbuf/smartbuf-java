package com.github.sisyphsu.datatube.proto;

import com.github.sisyphsu.datatube.utils.TimeUtils;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-10-08 16:02:37
 */
public class OutputStructPoolTest {

    @Test
    public void test() {
        OutputStructPool pool = new OutputStructPool(new Schema(true), 16);

        try {
            pool.register(false, null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }

        pool.register(true, new String[]{"id1", "name"});
        pool.register(true, new String[]{"id.2", "name"});
        pool.register(true, new String[]{"id.3", "name"});
        pool.register(true, new String[]{"id.4", "name"});
        pool.register(true, new String[]{"id.5", "name"});
        pool.register(true, new String[]{"id1", "name"});

        assert pool.size() == 5;
        assert pool.findStructID(new String[]{"id1", "name"}) == 1;
        assert pool.findStructID(new String[]{"id.5", "name"}) == 5;

        pool.register(false, new String[]{"id1", "name", "desc"});
        pool.register(false, new String[]{"id2", "name", "desc"});
        pool.register(false, new String[]{"id3", "name", "desc"});
        pool.register(false, new String[]{"id4", "name", "desc"});
        pool.register(false, new String[]{"id5", "name", "desc"});
        pool.register(false, new String[]{"id6", "name", "desc"});
        pool.register(false, new String[]{"id1", "name", "desc"});

        assert pool.size() == 11;
        assert pool.findStructID(new String[]{"id2", "name", "desc"}) == 7;
        assert pool.findStructID(new String[]{"id6", "name", "desc"}) == 11;

        try {
            pool.findStructID(new String[]{"ssd"});
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        pool.register(true, new String[]{"id7", "name"});
        assert pool.findStructID(new String[]{"id7", "name"}) == 6;
        assert pool.size() == 12;

        pool.register(false, new String[]{"id7", "name"});
        assert pool.findStructID(new String[]{"id7", "name"}) == 12;
        assert pool.size() == 12;

        pool.register(false, new String[]{"id1", "name"});
        assert pool.findStructID(new String[]{"id1", "name"}) == 12;
        assert pool.findStructID(new String[]{"id.5", "name"}) == 1;
        assert pool.size() == 12;

        pool.reset();
        assert pool.size() == 8;
        assert pool.findStructID(new String[]{"id1", "name", "desc"}) == 1;
    }

    @Test
    public void testStruct() {
        OutputStructPool.Struct struct1 = new OutputStructPool.Struct(false, 0, 0, new String[]{"id", "name"});
        OutputStructPool.Struct struct2 = new OutputStructPool.Struct(false, 0, 0, new String[]{"id", "name"});

        assert struct1.equals(struct2);
        assert struct1.hashCode() == struct2.hashCode();

        assert !struct1.equals(new Object());
    }

    @Test
    public void testExpire() throws InterruptedException {
        TimeUtils.INTERVAL = 8;
        Thread.sleep(1000);

        OutputStructPool pool = new OutputStructPool(new Schema(true), 8);

        Thread.sleep(10);
        pool.register(false, new String[]{"id1", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id2", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id3", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id4", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id5", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id1", "name"});
        Thread.sleep(10);

        pool.register(false, new String[]{"id6", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id7", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id8", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id9", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id0", "name"});
        Thread.sleep(10);

        pool.reset();
        assert pool.size() == 8;
        assert pool.findStructID(new String[]{"id1", "name"}) == 1;

        // id2 & id3 released
        try {
            pool.findStructID(new String[]{"id2", "name"});
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            pool.findStructID(new String[]{"id3", "name"});
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        pool.register(false, new String[]{"id5", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id10", "name"});
        Thread.sleep(10);
        pool.register(false, new String[]{"id11", "name"});
        Thread.sleep(10);

        pool.reset();
        assert pool.size() == 8;
        assert pool.findStructID(new String[]{"id5", "name"}) == 5;

        // id4 & id1 released
        try {
            pool.findStructID(new String[]{"id4", "name"});
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            pool.findStructID(new String[]{"id1", "name"});
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

}
