package com.github.sisyphsu.smartbuf.transport;

import com.github.sisyphsu.smartbuf.exception.InvalidStructException;
import com.github.sisyphsu.smartbuf.exception.UnexpectedReadException;
import com.github.sisyphsu.smartbuf.node.basic.ObjectNode;
import com.github.sisyphsu.smartbuf.utils.TimeUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-08 16:02:37
 */
public class MetaPoolTest {

    @Test
    public void test() {
        OutputMetaPool pool = new OutputMetaPool(16);
        try {
            pool.registerTmpStruct((String[]) null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }
        try {
            pool.registerCxtStruct((String[]) null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }
        assert pool.registerCxtStruct() == 0;
        assert pool.registerTmpStruct() == 0;

        assert pool.registerCxtStruct("id1", "name") == 2 + 1;
        assert pool.registerCxtStruct("id.2", "name") == 2 * 2 + 1;
        assert pool.registerCxtStruct("id.3", "name") == 3 * 2 + 1;
        assert pool.registerCxtStruct("id.4", "name") == 4 * 2 + 1;
        assert pool.registerCxtStruct("id.5", "name") == 5 * 2 + 1;
        assert pool.registerCxtStruct("id1", "name") == 3;

        assert pool.cxtStructIndex.size() == 5;
        assert pool.registerCxtStruct("id1", "name") == 3;
        assert pool.registerCxtStruct("id.5", "name") == 5 * 2 + 1;

        // temporary struct
        assert pool.registerTmpStruct("id1", "name", "desc") == 2;
        assert pool.registerTmpStruct("id2", "name", "desc") == 2 * 2;
        assert pool.registerTmpStruct("id3", "name", "desc") == 3 * 2;
        assert pool.registerTmpStruct("id4", "name", "desc") == 4 * 2;
        assert pool.registerTmpStruct("id5", "name", "desc") == 5 * 2;
        assert pool.registerTmpStruct("id6", "name", "desc") == 6 * 2;
        assert pool.registerTmpStruct("id1", "name", "desc") == 2;

        assert pool.tmpStructIndex.size() == 6;

        pool.reset();
        assert pool.registerCxtStruct("id.5", "name") == 5 * 2 + 1;
        assert pool.registerTmpStruct("id6", "name", "desc") == 2;
    }

    @Test
    public void testStruct() {
        OutputMetaPool.Struct struct1 = new OutputMetaPool.Struct(new String[]{"id", "name"}, null);
        OutputMetaPool.Struct struct2 = new OutputMetaPool.Struct(new String[]{"id", "name"}, null);

        assert struct1.equals(struct2);
        assert struct1.hashCode() == struct2.hashCode();

        assert !struct1.equals(new Object());
    }

    @Test
    public void testExpire() throws InterruptedException {
        TimeUtils.INTERVAL = 8;
        Thread.sleep(1000);

        OutputMetaPool pool = new OutputMetaPool(8);

        Thread.sleep(10);
        assert pool.registerCxtStruct("id1", "name") == 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id2", "name") == 2 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id3", "name") == 3 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id4", "name") == 4 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id5", "name") == 5 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id1", "name") == 2 + 1;
        Thread.sleep(10);

        assert pool.registerCxtStruct("id6", "name") == 6 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id7", "name") == 7 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id8", "name") == 8 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id9", "name") == 9 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id0", "name") == 10 * 2 + 1;
        Thread.sleep(10);

        pool.reset(); // 2, 3 released
        assert pool.cxtStructIndex.size() == 8;
        assert pool.registerCxtStruct("id3", "name") == 2 * 2 + 1;
        assert pool.registerCxtStruct("id2", "name") == 3 * 2 + 1;

        assert pool.registerCxtStruct("id5", "name") == 5 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id10", "name") == 11 * 2 + 1;
        Thread.sleep(10);
        assert pool.registerCxtStruct("id11", "name") == 12 * 2 + 1;
        Thread.sleep(10);

        pool.reset(); // 1, 4 released
        assert pool.registerCxtStruct("id4", "name") == 2 + 1;
        assert pool.registerCxtStruct("id1", "name") == 4 * 2 + 1;
    }

    @Test
    public void testNames() {
        OutputMetaPool.Names names1 = new OutputMetaPool.Names();
        names1.names = new String[]{"id"};

        OutputMetaPool.Names names2 = new OutputMetaPool.Names();
        names2.names = new String[]{"id", "name"};

        OutputMetaPool.Names names3 = new OutputMetaPool.Names();
        names3.names = new String[]{"id"};

        System.out.println(Objects.deepEquals(names1.names, names3.names));
        System.out.println(names3.equals(names1));

        assert !names3.equals(names2);
        assert names3.equals(names1);
        assert !names3.equals(new Object());
    }

    @Test
    public void testError() throws IOException {
        InputBuffer buffer = new InputBuffer();
        InputMetaPool pool = new InputMetaPool();

        try {
            buffer.reset(new byte[]{0b01111111});
            pool.read(buffer);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectedReadException;
        }

        Output output = new Output(true);
        byte[] bytes = output.write(new ObjectNode(true, new String[]{"id", "name"}, new Object[]{1, "hello"}));

        buffer.reset(bytes);
        buffer.readByte();
        buffer.readByte();
        pool.read(buffer);

        try {
            pool.findStructByID(1);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidStructException;
        }

        try {
            pool.findStructByID(2);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidStructException;
        }

        try {
            pool.findStructByID(11);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidStructException;
        }

        try {
            pool.findStructByID(5);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidStructException;
        }
    }

}
