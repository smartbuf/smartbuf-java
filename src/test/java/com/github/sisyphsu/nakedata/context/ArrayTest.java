package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.context.Array;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-09-29 16:36:47
 */
public class ArrayTest {

    @Test
    public void test() {
        Array<Long> array = new Array<>();
        array.add(1L);
        array.add(2L);
        array.add(3L);
        array.add(4L);
        array.add(5L);

        assert array.size() == 5;

        array.clear();
        assert array.size() == 0;
        assert array.cap() > 0;
    }

}
