package com.github.sisyphsu.canoe.benchmark;

import com.github.sisyphsu.canoe.IOWriter;
import com.github.sisyphsu.canoe.transport.OutputWriter;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * WriteByte
 * Benchmark              Mode  Cnt    Score   Error  Units
 * WriteBenchmark.direct  avgt    6  274.103 ± 1.252  ns/op
 * WriteBenchmark.writer  avgt    6  301.634 ± 0.801  ns/op
 *
 * @author sulin
 * @since 2019-10-30 18:53:48
 */
@Warmup(iterations = 2, time = 2)
@Fork(2)
@Measurement(iterations = 3, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class WriteBenchmark {

    private static final byte[]       buf    = new byte[1024];
    private static final OutputWriter writer = new OutputWriter(new ByteArrayWriter());
    private static       int          off;

    @Benchmark
    public void direct() {
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) i;
        }
    }

    @Benchmark
    public void writer() throws IOException {
        off = 0;
        for (int i = 0; i < buf.length; i++) {
            writer.writeByte((byte) i);
        }
    }

    public static class ByteArrayWriter implements IOWriter {

        @Override
        public void write(byte b) {
            buf[off++] = b;
        }
    }

}
