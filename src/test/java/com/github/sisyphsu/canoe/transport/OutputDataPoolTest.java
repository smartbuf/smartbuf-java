package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.utils.TimeUtils;
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
            assert e instanceof IllegalArgumentException;
        }
        try {
            dataPool.registerString(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }

        assert dataPool.registerFloat(0.0f) == 0;
        assert dataPool.registerDouble(0.0) == 0;
        assert dataPool.registerVarint(0L) == 0;
        assert dataPool.registerString("") == 0;

        assert dataPool.registerFloat(2.0f) == 1;
        assert dataPool.registerDouble(2.0) == 1;
        assert dataPool.registerVarint(2) == 1;
        assert dataPool.registerString("2") == 1;
        assert dataPool.registerSymbol("HI") == 0;

        // repeat
        assert dataPool.registerFloat(2.0f) == 1;
        assert dataPool.registerDouble(2.0) == 1;
        assert dataPool.registerVarint(2) == 1;
        assert dataPool.registerString("2") == 1;
        assert dataPool.registerSymbol("HI") == 0;

        assert dataPool.registerFloat(1.0f) == 2;
        assert dataPool.registerDouble(1.0) == 2;
        assert dataPool.registerVarint(1) == 2;
        assert dataPool.registerString("1") == 2;
        assert dataPool.registerSymbol("HIIIII") == 1;

        dataPool.reset();
        assert dataPool.registerFloat(1.0f) == 1;
        assert dataPool.registerDouble(1.0) == 1;
        assert dataPool.registerVarint(1) == 1;
        assert dataPool.registerString("1") == 1;
        assert dataPool.registerSymbol("HIIIII") == 1;
        assert dataPool.registerSymbol("HI") == 0;
    }

    @Test
    public void testExpire() throws InterruptedException {
        TimeUtils.INTERVAL = 5;
        Thread.sleep(1000);

        OutputDataPool dataPool = new OutputDataPool(4);
        Thread.sleep(10);
        assert dataPool.registerSymbol("1") == 0;
        Thread.sleep(10);
        assert dataPool.registerSymbol("2") == 1;
        Thread.sleep(10);
        assert dataPool.registerSymbol("3") == 2;
        Thread.sleep(10);
        assert dataPool.registerSymbol("4") == 3;
        Thread.sleep(10);
        assert dataPool.registerSymbol("5") == 4;
        Thread.sleep(10);
        assert dataPool.registerSymbol("6") == 5;
        Thread.sleep(10);
        assert dataPool.registerSymbol("1") == 0;

        dataPool.reset(); // 2, 3 was released
        assert dataPool.registerSymbol("1") == 0;
        assert dataPool.registerSymbol("7") == 1; // new '7' take 2's position
        assert dataPool.registerSymbol("8") == 2; // new '8' take 3's position
        assert dataPool.registerSymbol("2") == 6;
        assert dataPool.registerSymbol("3") == 7;
        assert dataPool.registerSymbol("5") == 4;
        assert dataPool.registerSymbol("7") == 1;

        dataPool.reset(); // remain 7,5,3,2
        assert dataPool.symbolIndex.size() == 4;
    }

}
