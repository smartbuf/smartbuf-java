package com.github.smartbuf.transport;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-09-30 18:01:40
 */
public class IDAllocatorTest {

    @Test
    public void test() {
        IDAllocator allocator = new IDAllocator();
        Assertions.assertEquals(0, allocator.acquire());
        Assertions.assertEquals(1, allocator.acquire());
        Assertions.assertEquals(2, allocator.acquire());
        Assertions.assertEquals(3, allocator.acquire());
        Assertions.assertNotEquals(5, allocator.acquire());

        allocator.release(2);
        allocator.release(1);
        Assertions.assertEquals(1, allocator.acquire());
        Assertions.assertEquals(2, allocator.acquire());

        allocator.release(0);
        allocator.release(3);
        Assertions.assertEquals(0, allocator.acquire());
        Assertions.assertEquals(3, allocator.acquire());

        try {
            allocator.release(10);
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }

        assert allocator.acquire() == 5;
        assert allocator.acquire() == 6;
        assert allocator.acquire() == 7;
        assert allocator.acquire() == 8;
        assert allocator.acquire() == 9;
        assert allocator.acquire() == 10;

        allocator.release(6);
        allocator.release(7);
        allocator.release(8);
        allocator.release(9);
        allocator.release(3);
        allocator.release(1);

        assert allocator.acquire() == 1;
        assert allocator.acquire() == 3;
    }

}
