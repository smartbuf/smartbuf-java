package com.github.smartbuf.benchmark;

import com.github.smartbuf.benchmark.large.LargeDeserBenchmark;
import com.github.smartbuf.benchmark.large.LargeSerialBenchmark;
import com.github.smartbuf.benchmark.medium.MediumDeserBenchmark;
import com.github.smartbuf.benchmark.medium.MediumSerialBenchmark;
import com.github.smartbuf.benchmark.small.SmallDeserBenchmark;
import com.github.smartbuf.benchmark.small.SmallSerialBenchmark;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author sulin
 * @since 2019-11-12 12:13:41
 */
public class Runner {

    public static void main(String[] args) {
        OptionsBuilder builder = new OptionsBuilder();

        builder.include(SmallSerialBenchmark.class.getName());
        builder.include(SmallDeserBenchmark.class.getName());

        builder.include(MediumSerialBenchmark.class.getName());
        builder.include(MediumDeserBenchmark.class.getName());

        builder.include(LargeSerialBenchmark.class.getName());
        builder.include(LargeDeserBenchmark.class.getName());

        try {
            new org.openjdk.jmh.runner.Runner(builder.build()).run();
        } catch (RunnerException e) {
            throw new RuntimeException(e);
        }
    }

}
