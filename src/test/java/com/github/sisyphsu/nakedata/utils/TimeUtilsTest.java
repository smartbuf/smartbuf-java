package com.github.sisyphsu.nakedata.utils;

import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-10-08 21:20:43
 */
public class TimeUtilsTest {

    @Test
    public void test() throws InterruptedException {
        long now = TimeUtils.fastNow();
        long uptime = TimeUtils.fastUpTime();
        System.out.println(now);
        System.out.println(uptime);

        Thread.sleep(1000);

        assert TimeUtils.fastNow() > now;
        assert TimeUtils.fastUpTime() > uptime;
    }

}
