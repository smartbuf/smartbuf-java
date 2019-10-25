package com.github.sisyphsu.datatube.proto;

import com.github.sisyphsu.datatube.utils.TimeUtils;
import org.junit.jupiter.api.Test;

import static com.github.sisyphsu.datatube.proto.Const.*;

/**
 * @author sulin
 * @since 2019-10-09 17:04:33
 */
public class OutputDataPoolTest {

    private static Schema schema = new Schema(true);

    @Test
    public void test() {
        OutputDataPool dataPool = new OutputDataPool(schema, 16);

        try {
            dataPool.registerSymbol(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }
        try {
            dataPool.registerString(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }

        dataPool.registerFloat(0.0f);
        dataPool.registerDouble(0.0);
        dataPool.registerVarint(0L);
        dataPool.registerString("");
        assert dataPool.findStringID("") == ID_ZERO_STRING;
        assert dataPool.findFloatID(0f) == ID_ZERO_FLOAT;
        assert dataPool.findDoubleID(0.0) == ID_ZERO_DOUBLE;
        assert dataPool.findVarintID(0L) == ID_ZERO_VARINT;

        dataPool.registerFloat(1.0f);
        dataPool.registerDouble(1.0);
        dataPool.registerVarint(1);
        dataPool.registerString("1");
        dataPool.registerSymbol("1");
        assert dataPool.size() == 5;
        assert dataPool.findFloatID(1.0f) == ID_PREFIX;
        assert dataPool.findDoubleID(1.0) == ID_PREFIX + 1;
        assert dataPool.findVarintID(1) == ID_PREFIX + 2;
        assert dataPool.findStringID("1") == ID_PREFIX + 3;
        assert dataPool.findSymbolID("1") == ID_PREFIX + 4;

        dataPool.registerFloat(1.0f);
        dataPool.registerDouble(1.0);
        dataPool.registerVarint(1);
        dataPool.registerString("1");
        dataPool.registerSymbol("1");
        assert dataPool.size() == 5;
        assert dataPool.findFloatID(1.0f) == ID_PREFIX;
        assert dataPool.findDoubleID(1.0) == ID_PREFIX + 1;
        assert dataPool.findVarintID(1) == ID_PREFIX + 2;
        assert dataPool.findStringID("1") == ID_PREFIX + 3;
        assert dataPool.findSymbolID("1") == ID_PREFIX + 4;

        dataPool.registerFloat(2.0f);
        dataPool.registerDouble(2.0);
        dataPool.registerVarint(2);
        dataPool.registerString("2");
        dataPool.registerSymbol("2");
        assert dataPool.size() == 10;
        assert dataPool.findFloatID(1.0f) == ID_PREFIX;
        assert dataPool.findFloatID(2.0f) == ID_PREFIX + 1;
        assert dataPool.findDoubleID(1.0) == ID_PREFIX + 2;
        assert dataPool.findDoubleID(2.0) == ID_PREFIX + 3;
        assert dataPool.findVarintID(1) == ID_PREFIX + 4;
        assert dataPool.findVarintID(2) == ID_PREFIX + 5;
        assert dataPool.findStringID("1") == ID_PREFIX + 6;
        assert dataPool.findStringID("2") == ID_PREFIX + 7;
        assert dataPool.findSymbolID("1") == ID_PREFIX + 8;
        assert dataPool.findSymbolID("2") == ID_PREFIX + 9;

        try {
            dataPool.findFloatID(1000f);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            dataPool.findDoubleID(1000);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            dataPool.findVarintID(1000);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            dataPool.findStringID("111111");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            dataPool.findSymbolID("111111");
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        schema.reset();
        dataPool.reset();
        assert dataPool.size() == 2;
        assert dataPool.findSymbolID("1") == ID_PREFIX;
        assert dataPool.findSymbolID("2") == ID_PREFIX + 1;
    }

    @Test
    public void testExpire() throws InterruptedException {
        TimeUtils.INTERVAL = 5;
        Thread.sleep(1000);

        OutputDataPool dataPool = new OutputDataPool(schema, 4);
        Thread.sleep(10);
        dataPool.registerSymbol("1");
        Thread.sleep(10);
        dataPool.registerSymbol("2");
        Thread.sleep(10);
        dataPool.registerSymbol("3");
        Thread.sleep(10);
        dataPool.registerSymbol("4");
        Thread.sleep(10);
        dataPool.registerSymbol("5");
        Thread.sleep(10);
        dataPool.registerSymbol("6");
        Thread.sleep(10);
        dataPool.registerSymbol("1");

        assert dataPool.size() == 6;
        assert dataPool.findSymbolID("1") == ID_PREFIX;
        assert dataPool.findSymbolID("2") == ID_PREFIX + 1;
        assert dataPool.findSymbolID("4") == ID_PREFIX + 3;

        schema.reset();
        dataPool.reset();
        assert dataPool.size() == 4;
        assert dataPool.findSymbolID("1") == ID_PREFIX;
        try {
            assert dataPool.findSymbolID("2") != ID_PREFIX + 1;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        assert dataPool.findSymbolID("4") == ID_PREFIX + 3;

        dataPool.registerSymbol("7");
        assert dataPool.size() == 5;
        assert dataPool.findSymbolID("4") == ID_PREFIX + 3;
        assert dataPool.findSymbolID("7") == ID_PREFIX + 1; // new '7' take 2's position

        schema.reset();
        dataPool.reset();
        assert dataPool.size() == 4;
        try {
            assert dataPool.findSymbolID("4") != ID_PREFIX + 3;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

}
