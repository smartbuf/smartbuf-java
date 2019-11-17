package com.github.smartbuf.transport;

import com.github.smartbuf.exception.InvalidDataException;
import com.github.smartbuf.exception.UnexpectedReadException;
import com.github.smartbuf.node.basic.SymbolNode;
import com.github.smartbuf.utils.TimeUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author sulin
 * @since 2019-10-09 17:04:33
 */
public class DataPoolTest {

    @Test
    public void testOutput() {
        OutputDataPool dataPool = new OutputDataPool(16);

        try {
            dataPool.registerSymbol(null);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
        try {
            dataPool.registerSymbol("");
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

        assert dataPool.registerFloat(0.0f) == 1;
        assert dataPool.registerDouble(0.0) == 1;
        assert dataPool.registerVarint(0L) == 1;
        assert dataPool.registerString("") == 1;

        assert dataPool.registerFloat(2.0f) == 2;
        assert dataPool.registerDouble(2.0) == 2;
        assert dataPool.registerVarint(2) == 2;
        assert dataPool.registerString("2") == 2;
        assert dataPool.registerSymbol("HI") == 1;

        // repeat
        assert dataPool.registerFloat(2.0f) == 2;
        assert dataPool.registerDouble(2.0) == 2;
        assert dataPool.registerVarint(2) == 2;
        assert dataPool.registerString("2") == 2;
        assert dataPool.registerSymbol("HI") == 1;

        assert dataPool.registerFloat(1.0f) == 3;
        assert dataPool.registerDouble(1.0) == 3;
        assert dataPool.registerVarint(1) == 3;
        assert dataPool.registerString("1") == 3;
        assert dataPool.registerSymbol("HIIIII") == 2;

        dataPool.reset();
        assert dataPool.registerFloat(1.0f) == 2;
        assert dataPool.registerDouble(1.0) == 2;
        assert dataPool.registerVarint(1) == 2;
        assert dataPool.registerString("1") == 2;
        assert dataPool.registerSymbol("HIIIII") == 2;
        assert dataPool.registerSymbol("HI") == 1;
    }

    @Test
    public void testExpire() throws InterruptedException {
        TimeUtils.INTERVAL = 5;
        Thread.sleep(1000);

        OutputDataPool dataPool = new OutputDataPool(4);
        Thread.sleep(10);
        assert dataPool.registerSymbol("1") == 1;
        Thread.sleep(10);
        assert dataPool.registerSymbol("2") == 2;
        Thread.sleep(10);
        assert dataPool.registerSymbol("3") == 3;
        Thread.sleep(10);
        assert dataPool.registerSymbol("4") == 4;
        Thread.sleep(10);
        assert dataPool.registerSymbol("5") == 5;
        Thread.sleep(10);
        assert dataPool.registerSymbol("6") == 6;
        Thread.sleep(10);
        assert dataPool.registerSymbol("1") == 1;

        dataPool.reset(); // 2, 3 was released
        assert dataPool.registerSymbol("1") == 1;
        assert dataPool.registerSymbol("7") == 2; // new '7' take 2's position
        assert dataPool.registerSymbol("8") == 3; // new '8' take 3's position
        assert dataPool.registerSymbol("2") == 7;
        assert dataPool.registerSymbol("3") == 8;
        assert dataPool.registerSymbol("5") == 5;
        assert dataPool.registerSymbol("7") == 2;

        dataPool.reset(); // remain 7,5,3,2
    }

    @Test
    public void testInputError() throws IOException {
        InputDataPool pool = new InputDataPool();

        InputBuffer buf = InputBuffer.valueOf(new byte[]{(byte) 0b01111111, (byte) 0b01111111});

        try {
            pool.read(buf);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectedReadException;
        }

        try {
            pool.getSymbol(10);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidDataException;
        }

        Output output = new Output(true);
        byte[] data = output.write(SymbolNode.valueOf("TEST"));
        buf = InputBuffer.valueOf(data);
        buf.readByte(); // ignore head
        buf.readByte(); // ignore seq
        pool.read(buf);

        assert pool.getSymbol(1).equals("TEST");
        try {
            pool.getSymbol(2);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidDataException;
        }

        try {
            pool.getFloat(10000);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidDataException;
        }
        try {
            pool.getDouble(10000);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidDataException;
        }
        try {
            pool.getVarint(10000);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidDataException;
        }
        try {
            pool.getString(10000);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidDataException;
        }
    }

}
