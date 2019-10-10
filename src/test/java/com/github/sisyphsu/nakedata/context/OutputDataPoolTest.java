package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.context.OutputDataPool;
import com.github.sisyphsu.nakedata.utils.TimeUtils;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-10-09 17:04:33
 */
public class OutputDataPoolTest {

    @Test
    public void test() {
        OutputDataPool dataPool = new OutputDataPool(16);

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

        dataPool.registerFloat(1.0f);
        dataPool.registerDouble(1.0);
        dataPool.registerVarint(1);
        dataPool.registerString("1");
        dataPool.registerSymbol("1");
        assert dataPool.size() == 5;
        assert dataPool.findFloatID(1.0f) == 4;
        assert dataPool.findDoubleID(1.0) == 5;
        assert dataPool.findVarintID(1) == 6;
        assert dataPool.findStringID("1") == 7;
        assert dataPool.findSymbolID("1") == 8;

        dataPool.registerFloat(1.0f);
        dataPool.registerDouble(1.0);
        dataPool.registerVarint(1);
        dataPool.registerString("1");
        dataPool.registerSymbol("1");
        assert dataPool.size() == 5;
        assert dataPool.findFloatID(1.0f) == 4;
        assert dataPool.findDoubleID(1.0) == 5;
        assert dataPool.findVarintID(1) == 6;
        assert dataPool.findStringID("1") == 7;
        assert dataPool.findSymbolID("1") == 8;

        dataPool.registerFloat(2.0f);
        dataPool.registerDouble(2.0);
        dataPool.registerVarint(2);
        dataPool.registerString("2");
        dataPool.registerSymbol("2");
        assert dataPool.size() == 10;
        assert dataPool.findFloatID(1.0f) == 4;
        assert dataPool.findFloatID(2.0f) == 5;
        assert dataPool.findDoubleID(1.0) == 6;
        assert dataPool.findDoubleID(2.0) == 7;
        assert dataPool.findVarintID(1) == 8;
        assert dataPool.findVarintID(2) == 9;
        assert dataPool.findStringID("1") == 10;
        assert dataPool.findStringID("2") == 11;
        assert dataPool.findSymbolID("1") == 12;
        assert dataPool.findSymbolID("2") == 13;


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

        dataPool.reset();
        assert dataPool.size() == 2;
        assert dataPool.findSymbolID("1") == 4;
        assert dataPool.findSymbolID("2") == 5;
    }

    @Test
    public void testExpire() throws InterruptedException {
        TimeUtils.INTERVAL = 5;
        Thread.sleep(1000);

        OutputDataPool dataPool = new OutputDataPool(4);
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
        assert dataPool.findSymbolID("1") == 4;
        assert dataPool.findSymbolID("2") == 5;
        assert dataPool.findSymbolID("4") == 7;

        dataPool.reset();
        assert dataPool.size() == 4;
        assert dataPool.findSymbolID("1") == 4;
        try {
            assert dataPool.findSymbolID("2") != 5;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        assert dataPool.findSymbolID("4") == 7;

        dataPool.registerSymbol("7");
        assert dataPool.size() == 5;
        assert dataPool.findSymbolID("4") == 7;
        assert dataPool.findSymbolID("7") == 5; // new '7' take 2's position

        dataPool.reset();
        assert dataPool.size() == 4;
        try {
            assert dataPool.findSymbolID("4") != 7;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

}
